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
import com.hodo.hdcloud.admin.api.entity.SysFile;
import com.hodo.hdcloud.common.core.util.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
public interface SysFileService extends IService<SysFile> {

	/**
	 * 上传文件
	 *
	 * @param file
	 * @return
	 */
	R uploadFile(MultipartFile file);

	/**
	 * 读取文件
	 *
	 * @param fileName
	 * @param response
	 */
	void getFile(String fileName, HttpServletResponse response);

	/**
	 * 删除文件
	 *
	 * @param id
	 * @return
	 */
	Boolean deleteFile(Long id);
}
