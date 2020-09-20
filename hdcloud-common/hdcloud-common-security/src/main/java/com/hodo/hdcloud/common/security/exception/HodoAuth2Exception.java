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

package com.hodo.hdcloud.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hodo.hdcloud.common.security.component.HodoAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author hodo
 * @date 2018/7/8
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = HodoAuth2ExceptionSerializer.class)
public class HodoAuth2Exception extends OAuth2Exception {
	@Getter
	private String errorCode;

	public HodoAuth2Exception(String msg) {
		super(msg);
	}

	public HodoAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
