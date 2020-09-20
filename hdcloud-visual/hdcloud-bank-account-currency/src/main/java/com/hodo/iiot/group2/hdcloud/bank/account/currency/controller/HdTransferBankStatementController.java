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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;

import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdTransferBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping("/hdtransferbankstatement")
@Api(value = "hdtransferbankstatement", tags = "内转账单管理")
public class HdTransferBankStatementController extends BaseContrller {
    @Autowired
    private final HdTransferBankStatementService hdTransferBankStatementService;

    /**
     * 分页查询
     * @param
     * @return
     */
    //页面展示
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdTransferBankStatementPage(@RequestParam() Map<String,Object> params) {
        return hdTransferBankStatementService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id
     * @return R
     */
    //查询
    //增加封裝名稱在裡面
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return hdTransferBankStatementService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdTransferBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    public R save(@RequestBody HdTransferBankStatement hdTransferBankStatement) {
        String id = UUIDUtils.generateUuid();
        hdTransferBankStatement.setId(id);
        //自身增加同时增加账单
        return hdTransferBankStatementService.save(hdTransferBankStatement);
    }

    /**
     * 修改bank_account
     * @param hdTransferBankStatement bank_account
     * @return R
     */
    //修改
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    public R updateById(@RequestBody HdTransferBankStatement hdTransferBankStatement) {
        return hdTransferBankStatementService.updateById(hdTransferBankStatement);
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
        return hdTransferBankStatementService.removeById(id);
    }

    //批量删除
    @RequestMapping(value = "doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("批量删除对象*")
    public R doBatchDel(String ids){
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try{
            for(String id:ids.split(",")){
                hdTransferBankStatementService.removeById(id);
                count++;
            }
            //baseAccountFeign.batchDelNzBankAccount(ids);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("删除失败");
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }

    //冲账
    @ApiOperation("内转账单冲账*")
    @RequestMapping(value = "/doBatchNzCZ", method = RequestMethod.GET)
    @ResponseBody
    public R doBatchCZ(String ids) {
        String message = null;
        message = "内转账单冲账";
        int count = 0;
        try{
            for (String id : ids.split(",")) {
                HdTransferBankStatement hdTransferBankStatement = hdTransferBankStatementService.getById(id).getData();
                HdTransferBankStatement t = new HdTransferBankStatement();
                MyBeanUtils.copyBeanNotNull2Bean(hdTransferBankStatement, t);
                t.setId(UUIDUtils.generateUuid());
                t.setMoney(hdTransferBankStatement.getMoney().multiply(new BigDecimal(-1)));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                t.setCreateTime(sdf.parse(sdf.format(new Date())));
                t.setRemark("用于" + sdf.format(hdTransferBankStatement.getCreateTime()) + ",凭证编号为：" + hdTransferBankStatement.getNo() + "的账务冲账！");
                hdTransferBankStatementService.save(t);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
            message = "内转账单冲账失败";
            return R.failed(message);
        }
        message = "您成功冲账" + count + "条记录！";
        return R.ok(message);
    }
    //导入
    @ApiOperation("导入*")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        List<HdTransferBankStatement> hdTransferBankStatements;
        ExcelUtil excelUtil = new ExcelUtil<HdTransferBankStatement>();
        HdCompanyMember subject;
        try {
            hdTransferBankStatements = excelUtil.importXls(file,HdTransferBankStatement.class);
            int count = 0;
            if(hdTransferBankStatements!=null&&hdTransferBankStatements.size()>0){
                for(HdTransferBankStatement hdNzbankAccount:hdTransferBankStatements){
                    count++;
//                        if(StringUtil.isEmpty(hdNzbankAccount.getRemark())){
//                            return new ObjectRestResponse<>(1004, "第" + count + "条数据摘要为空！");
//                        }
                    //原公司匹配改成成员单位匹配
                    if(StringUtil.isEmpty(hdNzbankAccount.getMemberIncomeSubjectCode())){
                        return R.failed("第" + count + "条数据收入成员单位为空");
                    }
                    subject = hdTransferBankStatementService.getSubjectEntity(hdNzbankAccount.getMemberIncomeSubjectCode());
                    if(subject==null){
                        return R.failed("第" + count + "条数据收入成员单位编码系统无法匹配！");
                    }
                    //成员单位
                    hdNzbankAccount.setMemberIncomeSubjectId(subject.getId());
                    //父级公司
                    hdNzbankAccount.setIncomeSubjectId(subject.getCompanyId());
                    if(StringUtil.isEmpty(hdNzbankAccount.getMemberPaySubjectCode())){
                        return R.failed("第" + count + "条数据支出成员单位为空！");
                    }
                    subject = hdTransferBankStatementService.getSubjectEntity(hdNzbankAccount.getMemberPaySubjectCode());
                    if(subject==null){
                        return R.failed("第" + count + "条数据支出成员单位编码系统无法匹配！");
                    }
                    hdNzbankAccount.setMemberPaySubjectId(subject.getId());
                    hdNzbankAccount.setPaySubjectId(subject.getCompanyId());
                    //开户行匹配
                    if(StringUtil.isEmpty(hdNzbankAccount.getBankAccountCode())){
                        return R.failed("第" + count + "条数据缺少开户行！");
                    }
                    HdBankAccount hdBankAccount = remoteBaseService.getAccountByTerm("",hdNzbankAccount.getBankAccountCode());
                    if(hdBankAccount==null){
                        return R.failed("第" + count + "条数据开户行系统无法匹配！");
                    }
                    hdNzbankAccount.setBankAccountId(hdBankAccount.getId());
                    //插入时添加no
                    hdNzbankAccount.setId(UUIDUtils.generateUuid());
                    //hdNzbankAccount.setCreateTime(new Date());
                }
                hdTransferBankStatementService.batchSave(hdTransferBankStatements);
            }
            //公司和开户行抬头系统内有
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入出错,请检查数字单元格是否为数值格式");
        }
        return R.ok("导入成功");
    }
    //导出
    @ApiOperation("导出*")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam Map<String,Object> params) throws ParseException {
        String userName = jjUtil.handleParams(params,"userName");
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String incomesubjectName = jjUtil.handleParams(params,"incomesubjectName");
        String paysubjectName = jjUtil.handleParams(params,"paysubjectName");
        Map<String,Object> requestParam = new HashedMap();
        List<HdTransferBankStatement> allNzbankAccountList = new ArrayList<>();
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
        params.put("incomesubject",incomesubjectName);
        params.put("dateStart",startTime);
        params.put("dateEnd",endTime);
        params.put("paysubject",paysubjectName);

        allNzbankAccountList = remoteBillService.getAllNzbankAccountList(params);
        hdTransferBankStatementService.formatAllNo(allNzbankAccountList);
        if(allNzbankAccountList==null){
            allNzbankAccountList = new ArrayList<>();
            System.out.println("内转账单查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdTransferBankStatement>();
        try {
            excelUtil.print("内转账单记录列表", HdTransferBankStatement.class, "内转账单记录列表", "导出时间:" + jjUtil.getDateStr(), "内转账单记录列表", allNzbankAccountList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取打印数据
    @ApiOperation(value = "获取打印列表*")
    @RequestMapping(value = "/getPrintList", method = RequestMethod.GET)
    @ResponseBody
    public R<List<HdTransferBankStatementDY>> getPrintList(String ids){
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        List<HdTransferBankStatementDY> hdNzbankAccountDYList = new ArrayList<>();
        HdCompanyMember hdCompanyMember;
        try{
            for(String id:ids.split(",")){
                HdTransferBankStatement hdTransferBankStatement = hdTransferBankStatementService.getById(id).getData();
                hdTransferBankStatementService.formatNo(hdTransferBankStatement,6);
                HdTransferBankStatementDY hdTransferBankStatementDY = new HdTransferBankStatementDY();
                try {
                    MyBeanUtils.copyBeanNotNull2Bean(hdTransferBankStatement, hdTransferBankStatementDY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(hdTransferBankStatement!=null){
                    //根据id获取名称
                    if(StringUtil.isNotEmpty(hdTransferBankStatement.getMemberIncomeSubjectId())){
                        hdCompanyMember = remoteBaseService.getMemberCompanyById(hdTransferBankStatement.getMemberIncomeSubjectId()).getData();
                        if(hdCompanyMember!=null){
                            hdTransferBankStatementDY.setMemberIncomeSubjectName(hdCompanyMember.getName());
                            hdTransferBankStatementDY.setMemberIncomeSubjectCode(hdCompanyMember.getCode());
                            hdTransferBankStatementDY.setIncomeSubjectName(hdCompanyMember.getCompanyName());
                            hdTransferBankStatementDY.setIncomeSubjectCode(hdCompanyMember.getCompanyCode());
                        }
                    }
                    if(StringUtil.isNotEmpty(hdTransferBankStatement.getMemberPaySubjectId())){
                        hdCompanyMember = remoteBaseService.getMemberCompanyById(hdTransferBankStatement.getMemberPaySubjectId()).getData();
                        if(hdCompanyMember!=null){
                            hdTransferBankStatementDY.setMemberPaySubjectName(hdCompanyMember.getName());
                            hdTransferBankStatementDY.setMemberPaySubjectCode(hdCompanyMember.getCode());
                            hdTransferBankStatementDY.setPaySubjectName(hdCompanyMember.getCompanyName());
                            hdTransferBankStatementDY.setPaySubjectCode(hdCompanyMember.getCompanyCode());
                        }
                    }
                    //补充添加wb账户和银行编码
                    if(StringUtil.isNotEmpty(hdTransferBankStatementDY.getBankAccountId())){
                        HdBankAccount hdBankAccount = remoteBaseService.getBankAccountById(hdTransferBankStatementDY.getBankAccountId()).getData();
                        if(hdBankAccount!=null){
                            hdTransferBankStatementDY.setBankAccountCode(hdBankAccount.getExternalBankAccountId());
                            hdTransferBankStatementDY.setBankName(hdBankAccount.getBankName());
                        }
                    }

                    if(hdTransferBankStatementDY.getMoney()!=null){
                        String money = hdTransferBankStatementDY.getMoney().toString();
                        List<Character> c = new ArrayList<>();
                        c.add('￥');
                        for(int i=0;i<money.length();i++){
                            c.add(money.charAt(i));
                            System.out.println(money.charAt(i));
                        }
                        hdTransferBankStatementDY.setMoneyChar(c);
                    }
                    //从redis拿
//                    if(StringUtil.isNotEmpty(hdTransferBankStatementDY.getCrtUserId())){
//                        String userId = hdTransferBankStatementDY.getCrtUserId();
//                        User user = iUserFeign.getById(userId);
//                        if(user!=null){
//                            hdNzbankAccountDY.setCrtUserId(user.getName());
//                        }else{
//                            hdNzbankAccountDY.setCrtUserId(BaseContextHandler.getName());
//                        }
//                    }else{
//                        hdNzbankAccountDY.setCrtUserId(BaseContextHandler.getName());
//                    }
                    //获取租户名称
                    Map<String,Object> params = new HashedMap();
                    params.put("tenant_id",hdTransferBankStatementDY.getTenantId());
                    HdBankTenant hdBankTenant = remoteBaseService.getTenantByTerm(params)!=null?
                            remoteBaseService.getTenantByTerm(params):null;
                    if(hdBankTenant!=null){
                        hdTransferBankStatementDY.setHeadName(hdBankTenant.getTenantName());
                    }
                    hdNzbankAccountDYList.add(hdTransferBankStatementDY);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("获取失败");
        }
        return R.ok(hdNzbankAccountDYList,"获取成功");
    }




}
