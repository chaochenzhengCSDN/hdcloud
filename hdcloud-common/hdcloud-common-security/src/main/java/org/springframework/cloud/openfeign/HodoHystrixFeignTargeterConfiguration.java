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

package org.springframework.cloud.openfeign;

import feign.hystrix.HystrixFeign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author hodo
 * <p>
 * HystrixFeignTargeter 配置
 */
@Configuration
@ConditionalOnClass(HystrixFeign.class)
@ConditionalOnProperty("feign.hystrix.enabled")
public class HodoHystrixFeignTargeterConfiguration {

	@Bean
	@Primary
	public Targeter hdcloudFeignTargeter() {
		return new HodoHystrixTargeter();
	}
}
