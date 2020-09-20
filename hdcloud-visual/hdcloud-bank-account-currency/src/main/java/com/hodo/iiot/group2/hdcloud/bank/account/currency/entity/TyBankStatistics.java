package com.hodo.iiot.group2.hdcloud.bank.account.currency.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;


import java.math.BigDecimal;

public class TyBankStatistics {
    private static final long serialVersionUID = 1L;

//    @Excel(name = "账户类型",width = 18)
//    private String bankType;
//    @Excel(name = "内部账户",width = 18)
//    private String nzId;
//    @Excel(name = "外部账户",width = 18)
//    private String wbzh;
    //对应银行
    @Excel(name = "银行名称",width = 18)
    private String bankName;

    @Excel(name = "公司名称",width = 18)
    private String superCompName;

    @Excel(name = "成员单位名称",width = 18)
    private String name;

    //增加银行编码
    @Excel(name = "银行编码",width = 18)
    private String bankCode;

    @Excel(name = "公司编号",width = 18)
    private String superCompCode;

    @Excel(name = "成员单位编号",width = 18)
    private String code;



    //期初余额
    @Excel(name="期初金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal beginBalance;
    //本期收入
    @Excel(name="收入金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentIncome;
    //本期支出
    @Excel(name="支出金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentPay;
    //期末余额
    @Excel(name="期末余额",numFormat ="#,##0.00",width = 18)
    private BigDecimal endBalance;
    //内转收入
    @Excel(name="内转收入",numFormat ="#,##0.00",width = 18)
    private BigDecimal nzIncome;
    //内转支出
    @Excel(name="内转支出",numFormat ="#,##0.00",width = 18)
    private BigDecimal nzPay;

    private String tenantId;





//    //新加不需要计算所以用String
//    //已批复授信金额
//    @Excel(name = "已批复授信金额",width = 18)
//    private BigDecimal passedCredit;
//    //已使用授信金额
//    @Excel(name = "已使用授信金额",width = 18)
//    private BigDecimal usedCredit;


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantid) {
        this.tenantId = tenantId;
    }



    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public String getSuperCompName() {
        return superCompName;
    }

    public void setSuperCompName(String superCompName) {
        this.superCompName = superCompName;
    }

    public String getSuperCompCode() {
        return superCompCode;
    }

    public void setSuperCompCode(String superCompCode) {
        this.superCompCode = superCompCode;
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

    public BigDecimal getNzIncome() {
        return nzIncome;
    }

    public void setNzIncome(BigDecimal nzIncome) {
        this.nzIncome = nzIncome;
    }

    public BigDecimal getNzPay() {
        return nzPay;
    }

    public void setNzPay(BigDecimal nzPay) {
        this.nzPay = nzPay;
    }

    //    public String getWbzh() {
//        return wbzh;
//    }
//    public void setWbzh(String wbzh) {
//        this.wbzh = wbzh;
//    }
//
//    public String getNzId() {
//        return nzId;
//    }
//
//    public void setNzId(String nzId) {
//        this.nzId = nzId;
//    }
//
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
//
//    public String getBankType() {
//        return bankType;
//    }
//
//    public void setBankType(String bankType) {
//        this.bankType = bankType;
//    }

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

//    public BigDecimal getPassedCredit() {
//        return passedCredit;
//    }
//
//    public void setPassedCredit(BigDecimal passedCredit) {
//        this.passedCredit = passedCredit;
//    }
//
//    public BigDecimal getUsedCredit() {
//        return usedCredit;
//    }
//
//    public void setUsedCredit(BigDecimal usedCredit) {
//        this.usedCredit = usedCredit;
//    }
}
