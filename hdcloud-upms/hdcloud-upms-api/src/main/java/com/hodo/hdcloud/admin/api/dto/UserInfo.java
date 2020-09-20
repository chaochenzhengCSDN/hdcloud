/*
 *
 *      Copyright (c) 2018-2025, hodo All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: 江苏红豆工业互联网有限公司
 *
 */

package com.hodo.hdcloud.admin.api.dto;

import com.hodo.hdcloud.admin.api.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hodo
 * @date 2017/11/11
 */
@Data
@ApiModel(value = "用户信息")
public class UserInfo implements Serializable {
	/**
	 * 用户基本信息
	 */
	@ApiModelProperty(value = "用户基本信息")
	private SysUser sysUser;
	/**
	 * 权限标识集合
	 */
	@ApiModelProperty(value = "权限标识集合")
	private String[] permissions;
	/**
	 * 角色集合
	 */
	@ApiModelProperty(value = "角色标识集合")
	private Integer[] roles;
}
