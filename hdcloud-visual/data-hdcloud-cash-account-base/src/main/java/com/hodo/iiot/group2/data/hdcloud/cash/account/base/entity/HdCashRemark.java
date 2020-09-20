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

package com.hodo.iiot.group2.data.hdcloud.cash.account.base.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@Data
@TableName("hd_cash_remark")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class HdCashRemark extends Model<HdCashRemark> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private Integer id;
    /**
     * 摘要
     */
    private String remark;
    /**
     * 创建人名称
     */
    private String createBy;
    /**
     * 创建人id
     */
    private Integer createById;
    /**
     * 租户id
     */
    private Integer tenantId;
    /**
     * 创建时间
     */
    private String createTime;
    }
