package com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = -2786301994259082323L;
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    private String id;
    @Excel(
            name = "*账户"
    )
    private String username;
    @Ignore
    private String password;
    @Excel(
            name = "*姓名"
    )
    private String name;
    private String birthday;
    private String address;
    @Column(
            name = "mobile_phone"
    )
    private String mobilePhone;
    @Column(
            name = "tel_phone"
    )
    private String telPhone;
    private String email;
    @Excel(
            name = "性别"
    )
    private String sex;
    private String type;
    @Excel(
            name = "描述"
    )
    private String description;
    @Column(
            name = "crt_user_name"
    )
    private String crtUserName;
    @Column(
            name = "crt_user_id"
    )
    private String crtUserId;
    @Column(
            name = "crt_time"
    )
    private Date crtTime;
    @Column(
            name = "upd_user_name"
    )
    private String updUserName;
    @Column(
            name = "upd_user_id"
    )
    private String updUserId;
    @Column(
            name = "upd_time"
    )
    private Date updTime;
    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private String attr5;
    private String attr6;
    private String attr7;
    private String attr8;
    @Column(
            name = "is_deleted"
    )
    private String isDeleted;
    @Column(
            name = "is_disabled"
    )
    private String isDisabled;
    @Excel(
            name = "*部门"
    )
    @Column(
            name = "depart_id"
    )
    private String departId;
    @Column(
            name = "is_super_admin"
    )
    private String isSuperAdmin;
    @Column(
            name = "tenant_id"
    )
    private String tenantId;

    public User() {
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getTelPhone() {
        return this.telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrtUserName() {
        return this.crtUserName;
    }

    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    public String getCrtUserId() {
        return this.crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    public Date getCrtTime() {
        return this.crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getUpdUserName() {
        return this.updUserName;
    }

    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    public String getUpdUserId() {
        return this.updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public Date getUpdTime() {
        return this.updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getAttr1() {
        return this.attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return this.attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return this.attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return this.attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public String getAttr5() {
        return this.attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public String getAttr6() {
        return this.attr6;
    }

    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    public String getAttr7() {
        return this.attr7;
    }

    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    public String getAttr8() {
        return this.attr8;
    }

    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    public String getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsDisabled() {
        return this.isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getDepartId() {
        return this.departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getIsSuperAdmin() {
        return this.isSuperAdmin;
    }

    public void setIsSuperAdmin(String isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}

