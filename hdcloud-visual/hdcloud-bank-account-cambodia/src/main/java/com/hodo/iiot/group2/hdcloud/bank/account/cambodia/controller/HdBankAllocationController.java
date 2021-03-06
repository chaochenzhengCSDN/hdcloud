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

import cn.afterturn.easypoi.exception.excel.ExcelImportException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAllocation;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankAllocationService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.jjUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/hdbankallocation" )
@Api(value = "hdbankallocation", tags = "管理")
public class HdBankAllocationController extends BaseContrller {

    private final HdBankAllocationService hdBankAllocationService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdBankAllocationPage(Map<String,Object> params) {
        return R.ok(hdBankAllocationService.page(params));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(hdBankAllocationService.getById(id));
    }

    /**
     * 新增
     * @param hdBankAllocation 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody HdBankAllocation hdBankAllocation) {
        return R.ok(hdBankAllocationService.save(hdBankAllocation));
    }

    /**
     * 修改
     * @param hdBankAllocation 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody HdBankAllocation hdBankAllocation) {
        return R.ok(hdBankAllocationService.updateById(hdBankAllocation));
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
        return R.ok(hdBankAllocationService.removeById(id));
    }

    //批量删除
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("批量移除对象")
    public R doBatchDel(String ids) {
        if(StringUtil.isEmpty(ids)){
            return R.failed("ids获取失败");
        }
        List<String> idStrs = new ArrayList<>();
        String message = null;
        int count = 0;
        try{
            for(String id:ids.split(",")){
                //远程调用批量删除
                idStrs.add(id);
                count++;
            }
            remoteBillService.batchDelBankAllocation(idStrs);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("删除失败");
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);

    }

    //导出
    @ApiOperation("导出*")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam @ApiParam(value = "参数名称(bankAccountFrom,bankAccountTo,startTime,endTime)")Map<String, Object> params){
        //params.put("isPage",false);

        List<HdBankAllocation> hdTybankAllocationList=null;
        try {
            //undefined
            //for()
            hdTybankAllocationList = remoteBillService.getAllBankAllocation(params);
            if(hdTybankAllocationList==null){
                hdTybankAllocationList = new ArrayList<>();
                System.out.println("调拨单查询失败");
            }
            ExcelUtil excelUtil = new ExcelUtil<HdBankAllocation>();
            excelUtil.print("调拨单列表", HdBankAllocation.class, "调拨单列表", "导出时间:" + jjUtil.getDateStr(), "调拨单列表", hdTybankAllocationList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //导入
    @ApiOperation("导入*")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        System.out.println("测试导入");
        R<Integer> entityObjectRestResponse = new R<>();
        List<HdBankAllocation> hdBankAllocations;
        List<HdBankAccount> hdBankAccounts=null;
        ExcelUtil excelUtil = new ExcelUtil<HdBankAllocation>();
        try{
            hdBankAllocations = excelUtil.importXls(file,HdBankAllocation.class);
            Map<String,List<HdBankAccount>> checkBankAccount = null;
            checkBankAccount = hdBankAllocationService.getTenantAndDict();
            if(checkBankAccount==null){
                return R.failed("账号数据异常");
            }
            if(hdBankAllocations!=null&&hdBankAllocations.size()>0){
                for(HdBankAllocation hdBankAllocation:hdBankAllocations){
                    String bankNameFrom = null;
                    String bankNameTo = null;
                    String bankIdFrom = null;
                    String bankIdTo = null;
                    if(StringUtil.isEmpty(hdBankAllocation.getDrawee())){
                        return R.failed("请填写付款人");
                    }
                    if(StringUtil.isEmpty(hdBankAllocation.getPayee())){
                        return R.failed("请填写收款人");
                    }
                    if(StringUtil.isEmpty(hdBankAllocation.getBankAccountFrom())){
                        return R.failed("请填写付款人账号");
                    }
                    if(StringUtil.isEmpty(hdBankAllocation.getBankAccountTo())){
                        return R.failed("请填写收款人账号");
                    }
                    //金额
                    if(hdBankAllocation.getMoney()==null){
                        return R.failed("请填写金额");
                    }
                    //金额详情
                    if(StringUtil.isEmpty(hdBankAllocation.getMoneyPurpose())){
                        return R.failed("请填写资金用途");
                    }
                    //时间
                    if(hdBankAllocation.getAllocationDate()==null){
                        return R.failed("请填写调拨时间");
                    }
                    //全称查询是否存在
                    if(checkBankAccount.containsKey(hdBankAllocation.getDrawee())){
                        hdBankAccounts = checkBankAccount.get(hdBankAllocation.getDrawee());
                    }
                    if(hdBankAccounts==null){
                        return R.failed("付款人下无法匹对账户");
                    }
                    for(HdBankAccount hdBankAccount:hdBankAccounts){
                        if(hdBankAccount.getExternalBankAccountId().equals(hdBankAllocation.getBankAccountFrom())||
                                hdBankAccount.getInternalBankAccountId()!=null&&hdBankAccount.getInternalBankAccountId().equals
                                        (hdBankAllocation.getBankAccountFrom())){
                            bankNameFrom = hdBankAccount.getBankName();
                            hdBankAllocation.setBankNameFrom(bankNameFrom);
                            bankIdFrom = hdBankAccount.getId();
                            hdBankAllocation.setBankAccountIdFrom(bankIdFrom);
                            break;
                        }
                    }
                    if(bankNameFrom==null){
                        return R.ok("付款人下无法匹对账户");
                    }
                    if(checkBankAccount.containsKey(hdBankAllocation.getPayee())){
                        hdBankAccounts = checkBankAccount.get(hdBankAllocation.getPayee());
                    }
                    if(hdBankAccounts==null){
                        return R.ok("收款人下无法匹对账户");
                    }
                    for(HdBankAccount hdBankAccount:hdBankAccounts){
                        if(hdBankAccount.getExternalBankAccountId().equals(hdBankAllocation.getBankAccountTo())||
                                hdBankAccount.getInternalBankAccountId()!=null&&hdBankAccount.getInternalBankAccountId().
                                        equals(hdBankAllocation.getBankAccountTo())){
                            bankNameTo = hdBankAccount.getBankName();
                            hdBankAllocation.setBankNameTo(bankNameTo);
                            bankIdTo = hdBankAccount.getId();
                            hdBankAllocation.setBankAccountIdTo(bankIdTo);
                            break;
                        }
                    }
                    if(bankNameTo==null){
                        return R.failed("收款人下无法匹对账户");
                    }
                }
            }
            hdBankAllocationService.batchSave(hdBankAllocations);
            //租户id获取银行账户看是否包含
            return R.ok("导入成功");
        }catch (ExcelImportException ex){
            return R.failed("获取导入文件错误请,检查内容格式");
        } catch (Exception e){
            e.printStackTrace();
            return R.failed("获取数据失败");
        }
    }


}
