package com.hodo.iiot.group2.hdcloud.bank.account.currency.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.math.BigDecimal;

public class TyBankDetailStatistics {
    private static final long serialVersionUID = 1L;


    //到账日期
    @Excel(name = "到账日期",width = 18)
    private String synAccountDate;


    //记账日期
    @Excel(name = "记账日期",width = 18)
    private String accountDate;


    //新增对方单位
    @Excel(name="对方单位名称",width = 18)
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


    @Excel(name="公司编码",width = 18)
    private String superCompCode;

    @Excel(name="公司名称",width = 18)
    private String superCompName;

    @Excel(name = "成员单位编码",width = 18)
    private String code;

    @Excel(name = "成员单位名称",width = 18)
    private String name;

    private String flag;

    private String no;

    private String by2;


    public String getBy2() {
        return by2;
    }

    public void setBy2(String by2) {
        this.by2 = by2;
    }

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


    public String getSuperCompCode() {
        return superCompCode;
    }

    public void setSuperCompCode(String superCompCode) {
        this.superCompCode = superCompCode;
    }

    public String getSuperCompName() {
        return superCompName;
    }

    public void setSuperCompName(String superCompName) {
        this.superCompName = superCompName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
