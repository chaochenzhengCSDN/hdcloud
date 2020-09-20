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

import java.time.LocalDateTime;
import java.util.Date;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:37:59
 */
@Data
@TableName("hd_bank_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_record")
public class HdBankRecord extends Model<HdBankRecord> {
private static final long serialVersionUID=1L;

/**
 * 主键
 */
        @TableId
    @ApiModelProperty(value = "主键")
private String id;
/**
 * 凭证编号
 */
@Excel(name = "凭证编号",width = 18)
    @ApiModelProperty(value = "凭证编号")
private String num;
/**
 * 创建日期
 */
@Excel(name = "操作日期", format = "yyyy-MM-dd",width = 18)
@ApiModelProperty(value = "创建日期")
@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
private Date createTime;
/**
 * 公司名稱
 */
@Excel(name = "公司名称",width = 18)
@ApiModelProperty(value = "公司名稱")
private String companyName;
/**
 * 成员单位id
 */
@Excel(name = "成员单位",width = 18)
@ApiModelProperty(value = "成员单位名稱")
private String companyMemberName;
/**
 * 摘要
 */
@Excel(name = "摘要",width = 40)
    @ApiModelProperty(value = "摘要")
private String remark;
/**
 * 收支方向
 */
@Excel(name = "收支方向",width = 18)
@ApiModelProperty(value = "收支方向")
private String flag;
/**
 * 公司名
 */
    @ApiModelProperty(value = "公司id")
private String companyId;


/**
 * 金额
 */
@Excel(name = "金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value = "金额")
private String money;
@Excel(name = "银行账号",width = 18)
@ApiModelProperty(value = "銀行賬戶")
private String bankAccount;
@Excel(name = "银行名称",width = 18)
@ApiModelProperty(value = "銀行名稱")
private String bankName;
/**
 * 创建人名称
 */

@ApiModelProperty(value = "创建人名称")
private String createName;
/**
 * 创建人
 */
@Excel(name = "操作人",width = 18)
@ApiModelProperty(value = "创建人")
private String createBy;
/**
 * 操作类型
 */
@Excel(name = "操作类型",width = 18)
@ApiModelProperty(value = "操作类型")
private String operType;
/**
 * 租户
 */
    @ApiModelProperty(value = "租户")
private String tenantId;

/**
 * 银行账户ID
 */
    @ApiModelProperty(value = "银行账户ID")
private String accountId;
/**
 * 成员单位id
 */
    @ApiModelProperty(value = "成员单位id")
private String companyMemberId;

/**
 * 备用1
 */
@ApiModelProperty(value = "备用1")
private String by;
@ApiModelProperty(value = "凭证编号格式化")
private String by1;

}
