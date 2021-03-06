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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @date 2019-10-29 15:28:23
 */
@Data
@TableName("hd_match_company")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "match_company")
public class HdMatchCompany extends Model<HdMatchCompany> {
private static final long serialVersionUID=1L;

/**
 * 主键
 */
        @TableId
    @ApiModelProperty(value = "主键")
private String id;
/**
 * 对方单位名称
 */
    @ApiModelProperty(value = "对方单位名称")
private String customerName;
/**
 * 公司代码
 */
    @ApiModelProperty(value = "公司代码")
private String companyCode;
/**
 * 公司名称
 */
    @ApiModelProperty(value = "公司名称")
private String companyName;
/**
 * 租户
 */
    @ApiModelProperty(value = "租户", hidden =true)
private String tenantId;
/**
 * 创建时间
 */
    @ApiModelProperty(value = "创建时间")
private Date createTime;
/**
 * 创建人名称
 */
    @ApiModelProperty(value = "创建人名称")
private String createBy;
/**
 * 公司id
 */
    @ApiModelProperty(value = "公司id")
private String companyId;
}
