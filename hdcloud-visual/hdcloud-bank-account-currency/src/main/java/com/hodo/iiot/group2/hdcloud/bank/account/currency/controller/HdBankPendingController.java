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


import com.alibaba.fastjson.JSONArray;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;


import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdFenListEntity;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankPendingService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankRecordService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankpending")
@Api(value = "hdbankpending", tags = "待处理管理")
public class HdBankPendingController extends BaseContrller {
    @Autowired
    private final HdBankPendingService hdBankPendingService;

    @Autowired
    private HdBankStatementService hdBankStatementService;
    @Autowired
    private HdBankRecordService hdBankRecordService;
    /**
     * 分页查询
     * @param
     * @return
     */
    //页面展示
    //增加封裝銀行賬號
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankPendingPage(@RequestParam() Map<String,Object> params) {
        return hdBankPendingService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id
     * @return R
     */
    //查询
    //增加封裝銀行名稱和外部賬號
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return hdBankPendingService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdBankPending bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    public R save(@RequestBody HdBankPending hdBankPending) {
        return hdBankPendingService.save(hdBankPending);
    }

    /**
     * 修改bank_account
     * @param hdBankPending bank_account
     * @return R
     */
    //编辑
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    public R updateById(@RequestBody HdBankPending hdBankPending) {
        return hdBankPendingService.updateById(hdBankPending);
    }

    /**
     * 通过id删除bank_account
     * @param id id
     * @return R
     */
    //删除
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable String id) {
        return hdBankPendingService.removeById(id);
    }


