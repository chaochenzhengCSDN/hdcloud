package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankStatistics {
    private static final long serialVersionUID = 1L;
    //增加银行编码
    @Excel(name = "银行编码",width = 18)
    private String bankCode;
    @Excel(name = "账户类型",width = 18)
    private String bankType;
    @Excel(name = "内部账户",width = 18)
    private String nzId;
    @Excel(name = "外部账户",width = 18)
    private String wbzh;
    //对应银行
    @Excel(name = "银行名称",width = 18)
    private String bankName;
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
    //新加不需要计算所以用String
    //已批复授信金额
    @Excel(name = "已批复授信金额",width = 18)
    private BigDecimal passedCredit;
    //已使用授信金额
    @Excel(name = "已使用授信金额",width = 18)
    private BigDecimal usedCredit;

}
