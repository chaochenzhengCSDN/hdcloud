package com.hodo.iiot.group2.hdcloud.bank.account.currency.entity;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 分账表
 * @date 2017-04-21 15:02:33
 */
public class HdFenListEntity implements java.io.Serializable {
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 存放成员单位id
     */
    private String memberCompany;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 金额
     */
    private String money;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMemberCompany() {
        return memberCompany;
    }

    public void setMemberCompany(String memberCompany) {
        this.memberCompany = memberCompany;
    }
}
