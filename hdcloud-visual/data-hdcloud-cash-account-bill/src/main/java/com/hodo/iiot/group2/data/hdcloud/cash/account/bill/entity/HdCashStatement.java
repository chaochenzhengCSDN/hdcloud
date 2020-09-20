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

package com.hodo.iiot.group2.data.hdcloud.cash.account.bill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@Data
@TableName("hd_cash_statement")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class HdCashStatement extends Model<HdCashStatement> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String id;
    /**
     * 日期
     */
    private LocalDateTime accountDate;
    /**
     * 凭证编号
     */
    private BigDecimal no;
    /**
     * 对方科目
     */
    private String subjects;
    /**
     * 摘要
     */
    private String remark;
    /**
     * 收入金额
     */
    private BigDecimal income;
    /**
     * 付出金额
     */
    private BigDecimal pay;
    /**
     * 创建人名称
     */
    private String createBy;
    /**
     * 租户
     */
    private Integer tenantId;
    /**
     * 账单类型
     */
    private String accountType;
    /**
     * 我方科目
     */
    private String mySubjects;
    /**
     * 账单来源标示
     */
    private String sourceType;
    /**
     * 备用字段
     */
    private String by2;
    /**
     * 备用字段
     */
    private String by3;
    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    private Integer createById;

    private Integer unitId;

    /**
     * 单位名称
     */
    @TableField(exist = false)
    private String unitName;

    @TableField(exist = false)
    private String beginBalance;

    @TableField(exist = false)
    private String currentIncome;

    @TableField(exist = false)
    private String currentPay;

    @TableField(exist = false)
    private String endBalance;

    }
