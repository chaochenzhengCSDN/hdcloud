package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;

public class BankDetailStatistics {
    private static final long serialVersionUID = 1L;
    //到账日期
    @Excel(name = "到账日期",width = 12)
    private String synAccountDate;
    //记账日期
    @Excel(name = "记账日期",width = 12)
    private String accountDate;
    //摘要
    @Excel(name = "摘要",width = 50)
    private String remark;
    //贷方金额
    //@Excel(name = "收入金额",numFormat ="#,##0.00",width = 15)
    @Excel(name = "收入金额",width = 15,type = 10)
    private BigDecimal income;
    //借方金额
    @Excel(name = "支出金额",width = 15,type = 10)
    //@Excel(name = "支出金额",numFormat ="#,##0.00",width = 15)
    private BigDecimal pay;

        //余额
    @Excel(name = "期末余额",width = 15,type = 10)
    //@Excel(name = "期末余额",numFormat ="#,##0.00",width = 15)
    private BigDecimal balance;
    //新增对方单位
    @Excel(name="对方单位名称",width = 35)
    private String subject;
    @Excel(name="公司编码",width = 10)
    private String companyCode;
    @Excel(name="公司名称",width = 18)
    private String companyName;
    private String flag;
    private String no;
    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getPay() {
        return pay;
    }

    public void setPay(BigDecimal pay) {
        this.pay = pay;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSynAccountDate() {
        return synAccountDate;
    }

    public void setSynAccountDate(String synAccountDate) {
        this.synAccountDate = synAccountDate;
    }
}
