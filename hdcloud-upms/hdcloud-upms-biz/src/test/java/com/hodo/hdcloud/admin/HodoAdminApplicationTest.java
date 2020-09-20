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

package com.hodo.hdcloud.admin;

import cn.hutool.core.date.DateUtil;
import com.hodo.hdcloud.common.core.constant.CacheConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hodo
 * @date 2018/10/7
 * <p>
 * 加解密单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HodoAdminApplicationTest {
	@Autowired
	private  CacheManager cacheManager;

	@Test
	public void testJasypt() {
		 DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

		String date = DateUtil.date().toDateStr();

		LocalDateTime dateTime = LocalDateTime.parse("2019-06-11",formatter);

		System.out.println(dateTime);
	}

	@Test
	public void clearUser(){
		cacheManager.getCache(CacheConstants.USER_DETAILS+"::admin").clear();
	}
}
