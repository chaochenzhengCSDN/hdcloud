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
 * @date 2019-12-04 11:23:50
 */
@Data
@TableName("hd_cash_statement")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "cash_statement")
public class HdCashStatement extends Model<HdCashStatement> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private String id;
    /**
     * 日期
     */
    @Excel(name = "日期", format = "yyyy-MM-dd",width = 18)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value="日期")
    private Date accountDate;
    /**
     * 凭证编号
     */
    @ApiModelProperty(value="凭证编号")
    private BigDecimal no;
    /**
     * 凭证编号格式化
     */
    @ApiModelProperty(value="凭证编号")
    private String noVo;

    /**
     * 对方科目
     */
    @Excel(name = "对方科目",width = 18)
    @ApiModelProperty(value="对方科目")
    private String subjects;
    /**
     * 摘要
     */
    @Excel(name = "摘要",width = 18)
    @ApiModelProperty(value="摘要")
    private String remark;
    /**
     * 单位名称
     */
    @ApiModelProperty(value="单位名称")
    private Integer unitId;
    @Excel(name="单位名称",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value = "单位名称导入和展示用")
    private String unitName;
    /**
     * 收入金额
     */
    @Excel(name="收入金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="收入金额")
    private BigDecimal income;
    /**
     * 付出金额
     */
    @Excel(name="付出金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="付出金额")
    private BigDecimal pay;

    @Excel(name="结存金额",numFormat ="#,##0.00",width = 18)
    @ApiModelProperty(value="结存金额")
    private BigDecimal balance;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value="创建人名称")
    private String createBy;
    /**
     * 租户
     */
    @ApiModelProperty(value="租户",hidden=true)
    private Integer tenantId;
    /**
     * 账单类型
     */
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
     * 备用字段
     */
    @ApiModelProperty(value="备用字段")
    private String by2;
    /**
     * 备用字段
     */
    @ApiModelProperty(value="备用字段")
    private String by3;
    /**
     * 创建日期
     */
    @ApiModelProperty(value="创建日期")
    private Date createTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private Integer createById;

    }
