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
package com.hodo.hdcloud.mp.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.mp.entity.WxMpMenu;
import com.hodo.hdcloud.mp.service.WxMenuService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 微信菜单管理
 *
 * @author hodo
 * @date 2019-03-27 20:45:18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wxmenu")
public class WxMenuController {

	private final WxMenuService wxMenuService;

	/**
	 * 分页查询
	 *
	 * @param page   分页对象
	 * @param wxMpMenu
	 * @return
	 */
	@GetMapping("/page")
	public R getWxMpMenuPage(Page page, WxMpMenu wxMpMenu) {
		return R.ok(wxMenuService.page(page, Wrappers.query(wxMpMenu)));
	}


	/**
	 * 通过appid查询微信菜单
	 *
	 * @param appId 公众号
	 * @return R
	 */
	@GetMapping("/{appId}")
	public R getById(@PathVariable("appId") String appId) {
		return wxMenuService.getByAppId(appId);
	}

	/**
	 * 新增微信菜单
	 *
	 * @param wxMenu 微信菜单列表
	 * @return R
	 */
	@SysLog("新增微信菜单")
	@PostMapping("/{appId}")
	@PreAuthorize("@pms.hasPermission('mp_wxmenu_add')")
	public R save(@RequestBody JSONObject wxMenu, @PathVariable String appId) {
		return R.ok(wxMenuService.save(wxMenu, appId));
	}

	/**
	 * 发布微信菜单
	 *
	 * @param appId 公众号
	 * @return R
	 */
	@SysLog("发布微信菜单")
	@PutMapping("/{appId}")
	@PreAuthorize("@pms.hasPermission('mp_wxmenu_push')")
	public R updateById(@PathVariable String appId) {
		return wxMenuService.push(appId);
	}

	@DeleteMapping("/{appId}")
	public R delete(@PathVariable("appId") String appId) {
		return wxMenuService.delete(appId);
	}


}
