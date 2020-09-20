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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-05 09:43:38
 */
@Data
@TableName("hd_bank_tenant")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class HdBankTenant extends Model<HdBankTenant> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="主键")
    private String id;
    /**
     * 租户名称即抬头名称
     */
    @ApiModelProperty(value="租户名称即抬头名称")
    private String tenantName;
    /**
     * 租户id即抬头对应租户
     */
    @ApiModelProperty(value="租户id即抬头对应租户",hidden=true)
    private Integer tenantId;
    /**
     * 创建日期
     */
    @ApiModelProperty(value="创建日期")
    private LocalDateTime createTime;
    /**
     * 创建人登录名称
     */
    @ApiModelProperty(value="创建人登录名称")
    private String createBy;
    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Integer createById;
    }
