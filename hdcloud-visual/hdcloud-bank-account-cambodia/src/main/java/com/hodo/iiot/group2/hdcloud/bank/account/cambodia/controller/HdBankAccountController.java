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

package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankAccountService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.jjUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankaccount" )
@Api(value = "hdbankaccount", tags = "管理")
public class HdBankAccountController extends BaseContrller {

    private final HdBankAccountService hdBankAccountService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdBankAccountPage(Map<String,Object> params) {
        return hdBankAccountService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {

        return hdBankAccountService.getById(id);
    }

    /**
     * 新增
     * @param hdBankAccount 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdBankAccount hdBankAccount) {
        return hdBankAccountService.save(hdBankAccount);
    }

    /**
     * 修改
     * @param hdBankAccount 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdBankAccount hdBankAccount) {
        return hdBankAccountService.updateById(hdBankAccount);
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable String id) {
        Boolean result = removeTerm(id);
        if(result){
            return R.failed("该账户下有账单信息,删除会导致统计错误！");
        }
        return R.ok(null,"成功删除");
    }

    //批量删除
    @ApiOperation("批量删除公司名匹配表")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody() String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if(StringUtils.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        List<String> idList = new ArrayList<String>();
        int count = 0;
        try{
            //批量
            for(String id:ids.split(",")){
                Boolean result = removeTerm(id);
                if(result){
                    return R.failed("该账户下有账单信息,删除会导致统计错误！");
                }
                idList.add(id);
                count++;
            }
            remoteBaseService.removeBankAccountByIds(idList);
        }catch (Exception e){
            e.printStackTrace();
            R.failed("银行账户删除失败");
        }
        return R.ok(null,"你成功删除"+count+"条");

    }

    //删除前的判断
    private boolean removeTerm(String id){
        List<HdBankStatement> hdBankStatements = remoteBillService.getBankStatementByAccount(id);
        if(hdBankStatements!=null&&hdBankStatements.size()>0){
            return true;
        }
        return false;
    }

    //导入
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        List<HdBankAccount> hdBankAccounts = new ArrayList<>();
        R result = new R();
        int count = 0;
        ExcelUtil excelUtil = new ExcelUtil<HdBankAccount>();
        try{
            hdBankAccounts = excelUtil.importXls(file,HdBankAccount.class);
            if(hdBankAccounts!=null&&hdBankAccounts.size()>0){
                for(HdBankAccount hdBankAccount:hdBankAccounts){
                    count++;
                    if(StringUtils.isEmpty(hdBankAccount.getExternalBankAccountId())){
                        return R.failed("第" + count + "条数据外转账号为空");
                    }
                    if(StringUtils.isEmpty(hdBankAccount.getBankCode())){
                        return R.failed("第" + count + "条数据银行编号为空");
                    }
                    if(StringUtils.isEmpty(hdBankAccount.getBankName())){
                        return R.failed("第" + count + "条数据银行名称为空");
                    }
                    Map<String,Object> params = new HashedMap();
                    params.put("bankCode",hdBankAccount.getBankCode());
                    List<HdBankAccount> bankNameStrs = remoteBaseService.selectListAllByTenant(params);
                    if(bankNameStrs!=null&&bankNameStrs.size()>0){
                        hdBankAccount.setBankName(bankNameStrs.get(0).getBankName());
                    }
                    if(StringUtils.isNotEmpty(hdBankAccount.getBankType())){
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
                    }else{
                        return R.failed("第" + count + "条银行类型为空,只能为(一般户,专户,理财户,基本户,贷款户)");
                    }
                }
                result = remoteBaseService.addBankAccountList(hdBankAccounts);
            }
        }catch (DuplicateKeyException e){
            return R.failed("请确认内部账户或外部账号无重复");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入失败");
        }
        return result;
    }
    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
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
            if(allHdBankAccountList==null){
                allHdBankAccountList = new ArrayList<>();
                System.out.println("账户查询失败");
            }
        }
        try {
            excelUtil.print("银行账户管理列表", HdBankAccount.class, "银行账户管理列表", "导出时间:" + jjUtil.getDateStr(), "银行账户管理列表", allHdBankAccountList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据抬头获取开户行要转一下所以无法直接用page
    @ApiOperation("根据抬头获取开户行接口*")
    @RequestMapping(value = "/getDictListByHead", method = RequestMethod.GET)
    public R getDictList(String tenant_name) {
        //根据租户名中间表获取id
        Map<String,Object> paramsTenant = new HashedMap();
        paramsTenant.put("tenant_name",tenant_name);
        HdBankTenant hdBankTenant = remoteBaseService.getTenantByTerm(paramsTenant);
        Integer tenantId = 0;
        if(hdBankTenant!=null){
            tenantId  = hdBankTenant.getTenantId();
        }else{
            return R.ok(new ArrayList<>());
        }
        List<HdBankAccount> hdBankAccountList = new ArrayList<>();
        Map<String,Object> params = new HashedMap();
        params.put("tenantId",tenantId);
        return hdBankAccountService.page(params);
    }









}
