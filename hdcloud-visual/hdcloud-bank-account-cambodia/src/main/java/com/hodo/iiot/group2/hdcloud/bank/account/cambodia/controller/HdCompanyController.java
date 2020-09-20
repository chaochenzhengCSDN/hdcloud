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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdCompanyService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/hdcompany" )
@Api(value = "hdcompany", tags = "管理")
public class HdCompanyController extends BaseContrller {

    private final HdCompanyService hdCompanyService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdCompanyPage(Map<String,Object> params) {
        return hdCompanyService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return hdCompanyService.getById(id);
    }

    /**
     * 新增
     * @param hdCompany 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdCompany hdCompany) {
        return hdCompanyService.save(hdCompany);
    }

    /**
     * 修改
     * @param hdCompany 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdCompany hdCompany) {
        return hdCompanyService.updateById(hdCompany);
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
            R.failed("某公司下有账单信息,删除会导致统计错误！");
        }
        return R.ok(null,"成功删除");
    }

    //批量删除
    @ApiOperation("批量删除公司")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed( "参数传递失败");
        }
        List<String> idStrs = new ArrayList<>();
        int count = 0;
        try{
            for(String id:ids.split(",")){
                Boolean result = removeTerm(id);
                if(result){
                    R.failed("某公司下有账单信息,删除会导致统计错误！");
                }
                idStrs.add(id);
                count++;
            }
            remoteBaseService.removeCompanyByIds(idStrs);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("公司删除失败");
        }
        return R.ok(null,"你成功删除"+count+"条");
    }

    //删除判断处理
    private Boolean removeTerm(String id){
        Map<String,Object> params = new HashedMap();
        params.put("company_id",id);
        List<HdBankStatement> hdBankStatements = remoteBillService.getAllBankStatement(params);
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
        List<HdCompany> companies= new ArrayList<>();
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        int count = 0;
        R result = new R();
        try{
            companies = excelUtil.importXls(file,HdCompany.class);
            if(companies!=null&&companies.size()>0){
                for(HdCompany hdCompany:companies){
                    count++;
                    if(StringUtil.isEmpty(hdCompany.getCode())){
                        return R.failed("第" + count + "条公司编号为空");
                    }
                }
                result = remoteBaseService.addCompanyList(companies);
            }
        }catch (DuplicateKeyException e){
            return R.failed( "导入重复编码！");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed( "导入失败");
        }
        return result;
    }

    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    public void exportXls(String userName,
                          String name,String code) throws ParseException {
        Map<String,Object> params = new HashedMap();
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        if(StringUtil.isNotEmpty(name)){
            params.put("name",name);
        }
        if(StringUtil.isNotEmpty(code)){
            params.put("code",code);
        }
        List<HdCompany> hdCompanies = remoteBaseService.getAllCompany(params);
        if(hdCompanies==null){
            hdCompanies = new ArrayList<>();
            System.out.println("公司查询失败");
        }
        try {
            excelUtil.print("银行公司管理列表", HdCompany.class, "银行公司管理列表", "导出时间:" + jjUtil.getDateStr(), "公司管理列表", hdCompanies, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
