/*
 *    Copyright (c) 2018-2025, hodo All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: 江苏红豆工业互联网有限公司
 */

package com.hodo.iiot.group2.hdcloud.bank.account.currency.controller;



import com.alibaba.fastjson.JSONObject;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;


import com.hodo.hdcloud.common.security.annotation.Inner;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankAccountService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.jjUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankaccount")
@Api(value = "hdbankaccount", tags = "银行账户管理")
public class HdBankAccountController extends BaseContrller {

    private final HdBankAccountService hdBankAccountService;

    /**
     * 分页查询
     * @param
     * @return
     */
    //成員單位和名稱封裝
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankAccountPage(@RequestParam() Map<String,Object> params) {
        return hdBankAccountService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id
     * @return R
     */
    //銀行名稱
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {

        return hdBankAccountService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdBankAccount bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdbankaccount_add')")
    public R save(@RequestBody HdBankAccount hdBankAccount) {

        return hdBankAccountService.save(hdBankAccount);
    }

    /**
     * 修改bank_account
     * @param hdBankAccount bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdbankaccount_edit')")
    public R updateById(@RequestBody HdBankAccount hdBankAccount) {
        return hdBankAccountService.updateById(hdBankAccount);
    }

    /**
     * 通过id删除bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable String id) {
        return this.remove(id);
    }


    //批量删除
    @ApiOperation("批量删除公司名匹配表")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody() String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                R result = this.remove(id);
                if(result.getCode()==1){
                    return result;
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行账户删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return R.ok( message);
    }

    private R remove(String id){
        List<HdBankStatement> hdBankStatements = remoteBillService.getBankStatementByAccount(id);
        if(hdBankStatements!=null&&hdBankStatements.size()>0){
            return R.failed("该账户下有账单信息,删除会导致统计错误！");
        }
        return hdBankAccountService.removeById(id);
    }

    //获取全部

    //导入
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        //内转账号，外部账号，银行名称必填
        List<HdBankAccount> hdBankAccounts;
        R result = new R();
        //List<String> testComName = new ArrayList<>();
        ExcelUtil excelUtil = new ExcelUtil<HdBankStatement>();
        try {
            hdBankAccounts = excelUtil.importXls(file, HdBankAccount.class);
            int count = 0;
            if (hdBankAccounts != null && hdBankAccounts.size() > 0) {
                for (HdBankAccount hdBankAccount : hdBankAccounts) {
                    count++;
                    if (StringUtil.isEmpty(hdBankAccount.getExternalBankAccountId())) {
                        return R.failed("第" + count + "条数据外转账号为空");
                    }
                    if (StringUtil.isEmpty(hdBankAccount.getBankCode())) {
                        return R.failed("第" + count + "条数据银行编号为空");
                    }
                    if (StringUtil.isEmpty(hdBankAccount.getBankName())) {
                        return R.failed("第" + count + "条数据银行名称为空");
                    }
                    //testComName.add(hdNzDict.getBank());
                    //也没有说不能出现一个编号两个名称
                    //根据编号查名称,控制不了excel出现两个code一个名称
                    Map<String,Object> params = new HashedMap();
                    params.put("bankCode",hdBankAccount.getBankCode());
                    List<HdBankAccount> bankNameStrs = remoteBaseService.selectListAllByTenant(params);
                    if(bankNameStrs!=null&&bankNameStrs.size()>0){
                        hdBankAccount.setBankName(bankNameStrs.get(0).getBankName());
                    }
                    //7.4
//                    String hdBankId = hdBankBiz.getBankId(hdNzDict.getBankCode());
//                    if(hdBankId==null){
//                        return new ObjectRestResponse<>(1004, "第" + count + "条系统无法匹配银行名称");
//                    }
//                    hdNzDict.setBank(hdBankId);
                    //6.25
                    //根据bankType获取信息
                    if(hdBankAccount.getBankType()!=null&&StringUtil.isNotEmpty(hdBankAccount.getBankType())) {
                        if ("一般户".equals(hdBankAccount.getBankType())) {
                            hdBankAccount.setBankType("0");
                        } else if ("专户".equals(hdBankAccount.getBankType())) {
                            hdBankAccount.setBankType("1");
                        } else if ("理财户".equals(hdBankAccount.getBankType())) {
                            hdBankAccount.setBankType("2");
                        } else if ("基本户".equals(hdBankAccount.getBankType())){
                            hdBankAccount.setBankType("3");
                        } else if("贷款户".equals(hdBankAccount.getBankType())){
                            hdBankAccount.setBankType("4");
                        } else {
                            return R.failed("第" + count + "条银行类型错误,只能为(一般户,专户,理财户,基本户,贷款户)");
                        }
                    }
                }
                result = remoteBaseService.addBankAccountList(hdBankAccounts);
            }

        } catch (DuplicateKeyException e) {
            return R.failed( "请确认内部账号外部账号无重复");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("导入出错");
        }
        return result;
    }
    //导出
    /**
     * 导出
     */
    //添加录入人条件6.24
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
//    @IgnoreUserToken
//    @IgnoreClientToken
    public void exportXls(@RequestParam() Map<String,Object> params) throws ParseException {
        List<HdBankAccount> allHdBankAccountList = remoteBaseService.selectListAllByTenant(params);
        ExcelUtil excelUtil = new ExcelUtil<HdBankAccount>();
        if(allHdBankAccountList!=null&&allHdBankAccountList.size()>0){
            for(HdBankAccount hdBankAccount:allHdBankAccountList){
                if ("0".equals(hdBankAccount.getBankType())) {
                    hdBankAccount.setBankType("一般户");
                } else if ("1".equals(hdBankAccount.getBankType())) {
                    hdBankAccount.setBankType("专户");
                } else if ("2".equals(hdBankAccount.getBankType())) {
                    hdBankAccount.setBankType("理财户");
                } else if ("3".equals(hdBankAccount.getBankType())){
                    hdBankAccount.setBankType("基本户");
                } else if("4".equals(hdBankAccount.getBankType())){
                    hdBankAccount.setBankType("贷款户");
                }else{
                    hdBankAccount.setBankType("其他");
                }
            }
        }
        if(allHdBankAccountList==null){
            allHdBankAccountList = new ArrayList<>();
            System.out.println("账户查询失败");
        }
        try {
            excelUtil.print("银行账户管理列表", HdBankAccount.class, "银行账户管理列表", "导出时间:" + jjUtil.getDateStr(), "银行账户管理列表", allHdBankAccountList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取开户行直接用page

    //根据抬头获取开户行要转一下所以无法直接用page
    @ApiOperation("根据抬头获取开户行接口*")
    @RequestMapping(value = "/getDictListByHead", method = RequestMethod.GET)
    public R getDictList(String tenant_name) {
        //根据租户名称去中间表获取id
        Map<String,Object> paramsTenant = new HashedMap();
        paramsTenant.put("tenant_name",tenant_name);
        HdBankTenant hdBankTenant = remoteBaseService.getTenantByTerm(paramsTenant);
        String tenantId = "";
        if(hdBankTenant!=null){
            tenantId = hdBankTenant.getTenantId();
        }else{
            return R.ok(new ArrayList<>());
        }
        List<HdBankAccount> hdBankAccountList = new ArrayList<>();
        Map<String,Object> params = new HashedMap();
        params.put("tenantId",tenantId);
        return hdBankAccountService.page(params);
    }

}
