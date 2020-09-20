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
 * @date 2019-10-29 15:28:24
 */
@Data
@TableName("hd_match_company_member")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "match_company_member")
public class HdMatchCompanyMember extends Model<HdMatchCompanyMember> {
private static final long serialVersionUID=1L;

/**
 * 主键id
 */
        @TableId
    @ApiModelProperty(value = "主键id")
private String id;
/**
 * 成员单位id
 */
    @ApiModelProperty(value = "成员单位id")
private String companyMemberId;
/**
 * 对方单位名称
 */
@Excel(name = "对方单位名称*",width = 20,orderNum = "2")
    @ApiModelProperty(value = "对方单位名称")
private String customerName;
/**
 * 外部账号id
 */
@Excel(name = "外部账号*",width = 20,orderNum = "3")
    @ApiModelProperty(value = "外部账号id")
private String bankAccountIdExternal;
/**
 * 备用
 */
    @ApiModelProperty(value = "备用")
private String by1;
/**
 * 备用2
 */
    @ApiModelProperty(value = "备用2")
private String by2;
/**
 * 租户id
 */
    @ApiModelProperty(value = "租户id", hidden =true)
private String tenantId;
/**
 * 创建人
 */
    @ApiModelProperty(value = "创建人")
private String createBy;
/**
 * 创建时间
 */
    @ApiModelProperty(value = "创建时间")
@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
private Date createTime;
/*
成员单位code
 */
@Excel(name = "成员单位编码*",width = 20,orderNum = "0")
    @ApiModelProperty(value = "成员单位编码")
private String companyMemberCode;
/*
成员单位name
 */
@Excel(name = "成员单位名称",width = 20,orderNum = "1")
    @ApiModelProperty(value = "成员单位名称")
private String companyMemberName;


@ApiModelProperty(value = "成员单位名称")
private String companyId;
}
