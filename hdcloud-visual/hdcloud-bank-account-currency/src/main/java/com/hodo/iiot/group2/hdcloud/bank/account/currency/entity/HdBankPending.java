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

package com.hodo.iiot.group2.hdcloud.bank.account.currency.entity;

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
import java.time.LocalDateTime;
import java.util.Date;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@Data
@TableName("hd_bank_pending")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_pending")
public class HdBankPending extends Model<HdBankPending> {
private static final long serialVersionUID=1L;

/**
 * 主键
 */
        @TableId
    @ApiModelProperty(value = "主键")
private String id;
/**
 * 九恒星账单日期
 */
@Excel(name = "到账日期", format = "yyyy-MM-dd",width = 18)
@ApiModelProperty(value = "九恒星账单日期")
@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
private Date synaccountDate;
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
@Excel(name = "对方单位名称",width = 18)
    @ApiModelProperty(value = "对方科目")
private String subjects;
/**
 * 摘要
 */
@Excel(name = "摘要",width = 18)
    @ApiModelProperty(value = "摘要")
private String remark;
/**
 * 收入
 */
@Excel(name = "收入金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value = "收入")
private BigDecimal income;
/**
 * 支出
 */
@Excel(name = "支出金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value = "支出")
private BigDecimal pay;
/**
 * 结存金额
 */
    @ApiModelProperty(value = "结存金额")
private BigDecimal blance;
/**
 * 公司名
 */
    @ApiModelProperty(value = "公司名")
private String companyName;
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
 * 外转id
 */
    @ApiModelProperty(value = "外转id")
private String rid;
/**
 * 内转id
 */
    @ApiModelProperty(value = "内转id")
private String sheetid;
@Excel(name = "银行账号",width = 18)
@ApiModelProperty(value = "银行账户为账户名称")
private String bankAccount;
/**
 * 银行
 */
@Excel(name = "银行名称",width = 18)
    @ApiModelProperty(value = "银行")
private String bankName;
/**
 * 我方科目
 */
    @ApiModelProperty(value = "我方科目")
private String mySubjects;
/**
 * 租户
 */
    @ApiModelProperty(value = "租户", hidden =true)
private String tenantId;

/**
 * 账单标示
 */
    @ApiModelProperty(value = "账单标示")
private String accountType;
/**
 * 银行账户id
 */
    @ApiModelProperty(value = "银行账户id")
private String bankAccountId;

}
