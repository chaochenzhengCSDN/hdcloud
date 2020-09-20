package com.hodo.iiot.group2.hdcloudbankaccountshares.common;


import com.hodo.hdcloud.common.core.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("响应信息主体")
public class MR<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("返回标记：成功标记=0，失败标记=1")
    private int code;
    @ApiModelProperty("返回信息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;

    private Long total;
    BigDecimal sumIncome;
    BigDecimal sumPay;
    BigDecimal sumBegin;
    BigDecimal sumEnd;
    BigDecimal sumPassedCredit;
    BigDecimal sumUsedCredit;

    public static <T> MR<Object> ok() {
        return restResult((Object)null, CommonConstants.SUCCESS, (String)null);
    }

    public static <T> MR<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, (String)null);
    }

    public static <T> MR<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg);
    }

    public static <T> MR<Object> failed() {
        return restResult((Object)null, CommonConstants.FAIL, (String)null);
    }

    public static <T> MR<Object> failed(String msg) {
        return restResult((Object)null, CommonConstants.FAIL, msg);
    }

    public static <T> MR<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, (String)null);
    }

    public static <T> MR<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }

    private static <T> MR<T> restResult(T data, int code, String msg) {
        MR<T> apiResult = new MR();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public String toString() {
        return "MR(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public MR() {
    }

    public MR(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public MR(long total, T data,BigDecimal sumIncome,BigDecimal sumPay) {
        this.total = total;
        this.data = data;
        this.sumIncome = sumIncome;
        this.sumPay = sumPay;
    }

    public MR(long total, T data,BigDecimal sumIncome,BigDecimal sumPay,BigDecimal sumBegin,BigDecimal sumEnd) {
        this.total = total;
        this.data = data;
        this.sumIncome = sumIncome;
        this.sumPay = sumPay;
        this.sumBegin = sumBegin;
        this.sumEnd = sumEnd;
    }

    public MR(long total, T data,BigDecimal sumIncome,BigDecimal sumPay,BigDecimal sumBegin,BigDecimal sumEnd,
              BigDecimal sumPassedCredit,BigDecimal sumUsedCredit) {
        this.total = total;
        this.data = data;
        this.sumIncome = sumIncome;
        this.sumPay = sumPay;
        this.sumBegin = sumBegin;
        this.sumEnd = sumEnd;
        this.sumPassedCredit = sumPassedCredit;
        this.sumUsedCredit = sumUsedCredit;
    }


    public int getCode() {
        return this.code;
    }

    public MR<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public BigDecimal getSumBegin() {
        return sumBegin;
    }

    public void setSumBegin(BigDecimal sumBegin) {
        this.sumBegin = sumBegin;
    }

    public BigDecimal getSumEnd() {
        return sumEnd;
    }

    public void setSumEnd(BigDecimal sumEnd) {
        this.sumEnd = sumEnd;
    }

    public BigDecimal getSumPassedCredit() {
        return sumPassedCredit;
    }

    public void setSumPassedCredit(BigDecimal sumPassedCredit) {
        this.sumPassedCredit = sumPassedCredit;
    }

    public BigDecimal getSumUsedCredit() {
        return sumUsedCredit;
    }

    public void setSumUsedCredit(BigDecimal sumUsedCredit) {
        this.sumUsedCredit = sumUsedCredit;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public BigDecimal getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(BigDecimal sumIncome) {
        this.sumIncome = sumIncome;
    }

    public BigDecimal getSumPay() {
        return sumPay;
    }

    public void setSumPay(BigDecimal sumPay) {
        this.sumPay = sumPay;
    }

    public String getMsg() {
        return this.msg;
    }

    public MR<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public MR<T> setData(T data) {
        this.data = data;
        return this;
    }
}
