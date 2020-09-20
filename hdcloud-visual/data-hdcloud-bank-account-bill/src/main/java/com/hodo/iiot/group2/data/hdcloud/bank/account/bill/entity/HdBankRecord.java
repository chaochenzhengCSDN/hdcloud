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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value = "bank_account")
public class HdBankRecord extends Model<HdBankRecord> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人id
     */
    private Integer createById;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createName;
    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private String operType;
    /**
     * 凭证编号
     */
    @ApiModelProperty(value = "凭证编号")
    private String num;
    /**
     * 摘要
     */
    @ApiModelProperty(value = "摘要")
    private String remark;
    /**
     * 公司名
     */
    @ApiModelProperty(value = "公司名")
    private String companyId;
    /**
     * 收支方向
     */
    @ApiModelProperty(value = "收支方向")
    private String flag;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private String money;
    /**
     * 租户
     */
    @ApiModelProperty(value = "租户")
    private Integer tenantId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 银行账户ID
     */
    @ApiModelProperty(value = "银行账户ID")
    private String bankAccountId;

    @TableField(exist = false)
    private String bankAccount;

    @TableField(exist = false)
    private String bankName;
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

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private String companyMemberName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}
