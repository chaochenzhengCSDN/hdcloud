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

        com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

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
public class HdTransferBankStatement extends Model<HdTransferBankStatement> {
private static final long serialVersionUID=1L;

/**
 * 
 */
@TableId(type = IdType.UUID)
@ApiModelProperty(value = "")
private String id;
/**
 * 凭证编号
 */
@ApiModelProperty(value = "凭证编号")
private BigDecimal no;

/**
 * 创建人id
 */
private Integer createById;


/**
 * 借方科目
 */
    @ApiModelProperty(value = "借方科目")
private String incomeSubjectId;
/**
 * 摘要
 */
@ApiModelProperty(value = "摘要")
@Excel(name = "摘要*",width = 18)
private String remark;
/**
 * 金额
 */
@ApiModelProperty(value = "金额")
@Excel(name = "交易金额*",numFormat ="#,##0.00",width = 18)
private BigDecimal money;
/**
 * 贷方科目
 */
    @ApiModelProperty(value = "贷方科目")
private String paySubjectId;
/**
 * 创建时间
 */
@Excel(name = "记账日期*", format = "yyyy-MM-dd",width = 18)
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
@Excel(name = "银行账号*",width = 18)
    @ApiModelProperty(value = "开户行")
private String bankAccountId;
/**
 * 备注1
 */
    @ApiModelProperty(value = "备注1")
    @Excel(name = "凭证编号",width = 18)
private String by1;
/**
 * 备注2
 */
    @ApiModelProperty(value = "备注2")
private String by2;
/**
 * 收入成员单位
 */
    @ApiModelProperty(value = "收入成员单位")
private String memberIncomeSubjectId;
/**
 * 支付成员单位
 */
    @ApiModelProperty(value = "支付成员单位")
private String memberPaySubjectId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;


    //收入公司编码
    @TableField(exist = false)
    @ApiModelProperty(value="收入公司编号打印",name="incomesubjectNo")
    private String incomesubjectNo;
    //支出公司编码
    @TableField(exist = false)
    @ApiModelProperty(value="支出公司编号打印",name="paysubjectNo")
    private String paysubjectNo;

    //银行名称字段
    @Transient
    @ApiModelProperty(value="银行名称停用",name="banknoId")
    private String banknoId;

    //收入成员单位编号
    @TableField(exist = false)
    private String memberIncomeSubjectCode;

    //支付成员单位编号
    @TableField(exist = false)
    private String memberPaySubjectCode;

    //收入成员单位名
    @TableField(exist = false)
    private String memberIncomeSubjectName;

    //支付成员单位名
    @TableField(exist = false)
    private String memberPaySubjectName;

    //收入成员单位名
    @TableField(exist = false)
    @Excel(name = "收入公司名称*",width = 18)
    private String incomeSubjectName;

    //支付成员单位名
    @TableField(exist = false)
    @Excel(name = "支出公司名称*",width = 18)
    private String paySubjectName;

    @TableField(exist = false)
    @Excel(name = "银行编号",width = 18)
    private String bankAccountCode;

    @TableField(exist = false)
    private String bankCode;

    @TableField(exist = false)
    @Excel(name = "银行名称",width = 18)
    private String bankName;

    @Transient
    private String value;

}
