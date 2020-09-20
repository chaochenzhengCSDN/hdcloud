package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(value = "com_detail")
public class ComDetailStatistics {
    private static final long serialVersionUID = 1L;
    //到账日期
    @ApiModelProperty(value="到账日期")
    @Excel(name = "到账日期",width = 12)
    private String synAccountDate;
    //记账日期
    @ApiModelProperty(value="记账日期")
    @Excel(name = "记账日期",width = 12)
    private String accountDate;
    //摘要
    @ApiModelProperty(value="摘要")
    @Excel(name = "摘要",width = 40)
    private String remark;
    //贷方金额
    @ApiModelProperty(value="收入金额")
    @Excel(name = "收入金额",width = 15,type = 10)
    //@Excel(name = "收入金额",numFormat ="#,##0.00",width = 15)
    private BigDecimal income;
    //借方金额
    @ApiModelProperty(value="支出金额")
    @Excel(name = "支出金额",width = 15,type = 10)
    //@Excel(name = "支出金额",numFormat ="#,##0.00",width = 15)
    private BigDecimal pay;

        //余额
        @ApiModelProperty(value="期末余额")
    @Excel(name = "期末余额",width = 15,type = 10)
    //@Excel(name = "期末余额",numFormat ="#,##0.00",width = 15)
    private BigDecimal balance;
    //新增明细对方单位
    @ApiModelProperty(value="对方单位名称")
    @Excel(name = "对方单位名称",width = 30)
    private String subject;
    //银行
//    @Excel(name = "银行")
//    private String bank;
    @ApiModelProperty(value="银行编码")
    @Excel(name = "银行编码",width = 15)
    private String bankCode;
    @ApiModelProperty(value="银行账号")
    @Excel(name = "银行账号",width = 22)
    private String bankAccount;
    @ApiModelProperty(value="银行名称")
    @Excel(name = "银行名称",width = 35)
    private String bankName;

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

    public String getSynAccountDate() {
        return synAccountDate;
    }

    public void setSynAccountDate(String synAccountDate) {
        this.synAccountDate = synAccountDate;
    }
}
