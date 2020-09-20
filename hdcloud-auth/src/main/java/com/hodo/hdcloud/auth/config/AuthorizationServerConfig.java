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

package com.hodo.hdcloud.auth.config;

import cn.hutool.core.util.StrUtil;
import com.hodo.hdcloud.common.core.constant.SecurityConstants;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.security.component.HodoWebResponseExceptionTranslator;
import com.hodo.hdcloud.common.security.service.HodoClientDetailsService;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.hdcloud.common.security.service.HodoUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hodo
 * @date 2018/6/22
 * 认证服务器配置
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	private final DataSource dataSource;
	private final HodoUserDetailsService hdcloudUserDetailsService;
	private final AuthenticationManager authenticationManagerBean;
	private final RedisConnectionFactory redisConnectionFactory;

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		HodoClientDetailsService clientDetailsService = new HodoClientDetailsService(dataSource);
		clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
		clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
		clients.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer
				.allowFormAuthenticationForClients()
				.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancer())
				.userDetailsService(hdcloudUserDetailsService)
				.authenticationManager(authenticationManagerBean)
				.reuseRefreshTokens(false)
				.pathMapping("/oauth/confirm_access", "/token/confirm_access")
				.exceptionTranslator(new HodoWebResponseExceptionTranslator());
	}


	@Bean
	public TokenStore tokenStore() {
		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
		tokenStore.setPrefix(SecurityConstants.PIGX_PREFIX + SecurityConstants.OAUTH_PREFIX);
		tokenStore.setAuthenticationKeyGenerator(new DefaultAuthenticationKeyGenerator() {
			@Override
			public String extractKey(OAuth2Authentication authentication) {
				return super.extractKey(authentication) + StrUtil.COLON + TenantContextHolder.getTenantId();
			}
		});
		return tokenStore;
	}

	/**
	 * token增强，客户端模式不增强。
	 *
	 * @return TokenEnhancer
	 */
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return (accessToken, authentication) -> {
			if (SecurityConstants.CLIENT_CREDENTIALS
					.equals(authentication.getOAuth2Request().getGrantType())) {
				return accessToken;
			}

			final Map<String, Object> additionalInfo = new HashMap<>(8);
			HodoUser hdcloudUser = (HodoUser) authentication.getUserAuthentication().getPrincipal();
			additionalInfo.put(SecurityConstants.DETAILS_USER_ID, hdcloudUser.getId());
			additionalInfo.put(SecurityConstants.DETAILS_USERNAME, hdcloudUser.getUsername());
			additionalInfo.put(SecurityConstants.LOGIN_NAME, hdcloudUser.getLoginName());
			additionalInfo.put(SecurityConstants.DETAILS_DEPT_ID, hdcloudUser.getDeptId());
			additionalInfo.put(SecurityConstants.DETAILS_TENANT_ID, hdcloudUser.getTenantId());
			additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PIGX_LICENSE);
			additionalInfo.put(SecurityConstants.ACTIVE, Boolean.TRUE);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			return accessToken;
		};
	}
}
