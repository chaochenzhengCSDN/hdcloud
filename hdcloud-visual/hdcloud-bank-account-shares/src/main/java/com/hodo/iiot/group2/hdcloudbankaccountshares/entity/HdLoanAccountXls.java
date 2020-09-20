package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 * @author Mr.AG
 * @email 463540703@qq.com
 * @version 2019-08-20 14:16:22
 */
@Table(name = "HD_LOAN_ACCOUNT")
public class HdLoanAccountXls implements Serializable {
	private static final long serialVersionUID = 1L;

    //银行账户
    @Excel(name = "银行账户",width = 18)
    @Transient
    private String bankAccount;
	//开户行
	@Excel(name = "授信银行",width = 18)
	@Transient
	private String bankName;
	    //已批复授信金额
	@Excel(name = "已批复额度",numFormat ="#,##0.00",width = 18)
    @Column(name = "PASSED_CREDIT")
    private String passedCredit;
	
	    //已使用授信金额
	@Excel(name = "已使用授信额度",numFormat ="#,##0.00",width = 18)
    @Column(name = "USED_CREDIT")
    private String usedCredit;
	
	    //可使用授信额度
	@Excel(name = "可使用授信额度",numFormat ="#,##0.00",width = 18)
    @Column(name = "USEABLE_CREDIT")
    private String useableCredit;
	
	    //拟新增授信额度
	@Excel(name = "拟新增授信额度",numFormat ="#,##0.00",width = 18)
    @Column(name = "PLAN_CREDIT")
    private String planCredit;
	
	    //批复授信期限
	@Excel(name = "批复授信期限",width = 18)
    @Column(name = "DEADLINEREGION")
    private String deadlineregion;
	@ExcelCollection(name="贷款明细",orderNum="7")
	private List<HdLoanAccountDetailXls> detailList = new ArrayList<HdLoanAccountDetailXls>();

	public List<HdLoanAccountDetailXls> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<HdLoanAccountDetailXls> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 设置：已批复授信金额
	 */
	public void setPassedCredit(String passedCredit) {
		this.passedCredit = passedCredit;
	}
	/**
	 * 获取：已批复授信金额
	 */
	public String getPassedCredit() {
		return passedCredit;
	}
	/**
	 * 设置：已使用授信金额
	 */
	public void setUsedCredit(String usedCredit) {
		this.usedCredit = usedCredit;
	}
	/**
	 * 获取：已使用授信金额
	 */
	public String getUsedCredit() {
		return usedCredit;
	}
	/**
	 * 设置：可使用授信额度
	 */
	public void setUseableCredit(String useableCredit) {
		this.useableCredit = useableCredit;
	}
	/**
	 * 获取：可使用授信额度
	 */
	public String getUseableCredit() {
		return useableCredit;
	}
	/**
	 * 设置：拟新增授信额度
	 */
	public void setPlanCredit(String planCredit) {
		this.planCredit = planCredit;
	}
	/**
	 * 获取：拟新增授信额度
	 */
	public String getPlanCredit() {
		return planCredit;
	}
	/**
	 * 设置：批复授信期限
	 */
	public void setDeadlineregion(String deadlineregion) {
		this.deadlineregion = deadlineregion;
	}
	/**
	 * 获取：批复授信期限
	 */
	public String getDeadlineregion() {
		return deadlineregion;
	}


	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
}
