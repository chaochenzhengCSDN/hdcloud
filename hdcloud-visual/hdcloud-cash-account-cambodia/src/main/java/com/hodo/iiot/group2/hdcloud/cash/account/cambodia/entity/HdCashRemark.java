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

package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@Data
@TableName("hd_cash_remark")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "cash_remark")
public class HdCashRemark extends Model<HdCashRemark> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 摘要
     */
    @Excel(name = "摘要",width = 18)
    @ApiModelProperty(value="摘要")
    private String remark;
    /**
     * 创建人名称
     */

    @ApiModelProperty(value="创建人名称")
    private String createBy;
    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Integer createById;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间",width = 18)
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 租户id
     */
    @ApiModelProperty(value="租户id",hidden=true)
    private Integer tenantId;
    }
