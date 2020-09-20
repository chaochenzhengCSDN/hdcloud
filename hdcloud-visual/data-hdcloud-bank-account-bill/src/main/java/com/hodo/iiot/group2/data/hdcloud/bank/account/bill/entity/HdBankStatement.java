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

package

        com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Data
@TableName("hd_bank_statement")
@EqualsAndHashCode(callSuper = true)
public class HdBankStatement extends Model<HdBankStatement> {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "")
    private String id;
    /**
     * 创建人id
     */
    private Integer createById;
    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;
    /**
     * 凭证编号
     */
    @ApiModelProperty(value = "凭证编号")
    private BigDecimal no;
    /**
     * 对方科目
     */
    @ApiModelProperty(value = "对方科目")
    private String subjects;
    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要")
    private String remark;
    /**
     * 收入金额
     */
    @ApiModelProperty(value = "收入金额")
    private BigDecimal income;
    /**
     * 付出金额
     */
    @ApiModelProperty(value = "付出金额")
    private BigDecimal pay;
    /**
     * 结存金额
     */
    @ApiModelProperty(value = "结存金额")
    private BigDecimal balance;
    /**
     * 公司名
     */
    @ApiModelProperty(value = "公司名")
    private String companyId;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createName;
    /**
     * 创建人登录名称
     */
    @ApiModelProperty(value = "创建人登录名称")
    private String createBy;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String rid;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String sheetid;
    /**
     * 租户
     */
    @ApiModelProperty(value = "租户")
    private Integer tenantId;
    /**
     * 同步时间
     */
    @ApiModelProperty(value = "同步时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date synaccountDate;
    /**
     * 账单类型
     */
    @ApiModelProperty(value = "账单类型")
    private String accountType;
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    private String bankName;
    /**
     * 我方科目
     */
    @ApiModelProperty(value = "我方科目")
    private String mySubjects;
    /**
     * 账单来源标示
     */
    @ApiModelProperty(value = "账单来源标示")
    private String sourceType;
    /**
     * 账单来源id只有内账
     */
    @ApiModelProperty(value = "账单来源id只有内账")
    private String sourceId;
    /**
     * 银行账户id
     */
    @ApiModelProperty(value = "银行账户id")
    private String bankAccountId;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String by2;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String companyMemberId;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String by3;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String by4;


    @TableField(exist = false)
    private String bankAccount;

    @TableField(exist = false)
    private String size;

    @TableField(exist = false)
    private String companyMemberName;

    @TableField(exist = false)
    private String companyMemberCode;


    @TableField(exist = false)
    private String companyName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}
