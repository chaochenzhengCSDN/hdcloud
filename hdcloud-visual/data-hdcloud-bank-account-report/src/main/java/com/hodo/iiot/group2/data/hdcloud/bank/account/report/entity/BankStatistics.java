package com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankStatistics {
    private static final long serialVersionUID = 1L;
    //增加银行编码
    private String bankCode;
    private String bankType;
    private String nzId;
    private String wbzh;
    //对应银行
    private String bankName;
    //期初余额
    private BigDecimal beginBalance;
    //本期收入
    private BigDecimal currentIncome;
    //本期支出
    private BigDecimal currentPay;
    //期末余额
    private BigDecimal endBalance;
    //新加不需要计算所以用String
    //已批复授信金额
    private BigDecimal passedCredit;
    //已使用授信金额
    private BigDecimal usedCredit;

}
