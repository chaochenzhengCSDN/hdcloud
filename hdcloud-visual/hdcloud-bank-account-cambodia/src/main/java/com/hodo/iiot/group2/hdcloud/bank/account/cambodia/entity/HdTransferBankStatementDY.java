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

package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Data
@TableName("hd_transfer_bank_statement")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_account")
public class HdTransferBankStatementDY extends Model<HdTransferBankStatementDY> {
private static final long serialVersionUID=1L;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String id;
    /**
     * 凭证编号
     */
    @ApiModelProperty(value = "凭证编号")
    private BigDecimal no;
    /**
     * 借方科目
     */
    @ApiModelProperty(value = "借方科目")
    private String incomeSubjectId;
    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要")
    private String remark;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal money;
    /**
     * 贷方科目
     */
    @ApiModelProperty(value = "贷方科目")
    private String paySubjectId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建人登录名称
     */
    @ApiModelProperty(value = "创建人登录名称")
    private String crtUserId;
    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    private String streamNo;
    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String billNo;
    /**
     * 租户
     */
    @ApiModelProperty(value = "租户", hidden =true)
    private Integer tenantId;
    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    private String bankAccountId;
    /**
     * 备注1
     */
    @ApiModelProperty(value = "备注1")
    private String by1;
    /**
     * 备注2
     */
    @ApiModelProperty(value = "备注2")
    private String by2;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    //收入成员单位名
    @ApiModelProperty(value = "收入成员单位名")
    private String incomeSubjectName;

    //支付成员单位名
    @ApiModelProperty(value = "支付成员单位名")
    private String paySubjectName;
    //收入成员单位名
    @ApiModelProperty(value = "收入成员单位名")
    private String incomeSubjectCode;

    //支付成员单位名
    @ApiModelProperty(value = "支付成员单位名")
    private String paySubjectCode;


    @ApiModelProperty(value = "銀行code")
    private String bankAccountCode;

    @ApiModelProperty(value = "銀行名")
    private String bankName;
    @ApiModelProperty(value = "金额分解")
    private List<Character> moneyChar;
    @ApiModelProperty(value = "抬头")
    private String headName;
}
