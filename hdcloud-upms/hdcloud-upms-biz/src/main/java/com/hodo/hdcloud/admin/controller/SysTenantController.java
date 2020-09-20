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

package com.hodo.hdcloud.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.admin.api.entity.SysTenant;
import com.hodo.hdcloud.admin.api.entity.SysUser;
import com.hodo.hdcloud.admin.service.SysTenantService;
import com.hodo.hdcloud.admin.service.SysUserService;
import com.hodo.hdcloud.common.core.constant.CacheConstants;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 租户管理
 *
 * @author hodo
 * @date 2019-05-15 15:55:41
 */
@RestController
@AllArgsConstructor
@RequestMapping("/tenant")
@Api(value = "tenant", tags = "租户管理")
public class SysTenantController {

	private final SysTenantService sysTenantService;

	/**
	 * 分页查询
	 *
	 * @param page      分页对象
	 * @param sysTenant 租户
	 * @return
	 */
	@GetMapping("/page")
	public R getSysTenantPage(Page page, SysTenant sysTenant) {
		//给租户加入用户信息
		IPage<SysTenant> sysTenantPage = sysTenantService.page(sysTenantService.page(page, Wrappers.query(sysTenant)));
		if(sysTenantPage!=null){
			Object list = sysTenantPage.getRecords();
			List<SysTenant> sysTenantList = (List) list;
			if(sysTenantList!=null&&sysTenantList.size()>0){
				for(SysTenant sysTenantSingle:sysTenantList){
					Integer tenantId = sysTenantSingle.getId();
					//查询租户管理员
					QueryWrapper<SysUser> queryUser = new QueryWrapper<>();
					SysUser user = sysTenantService.getUserByTenant(
							tenantId,"admin");
					if(user!=null){
						sysTenantSingle.setUserId(user.getUserId());
						sysTenantSingle.setUserName(user.getUsername());
						sysTenantSingle.setLoginName(user.getLoginName());
					}
				}
			}
			sysTenantPage.setRecords(sysTenantList);
		}
		return R.ok(sysTenantPage);
	}


	/**
	 * 通过id查询租户
	 *
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(sysTenantService.getById(id));
	}

	/**
	 * 新增租户
	 *
	 * @param sysTenant 租户
	 * @return R
	 */
	@SysLog("新增租户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_systenant_add')")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R save(@RequestBody SysTenant sysTenant) {
		return R.ok(sysTenantService.saveTenant(sysTenant));
	}

	/**
	 * 修改租户
	 *
	 * @param sysTenant 租户
	 * @return R
	 */
	@SysLog("修改租户")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_systenant_edit')")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R updateById(@RequestBody SysTenant sysTenant) {
		return R.ok(sysTenantService.updateById(sysTenant));
	}

	/**
	 * 通过id删除租户
	 *
	 * @param id id
	 * @return R
	 */
	@SysLog("删除租户")
	@DeleteMapping("/{id}")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	@PreAuthorize("@pms.hasPermission('admin_systenant_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(sysTenantService.removeById(id));
	}

	/**
	 * 查询全部有效的租户
	 *
	 * @return
	 */
	@Inner(value = false)
	@GetMapping("/list")
	@Cacheable(value = CacheConstants.TENANT_DETAILS)
	public R list() {
		return R.ok(sysTenantService.getNormal());
	}

}
