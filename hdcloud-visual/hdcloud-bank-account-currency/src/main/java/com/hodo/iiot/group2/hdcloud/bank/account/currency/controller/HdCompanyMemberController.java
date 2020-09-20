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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;

import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdCompanyMemberService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.jjUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdcompanymember")
@Api(value = "hdcompanymember", tags = "成员单位管理")
public class HdCompanyMemberController extends BaseContrller {

    private final HdCompanyMemberService hdCompanyMemberService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdCompanyMemberPage(@RequestParam() Map<String,Object> params) {
        return hdCompanyMemberService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return hdCompanyMemberService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdCompanyMember bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdcompanymember_add')")
    public R save(@RequestBody HdCompanyMember hdCompanyMember) {
        return hdCompanyMemberService.save(hdCompanyMember);
    }

    /**
     * 修改bank_account
     * @param hdCompanyMember bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdcompanymember_edit')")
    public R updateById(@RequestBody HdCompanyMember hdCompanyMember) {
        return hdCompanyMemberService.updateById(hdCompanyMember);
    }

    /**
     * 通过id删除bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    //@PreAuthorize("@pms.hasPermission('generator_hdcompanymember_del')")
    public R removeById(@PathVariable String id) {

        return this.remove(id);
    }

    //加入判断是否有成员单位匹配问题不能删除
    //批量删除
    @ApiOperation("批量删除银行操作记录")
    @RequestMapping(value = "/doBatchDel2", method = RequestMethod.POST)
    //@ResponseBody
    public R doBatchDel2(@RequestBody() String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        List<String> idStrs = new ArrayList<>();
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                R result = this.remove(id);
                if(result.getCode()==1){
                    return result;
                }
                idStrs.add(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司删除失败";
            return R.failed(message);
        }
        //验证完批量删除
        return remoteBaseService.removeCompanyMemberByIds(idStrs);

    }

    //封裝刪除用戶刪除判斷
    private R remove(String id){
        //账单根据成员单位查询账单
        Map<String,Object> params = new HashedMap();
        params.put("company_member_id",id);
        List<HdBankStatement> hdBankAccounts = remoteBillService.getAllSingle(params);

        List<HdMatchCompanyMember> hdMatchCompanyMembers = remoteBaseService.getAllSingleMatchCompanyMember(params);
        if(hdBankAccounts!=null&&hdBankAccounts.size()>0){
            return R.failed("某成员单位下有账单信息,删除会导致统计错误！");
        }
        if(hdMatchCompanyMembers!=null&&hdMatchCompanyMembers.size()>0){
            return R.failed("某成员单位下有匹配信息,请先删除匹配信息！");
        }

        return R.ok("成功");
    }

    /**
     * 导出
     */
    //添加录入人条件6.24
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(String company, String code,String superCompId) throws ParseException {

        //只有登录人可以导出
        Map<String,Object> params = new HashedMap();
        jjUtil.addParams(params,"company",company);
        jjUtil.addParams(params,"code",code);
        jjUtil.addParams(params,"superCompId",superCompId);
        List<HdCompanyMember> allCompanyEx = remoteBaseService.getAllMemberCompany(params);
        if(allCompanyEx==null){
            allCompanyEx = new ArrayList<>();
            System.out.println("成员单位查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdCompanyMember>();
        try {
            excelUtil.print("成员单位管理列表", HdCompanyMember.class, "成员单位管理列表", "导出时间:" + jjUtil.getDateStr(), "成员单位管理列表", allCompanyEx, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加录入人条件6.24
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file") MultipartFile file
    ) {

        List<HdCompanyMember> hdCompanyMembers;
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        int count = 0;
        try {
            hdCompanyMembers = excelUtil.importXls(file, HdCompanyMember.class);
            if (hdCompanyMembers != null && hdCompanyMembers.size() > 0) {
                for(HdCompanyMember hdCompanyMember:hdCompanyMembers){
                    count++;
                    if(StringUtil.isEmpty(hdCompanyMember.getCompanyCode())){
                        return R.failed( "第" + count + "条数据所属公司编号为空");
                    }
                    if(StringUtil.isEmpty(hdCompanyMember.getCode())){
                        return R.failed( "第" + count + "条数据成员单位编号为空");
                    }
                    if(StringUtil.isEmpty(hdCompanyMember.getName())){
                        return R.failed( "第" + count + "条数据成员单位名称为空");
                    }
                    Map params = new HashMap();
                    params.put("code",hdCompanyMember.getCompanyCode());
                    List<HdCompany> hdCompanies = remoteBaseService.getAllCompany(params);

                    if(hdCompanies.size()==0){
                        return R.failed("第" + count + "条数据所属公司编码不存在");
                    }
                    hdCompanyMember.setCompanyId(hdCompanies.get(0).getId());
                    hdCompanyMember.setCompanyName(hdCompanies.get(0).getName());
                    hdCompanyMember.setCompanyCode(hdCompanies.get(0).getCode());
                }

            }
        } catch (DuplicateKeyException e) {
            return R.failed( "导入相同单位名称或编码");
        } catch (SQLException e) {
            return R.failed("数据不完整");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("导入失败");
        }
        return remoteBaseService.addCompanyMemberList(hdCompanyMembers);

    }


}
