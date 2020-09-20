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


import com.hodo.hdcloud.common.security.annotation.Inner;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdCompanyService;
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
@RequestMapping("/hdcompany")
@Api(value = "hdcompany", tags = "公司管理")
public class HdCompanyController extends BaseContrller {

    private final HdCompanyService hdCompanyService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdCompanyPage(@RequestParam() Map<String,Object> params) {
        return hdCompanyService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return hdCompanyService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdCompany bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('generator_hdcompany_add')")
    public R save(@RequestBody HdCompany hdCompany) {
        return hdCompanyService.save(hdCompany);
    }

    /**
     * 修改bank_account
     * @param hdCompany bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    public R updateById(@RequestBody HdCompany hdCompany) {

        return hdCompanyService.updateById(hdCompany);
    }

    /**
     * 通过id删除bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    //@PreAuthorize("@pms.hasPermission('generator_hdcompany_del')")
    public R removeById(@PathVariable String id) {

        return hdCompanyService.removeById(id);
    }

    //获取所有公司名称




    //批量删除
    @ApiOperation("批量删除银行操作记录")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed( "参数传递失败");
        }
        String message = null;
        List<String> idStrs = new ArrayList<>();
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                R result = hdCompanyService.removeById(id);

                if(result.getCode()==1){
                    return hdCompanyService.removeById(id);
                }
                idStrs.add(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司删除失败";
            return R.failed( message);
        }

        return remoteBaseService.removeCompanyByIds(idStrs);

    }

    //导入
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        List<HdCompany> companies;
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        R result = new R();
        int count = 0;
        try {
            companies = excelUtil.importXls(file, HdCompany.class);
            if (companies != null && companies.size() > 0) {
                for(HdCompany hdCompany:companies){
                    count++;
                    if(StringUtil.isEmpty(hdCompany.getCode())){
                        return R.failed("第" + count + "条公司编号为空");
                    }
                }
                result = remoteBaseService.addCompanyList(companies);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed( "导入失败");
        }
        //检查是否有同名，是否无名称
        return result;
    }
    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    public void exportXls(String userName,
                          String name,String code) throws ParseException {
        //只有登录人可以导出

        Map<String,Object> params = new HashedMap();
        if(StringUtil.isNotEmpty(name)){
            params.put("name",name);
        }
        if(StringUtil.isNotEmpty(code)){
            params.put("code",code);
        }
        List<HdCompany> allCompanyEx = remoteBaseService.getAllCompany(params);
        if(allCompanyEx==null){
            allCompanyEx = new ArrayList<>();
            System.out.println("公司查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        try {
            excelUtil.print("银行公司管理列表", HdCompany.class, "银行公司管理列表", "导出时间:" + jjUtil.getDateStr(), "公司管理列表", allCompanyEx, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }













}
