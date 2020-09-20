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

package com.hodo.hdcloud.pay.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.pay.entity.PayTradeOrder;
import com.hodo.hdcloud.pay.service.PayTradeOrderService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 支付
 *
 * @author hodo
 * @date 2019-05-28 23:58:18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/order" )
@Api(value = "order", tags = "订单")
public class PayTradeOrderController {

    private final  PayTradeOrderService payTradeOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param payTradeOrder 支付
     * @return
     */
    @GetMapping("/page" )
    public R getPayTradeOrderPage(Page page, PayTradeOrder payTradeOrder) {
        return R.ok(payTradeOrderService.page(page, Wrappers.query(payTradeOrder)));
    }


    /**
     * 通过id查询支付
     * @param orderId id
     * @return R
     */
    @GetMapping("/{orderId}" )
    public R getById(@PathVariable("orderId" ) String orderId) {
        return R.ok(payTradeOrderService.getById(orderId));
    }

    /**
     * 新增支付
     * @param payTradeOrder 支付
     * @return R
     */
    @SysLog("新增支付" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('pay_paytradeorder_add')" )
    public R save(@RequestBody PayTradeOrder payTradeOrder) {
        return R.ok(payTradeOrderService.save(payTradeOrder));
    }

    /**
     * 修改支付
     * @param payTradeOrder 支付
     * @return R
     */
    @SysLog("修改支付" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('pay_paytradeorder_edit')" )
    public R updateById(@RequestBody PayTradeOrder payTradeOrder) {
        return R.ok(payTradeOrderService.updateById(payTradeOrder));
    }

    /**
     * 通过id删除支付
     * @param orderId id
     * @return R
     */
    @SysLog("删除支付" )
    @DeleteMapping("/{orderId}" )
    @PreAuthorize("@pms.hasPermission('pay_paytradeorder_del')" )
    public R removeById(@PathVariable String orderId) {
        return R.ok(payTradeOrderService.removeById(orderId));
    }

}
