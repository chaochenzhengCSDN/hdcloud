///*
// *    Copyright (c) 2018-2025, hodo All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// *
// * Redistributions of source code must retain the above copyright notice,
// * this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright
// * notice, this list of conditions and the following disclaimer in the
// * documentation and/or other materials provided with the distribution.
// * Neither the name of the pig4cloud.com developer nor the names of its
// * contributors may be used to endorse or promote products derived from
// * this software without specific prior written permission.
// * Author: 江苏红豆工业互联网有限公司
// */
//
//package com.hodo.iiot.group2.hdcloudbankaccountcurrency.controller;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.hodo.hdcloud.common.core.util.R;
//import com.hodo.hdcloud.common.log.annotation.SysLog;
//import bank_account.generator.service.HdMatchCompanyService;
//import HdMatchCompany;
//import org.springframework.security.access.prepost.PreAuthorize;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//
///**
// * bank_account
// *
// * @author dq
// * @date 2019-10-29 15:28:23
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/hdmatchcompany")
//@Api(value = "hdmatchcompany", tags = "bank_account管理")
//public class HdMatchCompanyController {
//
//    private final HdMatchCompanyService hdMatchCompanyService;
//
//    /**
//     * 分页查询
//     * @param page 分页对象
//     * @param hdMatchCompany bank_account
//     * @return
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @GetMapping("/page")
//    public R getHdMatchCompanyPage(Page page, HdMatchCompany hdMatchCompany) {
//        return R.ok(hdMatchCompanyService.page(page, Wrappers.query(hdMatchCompany)));
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
//        return R.ok(hdMatchCompanyService.getById(id));
//    }
//
//    /**
//     * 新增bank_account
//     * @param hdMatchCompany bank_account
//     * @return R
//     */
//    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
//    @SysLog("新增bank_account")
//    @PostMapping
//    @PreAuthorize("@pms.hasPermission('generator_hdmatchcompany_add')")
//    public R save(@RequestBody HdMatchCompany hdMatchCompany) {
//        return R.ok(hdMatchCompanyService.save(hdMatchCompany));
//    }
//
//    /**
//     * 修改bank_account
//     * @param hdMatchCompany bank_account
//     * @return R
//     */
//    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
//    @SysLog("修改bank_account")
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('generator_hdmatchcompany_edit')")
//    public R updateById(@RequestBody HdMatchCompany hdMatchCompany) {
//        return R.ok(hdMatchCompanyService.updateById(hdMatchCompany));
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
//    @PreAuthorize("@pms.hasPermission('generator_hdmatchcompany_del')")
//    public R removeById(@PathVariable String id) {
//        return R.ok(hdMatchCompanyService.removeById(id));
//    }
//
//}
