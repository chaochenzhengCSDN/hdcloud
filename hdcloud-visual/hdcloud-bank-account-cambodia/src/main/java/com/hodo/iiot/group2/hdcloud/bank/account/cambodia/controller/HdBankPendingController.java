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

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;

import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankPendingService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankRecordService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;


/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankpending" )
@Api(value = "hdbankpending", tags = "管理")
public class HdBankPendingController extends BaseContrller {

    private final HdBankPendingService hdBankPendingService;

    private HdBankStatementService hdBankStatementService;
    private HdBankRecordService hdBankRecordService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdBankPendingPage(Map<String,Object> params) {
        return hdBankPendingService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return hdBankPendingService.getById(id);
    }

    /**
     * 新增
     * @param hdBankPending 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('data-hdcloud-bank-account-base_hdbankpending_add')" )
    public R save(@RequestBody HdBankPending hdBankPending) {
        return hdBankPendingService.save(hdBankPending);
    }

    /**
     * 修改
     * @param hdBankPending 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('data-hdcloud-bank-account-base_hdbankpending_edit')" )
    public R updateById(@RequestBody HdBankPending hdBankPending) {
        return hdBankPendingService.updateById(hdBankPending);
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('data-hdcloud-bank-account-base_hdbankpending_del')" )
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
        if (params.get("companyId")==null) {
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
            String companyId = params.get("companyId").toString();
            for (String id : ids.split(",")) {
                HdBankStatement hdBankStatement = new HdBankStatement();
                HdBankPending pending = hdBankPendingService.getById(id).getData();
                MyBeanUtils.copyBeanNotNull2Bean(pending,hdBankStatement);
                if(params.get("accountDate")!=null){
                    accountDay = DateUtils.date_sdf.parse(params.get("accountDate").toString());
                    hdBankStatement.setAccountDate(accountDay);
                }
                hdBankStatement.setId(null);
                hdBankStatement.setCompanyId(companyId);
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
    @ApiOperation("分账")
    @RequestMapping(value = "/doFen", method = RequestMethod.POST)
    @ResponseBody
    public R doFen(String id,String accountDate, String hdFenStr) {
        //需求更改增加处理人,同步人和处理人不一定一人
        String message = null;
        Date accountDay = null;
        List<HdBankRecord> hdBankRecords = new ArrayList<>();
        if (StringUtil.isEmpty(id)) {
            return R.failed("传入参数失败");
        }
        if (StringUtil.isEmpty(hdFenStr)) {
            return R.failed("传入参数失败");
        }
        //List<Object> strList = JSONArray.parseArray(str);
        List<HdFenListEntity> hdFenList = new ArrayList<>();
        hdFenList = JSONArray.parseArray(hdFenStr, HdFenListEntity.class);
        try{
            //检查公司是否为空
            for (HdFenListEntity hdFen : hdFenList) {
                if (StringUtil.isEmpty(hdFen.getCompanyId())) {
                    return R.failed("分账有公司名为空");
                }
                if(StringUtil.isNotEmpty(hdFen.getMoney())){
                    hdFen.setMoney(new DecimalFormat().parse(hdFen.getMoney()).toString());
                }else{
                    hdFen.setMoney("0.00");
                }
            }
            //先给金额排个序
            Collections.sort(hdFenList, new Comparator<HdFenListEntity>() {
                @Override
                public int compare(HdFenListEntity o1, HdFenListEntity o2) {
                    int a = new BigDecimal(o1.getMoney()).compareTo(new BigDecimal(o2.getMoney()));
                    if (a == 1) {
                        return 1;
                    } else if (a == -1) {
                        return -1;
                    } else {
                        return 0;
                    }
                }

            });
            HdBankPending hdBankPending = remoteBillService.getBankPendingById(id).getData();
            if (hdBankPending == null) {
                return R.failed("数据查询异常");
            }
            //先录入一笔最大的金额进公司
            HdFenListEntity maxFenListEntity = hdFenList.get(hdFenList.size() - 1);
            HdBankStatement maxBankStatement = new HdBankStatement();
            MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, maxBankStatement);
            maxBankStatement.setCompanyId(maxFenListEntity.getCompanyId());
            maxBankStatement.setId(null);
            if(StringUtil.isNotEmpty(accountDate)){
                accountDay = DateUtils.date_sdf.parse(accountDate);
                maxBankStatement.setAccountDate(accountDay);
            }
            maxBankStatement.setAccountType(maxBankStatement.getRid()!=null?"0":"1");
            hdBankStatementService.insertEntity(maxBankStatement);
            hdBankRecords.add(hdBankRecordService.getHdBankRecord(maxBankStatement, "分账"));
            //最后一笔相反操作
            hdFenList.remove(hdFenList.size() - 1);
            //记录操作信息
            //先判断是为支出还是收入
            BigDecimal bPay = hdBankPending.getPay() != null ? hdBankPending.getPay() : BigDecimal.ZERO;
            if (bPay.compareTo(BigDecimal.ZERO) == 0) {
                //支出为0即为收入
                for (HdFenListEntity e : hdFenList) {
                    HdBankStatement bankIncome = new HdBankStatement();
                    HdBankStatement bankPay = new HdBankStatement();
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankIncome);
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankPay);
                    bankIncome.setCompanyId(e.getCompanyId());
                    bankIncome.setIncome(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    //bankIncome.setRid(hdBankPending.getRid());
                    bankIncome.setId(null);
                    bankIncome.setSourceType("1");
                    if(StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankIncome.setAccountDate(accountDay);
                    }
                    bankIncome.setAccountType(bankIncome.getRid()!=null?"0":"1");
                    hdBankStatementService.insertEntity(bankIncome);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordService.getHdBankRecord(bankIncome, "分账"));
                    bankPay.setCompanyId(maxFenListEntity.getCompanyId());
                    bankPay.setIncome(new BigDecimal("0.00"));
                    bankPay.setPay(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    //bankPay.setRid(hdBankPending.getRid());
                    bankPay.setId(null);
                    bankPay.setSourceType("1");
                    if(StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankPay.setAccountDate(accountDay);
                    }
                    bankPay.setAccountType(bankPay.getRid()!=null?"0":"1");
                    hdBankStatementService.insertEntity(bankPay);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordService.getHdBankRecord(bankPay, "分账"));
                }
            } else {
                //支出不为0即为支出
                for (HdFenListEntity e : hdFenList) {
                    HdBankStatement bankIncome = new HdBankStatement();
                    HdBankStatement bankPay = new HdBankStatement();
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankIncome);
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankPay);
                    bankIncome.setCompanyId(e.getCompanyId());
                    bankIncome.setPay(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    //bankIncome.setRid(hdBankPending.getRid());
                    bankIncome.setId(null);
                    bankIncome.setSourceType("1");
                    if(StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankIncome.setAccountDate(accountDay);
                    }
                    bankIncome.setAccountType(bankIncome.getRid()!=null?"0":"1");
                    hdBankStatementService.insertEntity(bankIncome);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordService.getHdBankRecord(bankIncome, "分账"));
                    bankPay.setCompanyId(maxFenListEntity.getCompanyId());
                    bankPay.setPay(new BigDecimal("0.00"));
                    //bankPay.setRid(hdBankPending.getRid());
                    bankPay.setIncome(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    bankPay.setId(null);
                    bankPay.setSourceType("1");
                    if(StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankPay.setAccountDate(accountDay);
                    }
                    bankPay.setAccountType(bankPay.getRid()!=null?"0":"1");
                    hdBankStatementService.insertEntity(bankPay);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordService.getHdBankRecord(bankPay, "分账"));
                }
            }
            //删除原分账记录
            remoteBillService.removeBankPendingById(id);
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

        return R.ok(null, message);
    }
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
                hdBankStatement.setCompanyId(hdFen.getCompanyId());
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










}
