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

package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.Date;

/**
 * 
 *
 * @author zxw
 * @date 2019-11-06 14:01:28
 */
@Data
@TableName("hd_loan_account")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class HdLoanAccount extends Model<HdLoanAccount> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 创建人id
     */
    private Integer createById;

    @Transient
    @Excel(name = "银行账户*",width = 18)
    private String bankAccount;

    @Transient
    @Excel(name = "授信银行",width = 18)
    private String bankno;

    /**
     * 已批复授信金额
     */
    @Excel(name = "已批复授信金额*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="已批复授信金额")
    private String passedCredit;
    /**
     * 已使用授信金额
     */
    @Excel(name = "已使用授信金额*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="已使用授信金额")
    private String usedCredit;
    /**
     * 可使用授信额度
     */
    @Excel(name = "可使用授信额度*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="可使用授信额度")
    private String useableCredit;
    /**
     * 拟新增授信额度
     */
    @Excel(name = "拟新增授信额度*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="拟新增授信额度")
    private String planCredit;
    /**
     * 批复授信期限
     */
    @Excel(name = "批复授信期限(1999-01-01~2000-01-01)*",width = 18)
    @ApiModelProperty(value="批复授信期限")
    private String deadlineregion;

    /**
     * 金额
     */
    @Excel(name = "金额*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="金额")
    private String money;

    /**
     * 贷款日
     */
    @Excel(name = "贷款日(1999-01-01)*",format = "yyyy-MM-dd",width = 18)
    @ApiModelProperty(value="贷款日")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date loandate;
    /**
     * 到账日
     */
    @Excel(name = "到账日(1999-01-01)*",format = "yyyy-MM-dd",width = 18)
    @ApiModelProperty(value="到账日")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date deaddate;

    /**
     * 利率
     */
    @Excel(name = "利率(%)*",width = 18)
    @ApiModelProperty(value="利率")
    private String rate;


    /**
     * 借账方式
     */
    @Excel(name = "借账方式*",width = 18)
    @ApiModelProperty(value="借账方式")
    private String method;

    /**
     * 备注1
     */
    @ApiModelProperty(value="备注1")
    private String by1;
    /**
     * 备注2
     */
    @ApiModelProperty(value="备注2")
    private String by2;
    /**
     * 授信银行
     */
    @ApiModelProperty(value="授信银行")
    private String bankId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private String createUserId;
    /**
     * 租户
     */
    @ApiModelProperty(value="租户",hidden=true)
    private int tenantId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;




    }
