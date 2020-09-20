package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

//弃用
@Data
public class BankComStatistics {
    private static final long serialVersionUID = 1L;
    @Excel(name = "银行编码",width = 18)
    private String bankCode;
    @Excel(name = "银行账号",width = 18)
    private String bankAccount;
    @Excel(name = "银行名称",width = 18)
    private String bankName;
    @Excel(name="期初金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal beginBalance;
    @Excel(name="收入金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentIncome;
    @Excel(name="支出金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentPay;
    @Excel(name="期末余额",numFormat ="#,##0.00",width = 18)
    private BigDecimal endBalance;
    @Excel(name = "公司编码",width = 18)
    private String companyCode;
    @Excel(name = "公司名称",width = 18)
    private String companyName;


}
