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

        com.hodo.iiot.group2.hdcloudbankaccountshares.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.Date;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Data
@TableName("hd_company")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "bank_account")
public class HdCompany extends Model<HdCompany> {
private static final long serialVersionUID=1L;

/**
 * 
 */
@TableId(type = IdType.UUID)
@ApiModelProperty(value = "")
private String id;
/**
 * 公司名
 */
@Excel(name = "公司名",width = 18)
@ApiModelProperty(value = "公司名")
private String name;
/**
 * 租户id
 */
@ApiModelProperty(value = "租户id", hidden =true)
private Integer tenantId;
/**
 * 公司编号
 */
@Excel(name = "公司编号",width = 18)
@ApiModelProperty(value = "公司编号")
private String code;
/**
 * 创建时间
 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
private Date createTime;
/**
 * 创建人
 */
    @ApiModelProperty(value = "创建人")
private String createBy;

    @Transient
    private String value;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
