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


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Data
@TableName("hd_bank_account")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_account")
public class HdBankAccount extends Model<HdBankAccount> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="主键")
    private String id;
    /**
     * 内转账号
     */
    @Excel(name = "内转银行账号",width = 18,orderNum = "1")
    @ApiModelProperty(value="内转账号")
    private String internalBankAccountId;
    /**
     * 银行名称
     */
    @Excel(name = "银行名称*",width = 18,orderNum = "3")
    @ApiModelProperty(value="银行名称")
    private String bankName;
    /**
     * 外部索引
     */
    @ApiModelProperty(value="外部索引")
    private String externalIndex;
    /**
     * 外部账户
     */
    @Excel(name = "外部银行账号*",width = 18,orderNum = "2")
    @ApiModelProperty(value="外部账户")
    private String externalBankAccountId;
    /**
     * 外部账户名称
     */
    @ApiModelProperty(value="外部账户名称")
    private String externalBankName;
    /**
     * 租户id
     */
    @ApiModelProperty(value="租户id",hidden=true)
    private Integer tenantId;
    /**
     * 创建日期
     */
    @ApiModelProperty(value="创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value="创建人名称")
    private String createBy;
    /**
     * 银行账户管理
     */
    @Excel(name = "账户类型*(一般户,专户,理财户,基本户以及贷款户选其一)",width = 18,orderNum = "4")
    @ApiModelProperty(value="银行账户类型")
    private String bankType;
    /**
     * 银行编号
     */
    @Excel(name = "银行编号*",width = 18,orderNum = "0")
    @ApiModelProperty(value="银行编号")
    private String bankCode;
    /**
     * 已批复授信金额
     */
    @Excel(name = "已批复授信金额",width = 18,orderNum = "5")
    @ApiModelProperty(value="已批复授信金额")
    private String passedCredit;
    /**
     * 已使用授信金额
     */
    @Excel(name = "已使用授信金额",width = 18,orderNum = "6")
    @ApiModelProperty(value="已使用授信金额")
    private String usedCredit;
    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Integer createById;
    }