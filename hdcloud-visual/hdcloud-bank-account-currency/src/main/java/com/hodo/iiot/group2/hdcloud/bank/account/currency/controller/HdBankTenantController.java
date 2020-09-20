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
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankTenantService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbanktenant")
@Api(value = "hdbanktenant", tags = "bank_account管理")
public class HdBankTenantController extends BaseContrller {

    private final HdBankTenantService hdBankTenantService;

//    /**
//     * 分页查询
//     * @param page 分页对象
//     * @param hdBankTenant bank_account
//     * @return
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @GetMapping("/page")
//    public R getHdBankTenantPage(Page page, HdBankTenant hdBankTenant) {
//        return R.ok(hdBankTenantService.page(page, Wrappers.query(hdBankTenant)));
//    }
//
//
//    /**
//     * 通过id查询bank_account
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id查询", notes = "通过id查询")
//    @GetMapping("/{id}")
//    public R getById(@PathVariable("id") String id) {
//        return R.ok(hdBankTenantService.getById(id));
//    }
//
    /**
     * 新增bank_account
     * @param hdBankTenant bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    public R save(@RequestBody HdBankTenant hdBankTenant) {
         return remoteBaseService.saveBankTenant(hdBankTenant);
    }
//
//    /**
//     * 修改bank_account
//     * @param hdBankTenant bank_account
//     * @return R
//     */
//    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
//    @SysLog("修改bank_account")
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('generator_hdbanktenant_edit')")
//    public R updateById(@RequestBody HdBankTenant hdBankTenant) {
//        return R.ok(hdBankTenantService.updateById(hdBankTenant));
//    }
//
//    /**
//     * 通过id删除bank_account
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
//    @SysLog("通过id删除bank_account")
//    @DeleteMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('generator_hdbanktenant_del')")
//    public R removeById(@PathVariable String id) {
//        return R.ok(hdBankTenantService.removeById(id));
//    }



    //不需要直接获取即可
    //根据租户id获取租户名称
    //根据租户id获取抬头
//    @RequestMapping(
//            value = {"getTenantNameById"},
//            method = {RequestMethod.GET}
//    )
//    @ResponseBody
//    @ApiOperation("根据租户id获取租户名称")
//    public R getBankNameByTenant(@RequestParam("tenantId") String tenantId) {
//        String result = "";
//        try {
//            return remoteBaseService.getBankNameByTenant(tenantId);
//        }catch (Exception e){
//            return R.failed("抬头绑定失败");
//        }
//    }
    //获取全部数据
//    @ApiOperation("获取全部数据")
//    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
//    @ResponseBody
//    public List<HdBankTenant> selectAll(){
//
//        Query query = new Query(new HashedMap());
//
//        return baseBiz.selectByQuery(query).getData().getRows();
//    }


}
