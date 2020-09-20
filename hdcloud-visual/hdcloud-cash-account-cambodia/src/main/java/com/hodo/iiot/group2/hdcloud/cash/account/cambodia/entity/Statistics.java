package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;

public class Statistics {

    private String companyId;
    @Excel(name="公司编码" ,width = 18)
    private String companyCode;
    @Excel(name="公司名称",width = 18)
    private String companyName;
    @Excel(name="期初金额",width = 18,type = 10)
    //@Excel(name="期初金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal beginBalance;
    @Excel(name="收入金额",width = 18,type = 10)
    //@Excel(name="收入金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentIncome;
    @Excel(name="支出金额",width = 18,type = 10)
    //@Excel(name="支出金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentPay;
    @Excel(name="期末余额",width = 18,type = 10)
    //@Excel(name="期末余额",numFormat ="#,##0.00",width = 18)
    private BigDecimal endBalance;
    private String tenantId;
    private String headName;
    //打印加入截止时间
    //截止时间
    private String endTime;
    //操作人
    private String userName;
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getBeginBalance() {
        return beginBalance;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public BigDecimal getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(BigDecimal currentIncome) {
        this.currentIncome = currentIncome;
    }

    public BigDecimal getCurrentPay() {
        return currentPay;
    }

    public void setCurrentPay(BigDecimal currentPay) {
        this.currentPay = currentPay;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }
}
