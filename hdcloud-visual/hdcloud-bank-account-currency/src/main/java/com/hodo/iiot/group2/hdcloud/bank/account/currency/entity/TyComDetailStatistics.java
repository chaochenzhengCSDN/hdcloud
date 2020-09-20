package com.hodo.iiot.group2.hdcloud.bank.account.currency.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;

public class TyComDetailStatistics {
    private static final long serialVersionUID = 1L;

    //到账日期
    @Excel(name = "到账日期",width = 18)
    private String synAccountDate;

    //记账日期
    @Excel(name = "记账日期",width = 18)
    private String accountDate;

    //新增明细对方单位
    @Excel(name = "对方单位",width = 18)
    private String subject;

    //摘要
    @Excel(name = "摘要",width = 18)
    private String remark;
    //贷方金额
    @Excel(name = "本期收入",numFormat ="#,##0.00",width = 18)
    private BigDecimal income;
    //借方金额
    @Excel(name = "本期支出",numFormat ="#,##0.00",width = 18)
    private BigDecimal pay;

    //余额
    @Excel(name = "期末余额",numFormat ="#,##0.00",width = 18)
    private BigDecimal balance;

    //银行
//    @Excel(name = "银行")
//    private String bank;
    @Excel(name = "银行编码",width = 18)
    private String bankCode;
    @Excel(name = "银行账号",width = 18)
    private String bankAccount;
    @Excel(name = "银行名称",width = 18)
    private String bankName;

    private String flag;
    private String no;



    public String getSynAccountDate() {
        return synAccountDate;
    }

    public void setSynAccountDate(String synAccountDate) {
        this.synAccountDate = synAccountDate;
    }

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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
}
