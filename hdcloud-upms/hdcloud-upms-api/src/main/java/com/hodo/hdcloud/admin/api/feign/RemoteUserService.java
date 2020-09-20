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

package com.hodo.hdcloud.admin.api.feign;

import com.hodo.hdcloud.admin.api.dto.UserInfo;
import com.hodo.hdcloud.admin.api.entity.SysUser;
import com.hodo.hdcloud.common.core.constant.SecurityConstants;
import com.hodo.hdcloud.common.core.constant.ServiceNameConstants;
import com.hodo.hdcloud.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author hodo
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteUserService {

	/**
	 * 通过登录名查询用户、角色信息
	 *
	 * @param loginName 登录名
	 * @param from     调用标志
	 * @return R
	 */
	@GetMapping("/user/info/{loginName}")
	R<UserInfo> info(@PathVariable("loginName") String loginName
			, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过社交账号或手机号查询用户、角色信息
	 *
	 * @param inStr appid@code
	 * @param from  调用标志
	 * @return
	 */
	@GetMapping("/social/info/{inStr}")
	R<UserInfo> social(@PathVariable("inStr") String inStr
			, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 查询上级部门的用户信息
	 *
	 * @param loginName 用户名
	 * @return R
	 */
	@GetMapping("/user/ancestor/{loginName}")
	R<List<SysUser>> ancestorUsers(@PathVariable("loginName") String loginName);
}
