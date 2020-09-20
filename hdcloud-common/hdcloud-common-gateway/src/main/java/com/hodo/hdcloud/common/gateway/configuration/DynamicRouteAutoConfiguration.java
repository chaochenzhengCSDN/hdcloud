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

package com.hodo.hdcloud.common.gateway.configuration;

import com.hodo.hdcloud.common.core.constant.CacheConstants;
import com.hodo.hdcloud.common.gateway.support.RouteCacheHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author hodo
 * @date 2018/11/5
 * <p>
 * 动态路由配置类
 */
@Slf4j
@Configuration
@ComponentScan("com.hodo.hdcloud.common.gateway")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class DynamicRouteAutoConfiguration {
	/**
	 * 配置文件设置为空
	 * redis 加载为准
	 *
	 * @return
	 */
//	@Bean
//	public PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator() {
//		return new PropertiesRouteDefinitionLocator(new GatewayProperties());
//	}

	/**
	 * redis 监听配置
	 *
	 * @param redisConnectionFactory redis 配置
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container
				= new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener((message, bytes) -> {
			log.warn("接收到重新JVM 重新加载路由事件");
			RouteCacheHolder.removeRouteList();
	}, new ChannelTopic(CacheConstants.ROUTE_JVM_RELOAD_TOPIC));
		return container;
	}
}
