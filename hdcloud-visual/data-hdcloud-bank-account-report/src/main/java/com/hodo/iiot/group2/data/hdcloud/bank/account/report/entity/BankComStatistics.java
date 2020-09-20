package com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankComStatistics {
    private static final long serialVersionUID = 1L;
    private String bankCode;
    private String bankAccount;
    private String bankName;
    private BigDecimal beginBalance;
    private BigDecimal currentIncome;
    private BigDecimal currentPay;
    private BigDecimal endBalance;
    private String companyCode;
    private String companyName;

}
