package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author Mr.AG
 * @email 463540703@qq.com
 * @version 2019-08-20 14:16:22
 */
@Table(name = "HD_LOAN_ACCOUNT")
public class HdLoanAccountDetailXls implements Serializable {
	private static final long serialVersionUID = 1L;


    //金额
    @Excel(name = "金额",numFormat ="#,##0.00",width = 18)
    @Column(name = "MONEY")
    private String money;
	    //贷款日
	@Excel(name = "贷款日(1999-01-01)",format = "yyyy-MM-dd",width = 18)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "LOANDATE")
    private Date loandate;
	
	    //到账日
	@Excel(name = "到账日(1999-01-01)",format = "yyyy-MM-dd",width = 18)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "DEADDATE")
    private Date deaddate;



    //借账方式
    @Excel(name = "借账方式",width = 18)
    @Column(name = "METHOD")
    private String method;

	//利率
	@Excel(name = "利率(%)",width = 18)
	@Column(name = "RATE")
	private String rate;


	/**
	 * 设置：贷款日
	 */
	public void setLoandate(Date loandate) {
		this.loandate = loandate;
	}
	/**
	 * 获取：贷款日
	 */
	public Date getLoandate() {
		return loandate;
	}
	/**
	 * 设置：到账日
	 */
	public void setDeaddate(Date deaddate) {
		this.deaddate = deaddate;
	}
	/**
	 * 获取：到账日
	 */
	public Date getDeaddate() {
		return deaddate;
	}
	/**
	 * 设置：金额
	 */
	public void setMoney(String money) {
		this.money = money;
	}
	/**
	 * 获取：金额
	 */
	public String getMoney() {
		return money;
	}
	/**
	 * 设置：借账方式
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：借账方式
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：利率
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}
	/**
	 * 获取：利率
	 */
	public String getRate() {
		return rate;
	}

}
