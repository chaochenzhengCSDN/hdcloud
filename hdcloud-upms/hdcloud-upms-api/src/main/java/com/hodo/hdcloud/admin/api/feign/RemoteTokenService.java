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

package com.hodo.hdcloud.admin.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.constant.SecurityConstants;
import com.hodo.hdcloud.common.core.constant.ServiceNameConstants;
import com.hodo.hdcloud.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author hodo
 * @date 2018/9/4
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteTokenService {
	/**
	 * 分页查询token 信息
	 *
	 * @param from   内部调用标志
	 * @param params 分页参数
	 * @param from   内部调用标志
	 * @return page
	 */
	@PostMapping("/token/page")
	R<Page> getTokenPage(@RequestBody Map<String, Object> params, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 删除token
	 *
	 * @param from  内部调用标志
	 * @param token token
	 * @param from  内部调用标志
	 * @return
	 */
	@DeleteMapping("/token/{token}")
	R<Boolean> removeTokenById(@PathVariable("token") String token, @RequestHeader(SecurityConstants.FROM) String from);
}
