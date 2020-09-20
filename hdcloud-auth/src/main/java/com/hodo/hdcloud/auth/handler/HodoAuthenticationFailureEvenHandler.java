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

package com.hodo.hdcloud.auth.handler;

import com.hodo.hdcloud.common.security.handler.AbstractAuthenticationFailureEvenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author hodo
 * @date 2018/10/8
 */
@Slf4j
@Component
public class HodoAuthenticationFailureEvenHandler extends AbstractAuthenticationFailureEvenHandler {

	/**
	 * 处理登录失败方法
	 * <p>
	 *
	 * @param authenticationException 登录的authentication 对象
	 * @param authentication          登录的authenticationException 对象
	 */
	@Override
	public void handle(AuthenticationException authenticationException, Authentication authentication) {
		log.info("用户：{} 登录失败，异常：{}", authentication.getPrincipal(), authenticationException.getLocalizedMessage());
	}
}
