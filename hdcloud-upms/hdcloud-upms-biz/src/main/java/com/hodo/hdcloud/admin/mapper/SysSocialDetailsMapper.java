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

package com.hodo.hdcloud.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hodo.hdcloud.admin.api.entity.SysSocialDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统社交登录账号表
 *
 * @author hodo
 * @date 2018-08-16 21:30:41
 */
@Mapper
public interface SysSocialDetailsMapper extends BaseMapper<SysSocialDetails> {

}
