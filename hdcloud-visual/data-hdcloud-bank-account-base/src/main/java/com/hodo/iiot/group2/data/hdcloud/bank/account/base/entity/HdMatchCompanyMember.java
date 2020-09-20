/*
 *    Copyright (c) 2018-2025, hodo All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: 江苏红豆工业互联网有限公司
 */

package

        com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Data
@TableName("hd_match_company_member")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_account")
public class HdMatchCompanyMember extends Model<HdMatchCompanyMember> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private Integer createById;
    /**
     * 成员单位id
     */
    @ApiModelProperty(value = "成员单位id")
    private String companyMemberId;
    /**
     * 对方单位名称
     */
    @ApiModelProperty(value = "对方单位名称")
    private String customerName;
    /**
     * 外部账号id
     */
    @ApiModelProperty(value = "外部账号id")
    private String bankAccountIdExternal;
    /**
     * 备用
     */
    @ApiModelProperty(value = "备用")
    private String by1;
    /**
     * 备用2
     */
    @ApiModelProperty(value = "备用2")
    private String by2;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id", hidden = true)
    private Integer tenantId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private String companyMemberName;

    @TableField(exist = false)
    private String companyMemberCode;

//    @TableField(exist = false)
//    private String externalBankAccountId;
}