    //批量删除
    @ApiOperation("批量删除银行待处理账单")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            List<String> idStrs = new ArrayList<>();
            for (String id:ids.split(",")){
                idStrs.add(id);
            }
            count = remoteBillService.batchDelBankPending(idStrs);
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行待处理账单删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }

    //划账
    @ApiOperation("划账")
    @RequestMapping(value = "/doHua", method = RequestMethod.POST)
    @ResponseBody
    public R doHua(@RequestBody Map<String,Object> params) {
        //需求更改增加处理人,同步人和处理人不一定一人
        String message = null;
        Date accountDay = null;
        List<HdBankRecord> hdBankRecordList = new ArrayList<>();
        if (params.get("memberCompanyId")==null) {
            return R.failed("参数传递失败");
        }
//        if(StringUtil.isEmpty(remark)){
//            return new ObjectRestResponse<>(1003,"参数传递失败");
//        }
        if (params.get("ids")==null) {
            return R.failed("参数传递失败");
        }
        //公司是否存在前端已做控制
        try {
            String ids = params.get("ids").toString();
            String memberCompany = params.get("memberCompanyId").toString();
            for (String id : ids.split(",")) {
                HdBankStatement hdBankStatement = new HdBankStatement();
                HdBankPending pending = hdBankPendingService.getById(id).getData();
                MyBeanUtils.copyBeanNotNull2Bean(pending,hdBankStatement);
                if(params.get("accountDate")!=null){
                    accountDay = DateUtils.date_sdf.parse(params.get("accountDate").toString());
                    hdBankStatement.setAccountDate(accountDay);
                }
                hdBankStatement.setId(null);
                hdBankPendingService.addMemberCom(memberCompany,hdBankStatement);
                //bank.setCompanyName(companyName);
                hdBankStatement.setSourceType("2");
                if (params.get("remark")!=null) {
                    hdBankStatement.setRemark(params.get("remark").toString());
                }
                //银行账单录入
                hdBankStatementService.insertEntity(hdBankStatement);
                //待处理账单删除
                hdBankPendingService.removeById(id);
                //批量插入记录表
                hdBankRecordList.add(hdBankRecordService.getHdBankRecord(hdBankStatement, "划账"));
            }
            remoteBillService.addBankRecordList(hdBankRecordList);
            message = "划账成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "划账失败";
            return R.failed(message);
        }

        return R.ok(message);
    }
    //分账
    @ApiOperation("新分账")
    @RequestMapping(value = "/doNewFen", method = RequestMethod.POST)
    @ResponseBody
    public R doNewFen(@RequestBody Map<String,Object> params) {
        String message = null;
        Date accountDay = null;
        List<HdBankRecord> hdBankRecords = new ArrayList<>();
        if (params.get("id")==null) {
            return R.failed("传入参数失败");
        }
        if (params.get("hdFenStr")==null) {
            return R.failed("传入参数失败");
        }
        String id = params.get("id").toString();
        String hdFenStr = params.get("hdFenStr").toString();
        //List<Object> strList = JSONArray.parseArray(str);
        List<HdFenListEntity> hdFenList = new ArrayList<>();
        hdFenList = JSONArray.parseArray(hdFenStr, HdFenListEntity.class);
        try{
            HdBankPending hdBankPending = hdBankPendingService.getById(id).getData();
            if (hdBankPending == null) {
                return R.failed("数据查询异常");
            }
            for (HdFenListEntity hdFen : hdFenList) {
                if (StringUtil.isEmpty(hdFen.getCompanyId())) {
                    return R.failed("分账有公司名为空");
                }
                if(StringUtil.isNotEmpty(hdFen.getMoney())){
                    hdFen.setMoney(new DecimalFormat().parse(hdFen.getMoney()).toString());
                }else{
                    hdFen.setMoney("0.00");
                }
                HdBankStatement hdBankStatement  = new HdBankStatement();
                MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, hdBankStatement);
                if(params.get("accountDate")!=null){
                    accountDay = DateUtils.date_sdf.parse(params.get("accountDate").toString());
                    hdBankStatement.setAccountDate(accountDay);
                }
                hdBankStatement.setId(null);
                hdBankPendingService.addMemberCom(hdFen.getMemberCompany(),hdBankStatement);
                hdBankStatement.setSourceType("1");
                BigDecimal bPay = hdBankPending.getPay() != null ? hdBankPending.getPay() : BigDecimal.ZERO;
                if (bPay.compareTo(BigDecimal.ZERO) == 0) {
                    hdBankStatement.setIncome(hdFen.getMoney()!=null?new BigDecimal(hdFen.getMoney()):BigDecimal.ZERO);
                }else{
                    hdBankStatement.setPay(hdFen.getMoney()!=null?new BigDecimal(hdFen.getMoney()):BigDecimal.ZERO);
                }
                hdBankStatementService.insertEntity(hdBankStatement);
                hdBankRecords.add(hdBankRecordService.getHdBankRecord(hdBankStatement, "分账"));
            }
            //删除原分账记录
            hdBankPendingService.removeById(id);
            //批量记录操作信息
            remoteBillService.addBankRecordList(hdBankRecords);
        }catch (ParseException e) {
            e.printStackTrace();
            return R.failed("同步至某日日期格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            message = "分账失败";
            return R.failed(message);
        }
        return R.ok( message);
    }

    //导出
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(String userName) throws ParseException {
        //companyName为中文名
        Map<String, Object> params = new HashedMap();
        String subjects = request.getParameter("subjects");
        String remark = request.getParameter("remark");
        String bankName = request.getParameter("account_id");
        String mySubjects = request.getParameter("mySubjects");

        if (StringUtil.isNotEmpty(subjects)) {
            params.put("subjects", subjects);
        }
        if (StringUtil.isNotEmpty(remark)) {
            params.put("remark", remark);
        }
        if (StringUtil.isNotEmpty(bankName)) {
            params.put("bankName", bankName);
        }
        if (StringUtil.isNotEmpty(mySubjects)) {
            params.put("mySubjects", mySubjects);
        }
        String startTime = request.getParameter("dateStart");
        String endTime = request.getParameter("dateEnd");
        //2.1版本新增金额区间查询
        String moneyStart = request.getParameter("moneyStart");
        String moneyEnd = request.getParameter("moneyEnd");
        if(StringUtil.isEmpty(moneyStart)){
            moneyStart=null;
        }
        if(StringUtil.isEmpty(moneyEnd)){
            moneyEnd=null;
        }
        params.put("moneyStart",moneyStart);
        params.put("moneyEnd",moneyEnd);
        params.put("dateStart",startTime);
        params.put("dateEnd",endTime);
        List<HdBankPending> allBankPendingList = remoteBillService.getAllBankPending(params);
        //根据银行账户添加附加信息
        //allBankPendingList = hdBankPendingService.addBankAccount(allBankPendingList);
        if(allBankPendingList==null){
            allBankPendingList = new ArrayList<>();
            System.out.println("待处理查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdBankPending>();
        try {
            excelUtil.print("银行待处理记录列表", HdBankPending.class, "银行待处理记录列表", "导出时间:" + jjUtil.getDateStr(), "银行待处理记录列表", allBankPendingList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查看总收入和总支出前端做不需要


}
