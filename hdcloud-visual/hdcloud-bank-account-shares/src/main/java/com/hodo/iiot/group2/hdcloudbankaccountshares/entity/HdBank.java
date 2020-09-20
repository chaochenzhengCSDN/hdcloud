package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author Mr.AG
 * @email 463540703@qq.com
 * @version 2019-07-04 10:58:47
 */
@Table(name = "HD_BANK")
public class HdBank implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
	@GeneratedValue(generator = "UUID")
    private String id;
	
	    //银行类型编码
	@Excel(name = "银行编码")
    @Column(name = "code")
    private String code;
	
	    //银行类型名称
	@Excel(name = "银行名称")
    @Column(name = "name")
    private String name;
	
	    //租户id
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //删除标示
    @Column(name = "deleteflag")
    private String deleteflag;
	
	    //创建人
	//@Excel(name = "负责人(请输入登录账号)")
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	@Transient
	private String value;

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
	 * 设置：银行类型编码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：银行类型编码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：银行类型名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：银行类型名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：租户id
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租户id
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：删除标示
	 */
	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}
	/**
	 * 获取：删除标示
	 */
	public String getDeleteflag() {
		return deleteflag;
	}
	/**
	 * 设置：创建人
	 */
	public String getCrtUserId() {
		return crtUserId;
	}

	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
