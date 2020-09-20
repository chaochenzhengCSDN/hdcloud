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
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdMatchCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdMatchCompanyMemberService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdmatchcompanymember")
@Api(value = "hdmatchcompanymember", tags = "成员单位匹配管理")
public class HdMatchCompanyMemberController extends BaseContrller {

    private final HdMatchCompanyMemberService hdMatchCompanyMemberService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdMatchCompanyMemberPage(@RequestParam() Map<String,Object> params) {
        return hdMatchCompanyMemberService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return hdMatchCompanyMemberService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdMatchCompanyMember bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdmatchcompanymember_add')")
    public R save(@RequestBody HdMatchCompanyMember hdMatchCompanyMember) {
        return hdMatchCompanyMemberService.save(hdMatchCompanyMember);
    }

    /**
     * 修改bank_account
     * @param hdMatchCompanyMember bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdmatchcompanymember_edit')")
    public R updateById(@RequestBody HdMatchCompanyMember hdMatchCompanyMember) {
        return hdMatchCompanyMemberService.updateById(hdMatchCompanyMember);
    }

    /**
     * 通过id删除bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    //@PreAuthorize("@pms.hasPermission('generator_hdmatchcompanymember_del')")
    public R removeById(@PathVariable String id) {
        return hdMatchCompanyMemberService.removeById(id);
    }

    //批量删除
    @ApiOperation("批量删除公司名匹配表")
    @RequestMapping(value = "/doBatchDel2", method = RequestMethod.POST)
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
                hdMatchCompanyMemberService.removeById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司名匹配表删除失败";
            return R.failed(500, message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }

    //导入
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file") MultipartFile file
    ) {

        List<HdMatchCompanyMember> hdMatchCompanyMembers;
        ExcelUtil excelUtil = new ExcelUtil<HdMatchCompanyMember>();
        int count = 0;
        try {
            hdMatchCompanyMembers = excelUtil.importXls(file, HdMatchCompanyMember.class);
            if (hdMatchCompanyMembers != null && hdMatchCompanyMembers.size() > 0) {
                for(HdMatchCompanyMember hdMatchCompanyMember:hdMatchCompanyMembers){
                    count++;
                    if(StringUtil.isEmpty(hdMatchCompanyMember.getCompanyMemberCode())){
                        return R.failed( "第" + count + "条成员单位编号为空");
                    }
                    if(StringUtil.isEmpty(hdMatchCompanyMember.getCustomerName())){
                        return R.failed("第" + count + "条对方单位名称为空");
                    }
                    if(StringUtil.isEmpty(hdMatchCompanyMember.getBankAccountIdExternal())){
                        return R.failed("第" + count + "条外部账号为空");
                    }
                    Map params = new HashMap();
                    //params.put("tenantId",BaseContextHandler.getTenantID());
                    params.put("code",hdMatchCompanyMember.getCompanyMemberCode());
                    List<HdCompanyMember> hdCompanyMembers = remoteBaseService.getAllMemberCompany(params);
                    if(hdCompanyMembers.size()==0){
                        return R.failed( "第" + count + "条成员单位不存在");
                    }
                    for (HdCompanyMember hdCompanyMember:hdCompanyMembers) {
                        if(hdCompanyMember.getCode().equals(hdMatchCompanyMember.getCompanyMemberCode())){
                            hdMatchCompanyMember.setCompanyMemberId(hdCompanyMember.getId());
                        }
                    }

                    Map params2 = new HashMap();
                    //params2.put("tenantId",BaseContextHandler.getTenantID());
                    params2.put("externalBankAccountId",hdMatchCompanyMember.getBankAccountIdExternal());
                    List<HdBankAccount> hdBankAccounts = remoteBaseService.selectListAllByTenant(params2);
                    if(hdBankAccounts==null||hdBankAccounts.size()==0){
                        return R.failed("第" + count + "条外部账号银行不存在");
                    }
                }
            }
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return R.failed( "请检查对方单位名称和外部账号，两者不能同时相同");
        } catch (SQLException e) {
            e.printStackTrace();
            return R.failed("数据不完整");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("导入失败");
        }
        return remoteBaseService.addMatchCompanyMemberList(hdMatchCompanyMembers);
    }


    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam() Map<String,Object> params) throws ParseException {

        //只有登录人可以导出
        List<HdMatchCompanyMember> allMatchCompanyMember = remoteBaseService.getAllMatchCompanyMember(params);
        if(allMatchCompanyMember==null){
            allMatchCompanyMember = new ArrayList<>();
            System.out.println("匹配查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdMatchCompanyMember>();
        try {
            excelUtil.print("匹配成员单位管理列表", HdMatchCompanyMember.class, "匹配成员单位管理列表", "导出时间:" + jjUtil.getDateStr(), "匹配成员单位管理列表", allMatchCompanyMember, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
