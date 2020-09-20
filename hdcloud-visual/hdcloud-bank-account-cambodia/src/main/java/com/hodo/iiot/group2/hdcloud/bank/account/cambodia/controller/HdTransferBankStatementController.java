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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdTransferBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdtransferbankstatement" )
@Api(value = "hdtransferbankstatement", tags = "管理")
public class HdTransferBankStatementController extends BaseContrller {

    private final HdTransferBankStatementService hdTransferBankStatementService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdTransferBankStatementPage(Map<String,Object> params) {
        return hdTransferBankStatementService.page(params);
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return hdTransferBankStatementService.getById(id);
    }

    /**
     * 新增
     * @param hdTransferBankStatement 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdTransferBankStatement hdTransferBankStatement) {
        return hdTransferBankStatementService.save(hdTransferBankStatement);
    }

    /**
     * 修改
     * @param hdTransferBankStatement 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdTransferBankStatement hdTransferBankStatement) {
        return hdTransferBankStatementService.updateById(hdTransferBankStatement);
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
                t.setCreateTime(jjUtil.parseDate(jjUtil.formatDate(new Date())));
                t.setRemark("用于" + jjUtil.formatDate(hdTransferBankStatement.getCreateTime()) + ",凭证编号为：" + hdTransferBankStatement.getNo() + "的账务冲账！");
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
        HdCompany subject;
        try {
            hdTransferBankStatements = excelUtil.importXls(file,HdTransferBankStatement.class);
            int count = 0;
            Map<String,Object> paramsCode = new HashedMap<>();

            if(hdTransferBankStatements!=null&&hdTransferBankStatements.size()>0){
                for(HdTransferBankStatement hdNzbankAccount:hdTransferBankStatements){
                    count++;
                    //原公司匹配改成成员单位匹配
                    if(StringUtil.isEmpty(hdNzbankAccount.getIncomeSubjectCode())){
                        return R.failed("第" + count + "条数据收入公司编码为空");
                    }
                    paramsCode.put("code",hdNzbankAccount.getIncomeSubjectCode());
                    subject = remoteBaseService.getCompanyByCode(paramsCode);
                    if(subject==null){
                        return R.failed("第" + count + "条数据收入公司编码系统无法匹配！");
                    }
                    //公司
                    hdNzbankAccount.setIncomeSubjectId(subject.getId());

                    if(StringUtil.isEmpty(hdNzbankAccount.getPaySubjectCode())){
                        return R.failed("第" + count + "条数据支出公司编码为空！");
                    }
                    paramsCode.put("code",hdNzbankAccount.getPaySubjectCode());
                    subject = remoteBaseService.getCompanyByCode(paramsCode);
                    if(subject==null){
                        return R.failed("第" + count + "条数据支出成员单位编码系统无法匹配！");
                    }
                    hdNzbankAccount.setPaySubjectId(subject.getId());
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
        Map<String,Object> requestParam = new org.apache.commons.collections.map.HashedMap();
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
        HdCompany hdCompany;
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
                    if(StringUtil.isNotEmpty(hdTransferBankStatement.getIncomeSubjectId())){
                        hdCompany = remoteBaseService.getCompanyById(hdTransferBankStatement.getIncomeSubjectId()).getData();
                        if(hdCompany!=null){
                            hdTransferBankStatementDY.setIncomeSubjectName(hdCompany.getName());
                            hdTransferBankStatementDY.setIncomeSubjectCode(hdCompany.getCode());
                        }
                    }
                    if(StringUtil.isNotEmpty(hdTransferBankStatement.getPaySubjectId())){
                        hdCompany = remoteBaseService.getCompanyById(hdTransferBankStatement.getPaySubjectId()).getData();
                        if(hdCompany!=null){
                            hdTransferBankStatementDY.setPaySubjectName(hdCompany.getName());
                            hdTransferBankStatementDY.setPaySubjectCode(hdCompany.getCode());
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
                    //获取租户名称
                    Map<String,Object> params = new org.apache.commons.collections.map.HashedMap();
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
