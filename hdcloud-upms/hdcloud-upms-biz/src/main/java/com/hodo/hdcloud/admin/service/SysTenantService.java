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

package com.hodo.hdcloud.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hodo.hdcloud.admin.api.entity.SysTenant;
import com.hodo.hdcloud.admin.api.entity.SysUser;

import java.util.List;

/**
 * 租户管理
 *
 * @author hodo
 * @date 2019-05-15 15:55:41
 */
public interface SysTenantService extends IService<SysTenant> {

	/**
	 * 获取正常的租户
	 *
	 * @return
	 */
	List<SysTenant> getNormal();

	/**
	 * 保存租户
	 *
	 * @param sysTenant
	 * @return
	 */
	Boolean saveTenant(SysTenant sysTenant);

	//获取租户对应用户
	SysUser getUserByTenant(Integer tenant,String loginName);

}
