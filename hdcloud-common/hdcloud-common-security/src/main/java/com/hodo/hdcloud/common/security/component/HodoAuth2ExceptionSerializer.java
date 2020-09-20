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

package com.hodo.hdcloud.common.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hodo.hdcloud.common.core.constant.CommonConstants;
import com.hodo.hdcloud.common.security.exception.HodoAuth2Exception;
import lombok.SneakyThrows;

/**
 * @author hodo
 * @date 2018/11/16
 * <p>
 * OAuth2 异常格式化
 */
public class HodoAuth2ExceptionSerializer extends StdSerializer<HodoAuth2Exception> {
	public HodoAuth2ExceptionSerializer() {
		super(HodoAuth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(HodoAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", CommonConstants.FAIL);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}
}
