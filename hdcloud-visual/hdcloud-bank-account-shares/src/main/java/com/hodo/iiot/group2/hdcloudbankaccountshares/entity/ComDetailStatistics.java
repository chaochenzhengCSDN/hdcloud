package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComDetailStatistics {
    private static final long serialVersionUID = 1L;
    //到账日期
    @Excel(name = "到账日期",width = 12)
    private String synAccountDate;
    //记账日期
    @Excel(name = "记账日期",width = 12)
    private String accountDate;
    //摘要
    @Excel(name = "摘要",width = 40)
    private String remark;
    //贷方金额
    @Excel(name = "收入金额",numFormat ="#,##0.00",width = 15)
    private BigDecimal income;
    //借方金额
    @Excel(name = "支出金额",numFormat ="#,##0.00",width = 15)
    private BigDecimal pay;

        //余额
    @Excel(name = "期末余额",numFormat ="#,##0.00",width = 15)
    private BigDecimal balance;
    //新增明细对方单位
    @Excel(name = "对方单位名称",width = 30)
    private String subject;
    //银行
//    @Excel(name = "银行")
//    private String bank;
    @Excel(name = "银行编码",width = 15)
    private String bankCode;
    @Excel(name = "银行账号",width = 22)
    private String bankAccount;
    @Excel(name = "银行名称",width = 35)
    private String bankName;

    private String flag;
    private String no;

}
