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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@Data
@TableName("hd_bank_allocation")
@EqualsAndHashCode(callSuper = true)
public class HdBankAllocation extends Model<HdBankAllocation> {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 付款人开户行
     */
    private String bankNameFrom;
    /**
     * 收款人开户行
     */
    private String bankNameTo;
    /**
     * 付款人账号
     */
    private String bankAccountFrom;
    /**
     * 收款人账号
     */
    private String bankAccountTo;
    /**
     * 付款人
     */
    private String drawee;
    /**
     * 收款人
     */
    private String payee;
    /**
     * 调拨操作日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date allocationDate;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 资金用途
     */
    private String moneyPurpose;

    /**
     * 创建人名称
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 租户id
     */
    private Integer tenantId;
    /**
     * 调拨单编号
     */
    private String number;
    /**
     * 付款人银行id
     */
    private String bankAccountIdFrom;
    /**
     * 收款人银行id
     */
    private String bankAccountIdTo;

    /**
     * 创建人id
     */
    private Integer createById;


}
