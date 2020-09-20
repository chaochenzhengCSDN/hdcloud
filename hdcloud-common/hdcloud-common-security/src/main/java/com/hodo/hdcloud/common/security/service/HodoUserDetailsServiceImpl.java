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

package com.hodo.hdcloud.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.hodo.hdcloud.admin.api.dto.UserInfo;
import com.hodo.hdcloud.admin.api.entity.SysUser;
import com.hodo.hdcloud.admin.api.feign.RemoteUserService;
import com.hodo.hdcloud.common.core.constant.CacheConstants;
import com.hodo.hdcloud.common.core.constant.CommonConstants;
import com.hodo.hdcloud.common.core.constant.SecurityConstants;
import com.hodo.hdcloud.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详细信息
 *
 * @author hodo
 */
@Slf4j
@Service
@AllArgsConstructor
public class HodoUserDetailsServiceImpl implements HodoUserDetailsService {
	private final RemoteUserService remoteUserService;
	private final CacheManager cacheManager;

	/**
	 * 用户密码登录
	 *
	 * @param loginName 登录名
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String loginName) {
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (cache != null && cache.get(loginName) != null) {
			return (HodoUser) cache.get(loginName).get();
		}

		R<UserInfo> result = remoteUserService.info(loginName, SecurityConstants.FROM_IN);
		UserDetails userDetails = getUserDetails(result);
		cache.put(loginName, userDetails);
		return userDetails;
	}


	/**
	 * 根据社交登录code 登录
	 *
	 * @param inStr TYPE@CODE
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserBySocial(String inStr) {
		return getUserDetails(remoteUserService.social(inStr, SecurityConstants.FROM_IN));
	}

	/**
	 * 构建userdetails
	 *
	 * @param result 用户信息
	 * @return
	 */
	private UserDetails getUserDetails(R<UserInfo> result) {
		if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		UserInfo info = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}
		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();
		boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);
		// 构造security用户

		return new HodoUser(user.getUserId(), user.getDeptId(), user.getTenantId(),user.getLoginName(), SecurityConstants.BCRYPT + user.getPassword(), enabled,
				true, true, !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
	}
}
