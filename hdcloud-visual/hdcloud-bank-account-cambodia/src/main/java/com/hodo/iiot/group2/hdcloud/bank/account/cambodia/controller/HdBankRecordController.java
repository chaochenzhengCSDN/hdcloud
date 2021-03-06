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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankRecordService;
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
@RequestMapping("/hdbankrecord" )
@Api(value = "hdbankrecord", tags = "管理")
public class HdBankRecordController extends BaseContrller {

    private final HdBankRecordService hdBankRecordService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdBankRecordPage(Map<String,Object> params) {

        return hdBankRecordService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return hdBankRecordService.getById(id);
    }

    /**
     * 新增
     * @param hdBankRecord 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdBankRecord hdBankRecord) {
        return hdBankRecordService.save(hdBankRecord);
    }

    /**
     * 修改
     * @param hdBankRecord 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdBankRecord hdBankRecord) {
        return hdBankRecordService.updateById(hdBankRecord);
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
        return hdBankRecordService.removeById(id);
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
        return remoteBillService.batchDelBankRecord(idStrs);
    }

    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(String userName) throws ParseException {
        //日期，摘要，公司，金额
        String startTime = request.getParameter("dateStart");
        String endTime = request.getParameter("dateEnd");
        String member_company = request.getParameter("company_member_id");
        String company = request.getParameter("company_id");
        String money = request.getParameter("money");
        String remark = request.getParameter("remark");
        //2.1版本新增金额区间查询
        String moneyStart = request.getParameter("moneyStart");
        String moneyEnd = request.getParameter("moneyEnd");
        //序号，银行,操作类型
        String num = request.getParameter("num");
        String accountId = request.getParameter("bank_account_id");
        String opertype = request.getParameter("operType");

        Map<String, Object> params = new HashedMap();
        if (StringUtil.isNotEmpty(money)) {
            params.put("money", money);
        }
        if (StringUtil.isNotEmpty(remark)) {
            params.put("remark", remark);
        }
        if(StringUtil.isNotEmpty(opertype)){
            params.put("operType",opertype);
        }
        params.put("moneyStart",moneyStart);
        params.put("moneyEnd",moneyEnd);
        params.put("company_member_id",member_company);
        params.put("company_id",company);
        params.put("dateStart",startTime);
        params.put("dateEnd",endTime);
        params.put("num",num);
        params.put("bank_account_id",accountId);
        List<HdBankRecord> allBankRecordList = remoteBillService.getAllBankRecord(params);
        //公司名称和收支方向
        if (allBankRecordList != null && allBankRecordList.size() > 0) {
            for (HdBankRecord hdBankRecord : allBankRecordList) {
                String formatNum = jjUtil.formatNo(hdBankRecord.getNumber());
                hdBankRecord.setBy1(formatNum);
                if (hdBankRecord.getFlag() != null) {
                    if ("0".equals(hdBankRecord.getFlag())) {
                        hdBankRecord.setFlag("收入");
                    } else {
                        hdBankRecord.setFlag("支出");
                    }
                }
                /**
                 * 改成成员单位获取数据
                 */
                if(hdBankRecord.getCompanyId()!=null){
                    //根据id获取成员单位实体类
                    HdCompany hdCompany = remoteBaseService.getCompanyById(hdBankRecord.getCompanyId()).getData();
                    if(hdCompany!=null){
                        hdBankRecord.setCompanyName(hdCompany.getName());
                    }
                }
            }
            if(allBankRecordList==null){
                allBankRecordList = new ArrayList<>();
                System.out.println("记录查询失败");
            }
            ExcelUtil excelUtil = new ExcelUtil<HdBankRecord>();
            try {
                excelUtil.print("银行处理记录列表", HdBankRecord.class, "银行处理记录列表", "导出时间:" + jjUtil.getDateStr(), "银行处理记录列表", allBankRecordList, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }








}
