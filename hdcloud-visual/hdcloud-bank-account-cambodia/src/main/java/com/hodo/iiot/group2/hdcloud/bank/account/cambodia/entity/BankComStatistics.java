package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;

//弃用
public class BankComStatistics {
    private static final long serialVersionUID = 1L;
    @Excel(name = "银行编码",width = 18)
    private String bankCode;
    @Excel(name = "银行账号",width = 18)
    private String bankAccount;
    @Excel(name = "银行名称",width = 18)
    private String bankName;
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
    @Excel(name = "公司编码",width = 18)
    private String companyCode;
    @Excel(name = "公司名称",width = 18)
    private String companyName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
