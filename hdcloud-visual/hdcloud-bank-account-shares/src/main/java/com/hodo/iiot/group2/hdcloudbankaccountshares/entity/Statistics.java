package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistics {

    private String companyId;
    @Excel(name="公司编码" ,width = 18)
    private String companyCode;
    @Excel(name="公司名称",width = 18)
    private String companyName;
    @Excel(name="期初金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal beginBalance;
    @Excel(name="收入金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentIncome;
    @Excel(name="支出金额",numFormat ="#,##0.00",width = 18)
    private BigDecimal currentPay;
    @Excel(name="期末余额",numFormat ="#,##0.00",width = 18)
    private BigDecimal endBalance;
    private String tenantId;
    private String headName;
    //打印加入截止时间
    //截止时间
    private String endTime;
    //操作人
    private String userName;

}
