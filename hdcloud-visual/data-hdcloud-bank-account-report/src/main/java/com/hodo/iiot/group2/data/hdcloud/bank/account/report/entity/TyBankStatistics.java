package com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TyBankStatistics {
    private static final long serialVersionUID = 1L;

    private String bankName;

    private String superCompName;

    private String name;

    private String bankCode;

    private String superCompCode;

    private String code;


    //期初余额
    private BigDecimal beginBalance;
    //本期收入
    private BigDecimal currentIncome;
    //本期支出
    private BigDecimal currentPay;
    //期末余额
    private BigDecimal endBalance;
    //内转收入
    private BigDecimal nzIncome;
    //内转支出
    private BigDecimal nzPay;

    //租户id
    private String tenantid;



}
