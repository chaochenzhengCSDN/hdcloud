package com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComDetailStatistics {
    private static final long serialVersionUID = 1L;
    //到账日期
    private String synAccountDate;
    //记账日期
    private String accountDate;
    //摘要
    private String remark;
    //贷方金额
    private BigDecimal income;
    //借方金额
    private BigDecimal pay;

        //余额
    private BigDecimal balance;
    //新增明细对方单位
    private String subject;
    //银行
//    @Excel(name = "银行")
//    private String bank;
    private String bankCode;
    private String bankAccount;
    private String bankName;

    private String flag;
    private String no;


}
