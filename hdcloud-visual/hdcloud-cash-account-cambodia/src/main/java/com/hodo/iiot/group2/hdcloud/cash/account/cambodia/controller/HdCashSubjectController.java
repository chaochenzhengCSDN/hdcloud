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

package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashRemark;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashSubject;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.HdCashSubjectService;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.jjUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
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
 * @date 2019-12-04 11:23:50
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdcashsubject" )
@Api(value = "hdcashsubject", tags = "管理")
public class HdCashSubjectController extends BaseContrller {

    private final HdCashSubjectService hdCashSubjectService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdCashSubjectPage(Map<String,Object> params) {
        return hdCashSubjectService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return hdCashSubjectService.getById(id);
    }

    /**
     * 新增
     * @param hdCashSubject 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdCashSubject hdCashSubject) {
        return hdCashSubjectService.save(hdCashSubject);
    }

    /**
     * 修改
     * @param hdCashSubject 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdCashSubject hdCashSubject) {
        return hdCashSubjectService.updateById(hdCashSubject);
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return hdCashSubjectService.removeById(id);
    }

    //导入
    @ApiOperation("导入")
    @SysLog("科目导入")
    @PostMapping(value = "/importXls")
    public R importXls(@RequestParam(value = "file",required = true)MultipartFile file){
        List<HdCashSubject> hdCashSubjectList;
        ExcelUtil excelUtil = new ExcelUtil<HdCashSubject>();
        R result = new R();
        int count = 0;
        try{
            hdCashSubjectList = excelUtil.importXls(file,HdCashSubject.class);
            if(hdCashSubjectList!=null&&hdCashSubjectList.size()>0){
                for(HdCashSubject hdCashSubject:hdCashSubjectList){
                    count++;
                    if(StringUtil.isEmpty(hdCashSubject.getSubject())){
                        return R.failed("第"+count+"条科目为空");
                    }
                }
                result = remoteBaseService.addCashSubjectList(hdCashSubjectList);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入失败");
        }
        return result;
    }

    //导出
    @ApiOperation("导出")
    @SysLog("科目导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    public void exportXls(@RequestParam()Map<String,Object> params) throws ParseException {
        List<HdCashSubject> cashSubjectList = remoteBaseService.getAllCashSubject(params);
        if(cashSubjectList==null){
            cashSubjectList = new ArrayList<>();
            System.out.println("科目查询失败！");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdCashSubject>();
        try {
            excelUtil.print("科目管理列表", HdCashSubject.class, "科目管理列表", "导出时间:" + jjUtil.getDateStr(), "科目管理列表", cashSubjectList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //批量删除
    @ApiOperation("批量删除科目")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed( "参数传递失败");
        }
        int count = 0;
        try{
            for(String id:ids.split(",")){
                count++;
                remoteBaseService.removeCashSubjectById(id);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("删除失败");
        }

        return R.ok(null,"你成功删除"+count+"条");
    }
}
