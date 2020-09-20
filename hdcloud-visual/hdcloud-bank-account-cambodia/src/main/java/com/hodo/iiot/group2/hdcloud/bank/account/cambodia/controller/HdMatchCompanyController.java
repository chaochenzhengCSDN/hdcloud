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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdMatchCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdMatchCompanyService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.jjUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
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
@RequestMapping("/hdmatchcompany" )
@Api(value = "hdmatchcompany", tags = "管理")
public class HdMatchCompanyController extends BaseContrller {

    private final HdMatchCompanyService hdMatchCompanyService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdMatchCompanyPage(Map<String,Object> params) {
        return hdMatchCompanyService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return hdMatchCompanyService.getById(id);
    }

    /**
     * 新增
     * @param hdMatchCompany 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdMatchCompany hdMatchCompany) {
        return hdMatchCompanyService.save(hdMatchCompany);
    }

    /**
     * 修改
     * @param hdMatchCompany 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdMatchCompany hdMatchCompany) {
        return hdMatchCompanyService.updateById(hdMatchCompany);
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
        return hdMatchCompanyService.removeById(id);
    }

    //批量删除
    @ApiOperation("批量删除公司名匹配表")
    @RequestMapping(value = "/doBatchDel2", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody() String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        int count = 0;
        try{
            for(String id:ids.split(",")){
                hdMatchCompanyService.removeById(id);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("公司匹配删除失败");
        }
        return R.ok(null,"您成功删除"+count+"条");
    }
    //导入
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file") MultipartFile file
    ) {
        List<HdMatchCompany> hdMatchCompanyList = new ArrayList<>();
        ExcelUtil excelUtil = new ExcelUtil();
        int count = 0;
        try{
            hdMatchCompanyList = excelUtil.importXls(file,HdMatchCompany.class);
            if(hdMatchCompanyList!=null&&hdMatchCompanyList.size()>0){
                for(HdMatchCompany hdMatchCompany:hdMatchCompanyList){
                    count++;
                    if(StringUtil.isEmpty(hdMatchCompany.getCompanyCode())){
                        return R.failed( "第" + count + "条公司编号为空");
                    }
                    if(StringUtil.isEmpty(hdMatchCompany.getCustomerName())){
                        return R.failed("第" + count + "条对方单位名称为空");
                    }
                    Map params = new HashedMap();
                    params.put("code",hdMatchCompany.getCompanyCode());
                    HdCompany hdCompany = remoteBaseService.getCompanyByCode(params);
                    if(hdCompany==null){
                        return R.failed( "第" + count + "条公司不存在");
                    }
                    hdMatchCompany.setCompanyId(hdCompany.getId());
                    hdMatchCompany.setCompanyName(hdCompany.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return R.failed("数据不完整");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("导入失败");
        }
        return remoteBaseService.addMatchCompanyList(hdMatchCompanyList);
    }
    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam() Map<String,Object> params) throws ParseException {

        //只有登录人可以导出
        List<HdMatchCompany> allMatchCompany = remoteBaseService.getAllMatchCompany(params);
        if(allMatchCompany==null){
            allMatchCompany = new ArrayList<>();
            System.out.println("匹配查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdMatchCompany>();
        try {
            excelUtil.print("匹配成员单位管理列表", HdMatchCompany.class, "匹配成员单位管理列表", "导出时间:" + jjUtil.getDateStr(), "匹配成员单位管理列表", allMatchCompany, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
