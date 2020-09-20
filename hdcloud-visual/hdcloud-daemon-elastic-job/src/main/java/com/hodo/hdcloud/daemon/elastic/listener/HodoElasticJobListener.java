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

package com.hodo.hdcloud.daemon.elastic.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

/**
 * @author hodo
 * @date 2018/7/24
 * 任务监听器
 */
public class HodoElasticJobListener implements ElasticJobListener {

	@Override
	public void beforeJobExecuted(ShardingContexts shardingContexts) {
		System.out.println(shardingContexts.getJobName() + " | MyElasticJobListener beforeJobExecuted");
	}

	@Override
	public void afterJobExecuted(ShardingContexts shardingContexts) {
		System.out.println(shardingContexts.getJobName() + " | MyElasticJobListener afterJobExecuted");
	}
}
