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
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Data
@TableName("hd_bank_statement")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_statement")
public class HdBankStatement extends Model<HdBankStatement> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private String id;
    /**
     * 同步时间
     */
    @Excel(name = "到账日期*", format = "yyyy-MM-dd",width = 18)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value="同步时间")
    private Date synaccountDate;
    /**
     * 日期
     */
    @Excel(name = "记账日期", format = "yyyy-MM-dd",width = 18)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value="日期")
    private Date accountDate;

    /**
     * 导出编号
     */
    @ApiModelProperty(value = "前端展示用")
    @Excel(name = "凭证编号",width = 18)
    private String  by1;

    @Excel(name = "公司名称",width = 18)
    private String companyName;

    @Excel(name = "公司编码",width = 18)
    @ApiModelProperty(value="公司编码")
    private String companyCode;
    /**
     * 凭证编号
     */

    @ApiModelProperty(value="凭证编号")
    private BigDecimal no;
    /**
     * 对方科目
     */
    @Excel(name = "对方科目名称*",width = 18)
    @ApiModelProperty(value="对方科目")
    private String subjects;
    /**
     * 摘要
     */
    @Excel(name = "摘要",width = 18)
    @ApiModelProperty(value="摘要")
    private String remark;
    /**
     * 收入金额
     */
    @Excel(name = "收入金额*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="收入金额")
    private BigDecimal income;
    /**
     * 付出金额
     */
    @Excel(name = "支出金额*",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="支出金额")
    private BigDecimal pay;
    /**
     * 结存金额
     */
    @Excel(name = "结存金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="结存金额")
    private BigDecimal balance;

    @Excel(name = "银行账号*",width = 18)
    @ApiModelProperty(value="银行账号")
    private String bankAccount;
    /**
     * 公司id
     */
    @ApiModelProperty(value="公司id")
    private String companyId;


    /**
     * 创建人名称
     */
    @ApiModelProperty(value="创建人名称")
    private String createName;
    /**
     * 创建人登录名称
     */
    @ApiModelProperty(value="创建人登录名称")
    private String createBy;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String rid;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String sheetid;
    /**
     * 租户
     */
    @ApiModelProperty(value="租户",hidden=true)
    private Integer tenantId;

    /**
     * 银行名称
     */
    @Excel(name = "银行名称",width = 18)
    @ApiModelProperty(value="银行名称")
    private String bankName;
    /**
     * 账单类型
     */
    @Excel(name = "账单类型",width = 18)
    @ApiModelProperty(value="账单类型")
    private String accountType;
    /**
     * 我方科目
     */
    @ApiModelProperty(value="我方科目")
    private String mySubjects;
    /**
     * 账单来源标示
     */
    @ApiModelProperty(value="账单来源标示")
    private String sourceType;
    /**
     * 账单来源id只有内账
     */
    @ApiModelProperty(value="账单来源id只有内账")
    private String sourceId;
    /**
     * 银行账户id
     */
    @ApiModelProperty(value="银行账户id")
    private String bankAccountId;


    /**
     * 
     */
    @ApiModelProperty(value="")
    private String by2;
    /**
     * 成员单位id
     */
    @ApiModelProperty(value="成员单位id")
    private String companyMemberId;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String by3;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String by4;
    /**
     * 创建日期
     */
    @ApiModelProperty(value="创建日期")
    private Date createTime;
    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Integer createById;
    }
