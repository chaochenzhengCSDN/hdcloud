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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashRemark;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashStatement;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashSubject;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashUnit;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.HdCashStatementService;
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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdcashstatement" )
@Api(value = "hdcashstatement", tags = "管理")
public class HdCashStatementController extends BaseContrller {

    private final HdCashStatementService hdCashStatementService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdCashStatementPage(Map<String,Object> params) {
        return hdCashStatementService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return hdCashStatementService.getById(id);
    }

    /**
     * 新增
     * @param hdCashStatement
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdCashStatement hdCashStatement) {
        //获取最大值添加
        Integer maxNo = 0;
        maxNo = remoteBillService.selectMaxNo(jjUtil.formatDate(hdCashStatement.getAccountDate()));
        if(maxNo==null){
            maxNo = 1;
        }
        hdCashStatement.setNo(new BigDecimal(maxNo));
        hdCashStatement.setAccountType("0");
        return hdCashStatementService.save(hdCashStatement);
    }

    /**
     * 修改
     * @param hdCashStatement 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdCashStatement hdCashStatement) {
        return hdCashStatementService.updateById(hdCashStatement);
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
        return hdCashStatementService.removeById(id);
    }


    //批量删除
    @ApiOperation("批量删除银行操作记录")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        List<String> idStrs = new ArrayList<>();
        int count = 0;
        try {
            for (String id:ids.split(",")){
                idStrs.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行操作记录删除失败";
            return R.failed(message);
        }
        return remoteBillService.batchDelCashStatement(idStrs);
    }

    //导出  一笔生成两笔，编号格式化

    @ApiOperation("导出")
    @SysLog("现金账单导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    public void exportXls(@RequestParam()Map<String,Object> params) throws ParseException {
        List<HdCashStatement> hdCashStatementList = hdCashStatementService.getAllCashStatement(params);
        if(hdCashStatementList==null){
            System.out.println("现金账单查询失败");
            hdCashStatementList = new ArrayList<>();
        }
        ExcelUtil excelUtil = new ExcelUtil<HdCashSubject>();
        try {
            excelUtil.print("科目管理列表", HdCashStatement.class, "科目管理列表", "导出时间:" + jjUtil.getDateStr(), "科目管理列表", hdCashStatementList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //导入
    @ApiOperation("导入")
    @SysLog("现金记账导入")
    @PostMapping(value = "/importXls")
    public R importXls(@RequestParam(value = "file",required = true)MultipartFile file){
        List<HdCashStatement> hdCashStatementList;
        ExcelUtil excelUtil = new ExcelUtil<HdCashStatement>();
        R result = new R();
        int count = 0;
        try{
            hdCashStatementList = excelUtil.importXls(file,HdCashStatement.class);
            if(hdCashStatementList!=null&&hdCashStatementList.size()>0){
                //根据时间排序
                Collections.sort(hdCashStatementList, new Comparator<HdCashStatement>() {
                    @Override
                    public int compare(HdCashStatement o1, HdCashStatement o2) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date dt1 = o1.getAccountDate();
                            Date dt2 = o2.getAccountDate();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                String tempMonth = jjUtil.getMonth(hdCashStatementList.get(0).getAccountDate());
                int maxNo = 0;
                for(HdCashStatement hdCashStatement:hdCashStatementList){
                    count++;
                    if(hdCashStatement.getAccountDate()==null){
                        R.failed("第"+count+"条账单账单时间获取失败");
                    }
                    if(StringUtil.isEmpty(hdCashStatement.getSubjects())){
                        R.failed("第"+count+"条账单科目获取失败");
                    }
                    if(StringUtil.isEmpty(hdCashStatement.getUnitName())){
                        R.failed("第"+count+"条账单单位获取失败");
                    }
                    if(StringUtil.isEmpty(hdCashStatement.getRemark())){
                        R.failed("第"+count+"条账单摘要获取失败");
                    }
                    //收入和支出不能同时为空
                    if(hdCashStatement.getIncome()==null){
                        hdCashStatement.setIncome(BigDecimal.ZERO);
                    }
                    if(hdCashStatement.getPay()==null){
                        hdCashStatement.setPay(BigDecimal.ZERO);
                    }
                    if(hdCashStatement.getIncome().compareTo(BigDecimal.ZERO)==0&&
                            hdCashStatement.getPay().compareTo(BigDecimal.ZERO)==0){
                        return R.failed("第"+count+"条账单收入或支出必须为有效值");
                    }
                    //根据单位名称查id
                    Map<String,Object> paramsName = new HashedMap<>();
                    paramsName.put("unitName",hdCashStatement.getUnitName());
                    HdCashUnit hdCashUnit = remoteBaseService.getCashUnitByName(paramsName);
                    if(hdCashUnit==null){
                        return R.failed("第"+count+"条账单单位系统无法匹配");
                    }
                    hdCashStatement.setUnitId(hdCashUnit.getId());
                    //根据时间生成编号，如果月份相同则不用根据月份继续查询最大值
                    Date accountDate = hdCashStatement.getAccountDate();
                    String month = jjUtil.getMonth(accountDate);
                    if(!month.equals(tempMonth)){
                        //根据年月查询最大值
                        maxNo = remoteBillService.selectMaxNo(jjUtil.formatDate(accountDate));
                        tempMonth = month;
                    }else{
                        maxNo++;
                    }
                    hdCashStatement.setNo(new BigDecimal(maxNo));
                    hdCashStatement.setAccountType("0");
                }
                remoteBillService.addCashStatementList(hdCashStatementList);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("账单导入失败");
        }
        return R.ok(null,"账单导入成功");
    }





}
