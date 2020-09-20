package com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistics {

    private String companyId;

    private String companyCode;

    private String companyName;

    private BigDecimal beginBalance;

    private BigDecimal currentIncome;

    private BigDecimal currentPay;

    private BigDecimal endBalance;

    private String tenantId;

    private String headName;
    //打印加入截止时间
    //截止时间
    private String endTime;
    //操作人
    private String userName;

}
