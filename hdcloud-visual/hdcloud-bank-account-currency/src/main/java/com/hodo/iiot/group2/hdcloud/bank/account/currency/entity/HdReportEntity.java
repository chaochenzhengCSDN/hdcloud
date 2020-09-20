package com.hodo.iiot.group2.hdcloud.bank.account.currency.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2019-11-15.
 */
public class HdReportEntity<T> implements Serializable {
    //塞入list
    @Getter
    @Setter
    @ApiModelProperty(value = "数据")
    private T data;
    //塞入其他值
    @Getter
    @Setter
    @ApiModelProperty(value = "本期收入")
    private BigDecimal sumIncome = BigDecimal.ZERO;
    @Getter
    @Setter
    @ApiModelProperty(value = "本期支出")
    private BigDecimal sumPay = BigDecimal.ZERO;
    @Getter
    @Setter
    @ApiModelProperty(value = "期初")
    private BigDecimal sumBegin = BigDecimal.ZERO;
    @Getter
    @Setter
    @ApiModelProperty(value = "期末")
    private BigDecimal sumEnd = BigDecimal.ZERO;
    @Getter
    @Setter
    @ApiModelProperty(value = "内转收入")
    private BigDecimal sumNzIncome = BigDecimal.ZERO;
    @Getter
    @Setter
    @ApiModelProperty(value = "内转支出")
    private BigDecimal sumNzPay = BigDecimal.ZERO;
    @Getter
    @Setter
    @ApiModelProperty(value = "合计")
    private long total = 0;
}
