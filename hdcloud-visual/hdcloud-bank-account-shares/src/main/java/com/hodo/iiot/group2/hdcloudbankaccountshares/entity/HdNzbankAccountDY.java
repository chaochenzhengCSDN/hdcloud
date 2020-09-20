package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 
 * 
 * @author Mr.AG
 * @email 463540703@qq.com
 * @version 2019-06-24 10:56:43
 */
public class HdNzbankAccountDY implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private String id;
	
	    //日期
//    @Column(name = "ACCOUNT_DATE")
//    private Date accountDate;
	//打印抬头名称就是租户名称
	private String headName;
	    //凭证编号
    private BigDecimal no;
	
	//借方公司
	@Excel(name = "收入公司")

    private String incomesubject;
	//贷方公司
	@Excel(name = "支出公司")

	private String paysubject;


	    //摘要
	@Excel(name = "摘要")

    private String remark;
	
	    //收入金额
	@Excel(name = "交易金额",numFormat ="#,##0.00")
    private BigDecimal money;

	    //创建时间
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
	
	    //创建人登录名称

    private String crtUserId;
	
	    //流水号
	@Excel(name = "流水号")

    private String streamno;
	
	    //单据编号
	@Excel(name = "单据编号")

    private String billno;
	
	    //租户
    private String tenantId;
	
	    //开户行,开户内账抬头
	@Excel(name = "开户行")

    private String bankno;
	
	    //备注1
	@Excel(name = "凭证编号(导入无须填写)")
    private String by1;

    private String by2;
	private String incomesubjectNo;
	private String paysubjectNo;
	private String incomesubjectId;
	private String paysubjectId;
	private String banknoId;
    private List<Character> moneyChar;
	private String bankWbno;
	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：日期
	 */
//	public void setAccountDate(Date accountDate) {
//		this.accountDate = accountDate;
//	}
	/**
	 * 获取：日期
	 */
//	public Date getAccountDate() {
//		return accountDate;
//	}
	/**
	 * 设置：凭证编号
	 */
	public void setNo(BigDecimal no) {
		this.no = no;
	}
	/**
	 * 获取：凭证编号
	 */
	public BigDecimal getNo() {
		return no;
	}
	/**
	 * 设置：借方科目
	 */
	public void setIncomesubject(String incomesubject) {
		this.incomesubject = incomesubject;
	}
	/**
	 * 获取：借方科目
	 */
	public String getIncomesubject() {
		return incomesubject;
	}
	/**
	 * 设置：摘要
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：摘要
	 */
	public String getRemark() {
		return remark;
	}
//	/**
//	 * 设置：收入金额
//	 */
//	public void setIncome(BigDecimal income) {
//		this.income = income;
//	}
//	/**
//	 * 获取：收入金额
//	 */
//	public BigDecimal getIncome() {
//		return income;
//	}
//	/**
//	 * 设置：付出金额
//	 */
//	public void setPay(BigDecimal pay) {
//		this.pay = pay;
//	}
//	/**
//	 * 获取：付出金额
//	 */
//	public BigDecimal getPay() {
//		return pay;
//	}


	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	/**
	 * 设置：贷方科目
	 */
	public void setPaysubject(String paysubject) {
		this.paysubject = paysubject;
	}
	/**
	 * 获取：贷方科目
	 */
	public String getPaysubject() {
		return paysubject;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建人登录名称
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：创建人登录名称
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：流水号
	 */
	public void setStreamno(String streamno) {
		this.streamno = streamno;
	}
	/**
	 * 获取：流水号
	 */
	public String getStreamno() {
		return streamno;
	}
	/**
	 * 设置：单据编号
	 */
	public void setBillno(String billno) {
		this.billno = billno;
	}
	/**
	 * 获取：单据编号
	 */
	public String getBillno() {
		return billno;
	}
	/**
	 * 设置：租户
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租户
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：开户行
	 */
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	/**
	 * 获取：开户行
	 */
	public String getBankno() {
		return bankno;
	}
	/**
	 * 设置：备注1
	 */
	public void setBy1(String by1) {
		this.by1 = by1;
	}
	/**
	 * 获取：备注1
	 */
	public String getBy1() {
		return by1;
	}
	/**
	 * 设置：备注2
	 */
	public void setBy2(String by2) {
		this.by2 = by2;
	}
	/**
	 * 获取：备注2
	 */
	public String getBy2() {
		return by2;
	}

	public List<Character> getMoneyChar() {
		return moneyChar;
	}

	public void setMoneyChar(List<Character> moneyChar) {
		this.moneyChar = moneyChar;
	}

	public String getIncomesubjectId() {
		return incomesubjectId;
	}

	public void setIncomesubjectId(String incomesubjectId) {
		this.incomesubjectId = incomesubjectId;
	}

	public String getPaysubjectId() {
		return paysubjectId;
	}

	public void setPaysubjectId(String paysubjectId) {
		this.paysubjectId = paysubjectId;
	}

	public String getBanknoId() {
		return banknoId;
	}

	public void setBanknoId(String banknoId) {
		this.banknoId = banknoId;
	}

	public String getIncomesubjectNo() {
		return incomesubjectNo;
	}

	public void setIncomesubjectNo(String incomesubjectNo) {
		this.incomesubjectNo = incomesubjectNo;
	}

	public String getPaysubjectNo() {
		return paysubjectNo;
	}

	public void setPaysubjectNo(String paysubjectNo) {
		this.paysubjectNo = paysubjectNo;
	}

	public String getBankWbno() {
		return bankWbno;
	}

	public void setBankWbno(String bankWbno) {
		this.bankWbno = bankWbno;
	}

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}
}
