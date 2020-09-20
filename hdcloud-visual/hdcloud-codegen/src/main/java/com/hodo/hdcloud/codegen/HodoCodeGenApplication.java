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

package com.hodo.hdcloud.codegen;

import com.hodo.hdcloud.common.datasource.annotation.EnableDynamicDataSource;
import com.hodo.hdcloud.common.security.annotation.EnableHodoFeignClients;
import com.hodo.hdcloud.common.security.annotation.EnableHodoResourceServer;
import com.hodo.hdcloud.common.swagger.annotation.EnableHodoSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author hodo
 * @date 2018/07/29
 * 代码生成模块
 */
@EnableDynamicDataSource
@EnableHodoSwagger2
@SpringCloudApplication
@EnableHodoFeignClients
@EnableHodoResourceServer
public class HodoCodeGenApplication {
	public static void main(String[] args) {
		SpringApplication.run(HodoCodeGenApplication.class, args);
	}
}
