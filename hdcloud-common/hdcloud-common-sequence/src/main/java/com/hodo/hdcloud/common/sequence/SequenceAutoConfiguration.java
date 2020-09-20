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

package com.hodo.hdcloud.common.sequence;

import com.hodo.hdcloud.common.sequence.builder.DbSeqBuilder;
import com.hodo.hdcloud.common.sequence.builder.RedisSeqBuilder;
import com.hodo.hdcloud.common.sequence.builder.SnowflakeSeqBuilder;
import com.hodo.hdcloud.common.sequence.properties.SequenceDbProperties;
import com.hodo.hdcloud.common.sequence.properties.SequenceRedisProperties;
import com.hodo.hdcloud.common.sequence.properties.SequenceSnowflakeProperties;
import com.hodo.hdcloud.common.sequence.range.impl.name.DateBizName;
import com.hodo.hdcloud.common.sequence.range.impl.name.DefaultBizName;
import com.hodo.hdcloud.common.sequence.sequence.Sequence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author hodo
 * @date 2019-05-26
 */
@Configuration
@ComponentScan("com.hodo.hdcloud.common.sequence")
@ConditionalOnMissingBean(Sequence.class)
public class SequenceAutoConfiguration {

	/**
	 * 数据库作为发号器的存储介质
	 *
	 * @param dataSource
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnBean(SequenceDbProperties.class)
	public Sequence dbSequence(DataSource dataSource,
							   SequenceDbProperties properties) {
		return DbSeqBuilder
				.create()
				.bizName(new DefaultBizName(properties.getBizName()))
				.dataSource(dataSource)
				.step(properties.getStep())
				.retryTimes(properties.getRetryTimes())
				.tableName(properties.getTableName())
				.build();
	}

	/**
	 * Redis 作为发号器的存储介质
	 *
	 * @param redisProperties
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnBean(SequenceRedisProperties.class)
	public Sequence redisSequence(RedisProperties redisProperties,
								  SequenceRedisProperties properties) {
		return RedisSeqBuilder
				.create()
				.bizName(new DateBizName(properties.getBizName()))
				.ip(redisProperties.getHost())
				.port(redisProperties.getPort())
				.auth(redisProperties.getPassword())
				.step(properties.getStep())
				.build();
	}

	/**
	 * snowflak 算法作为发号器实现
	 *
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnBean(SequenceSnowflakeProperties.class)
	public Sequence snowflakeSequence(SequenceSnowflakeProperties properties) {
		return SnowflakeSeqBuilder
				.create()
				.datacenterId(properties.getDatacenterId())
				.workerId(properties.getWorkerId())
				.build();
	}
}