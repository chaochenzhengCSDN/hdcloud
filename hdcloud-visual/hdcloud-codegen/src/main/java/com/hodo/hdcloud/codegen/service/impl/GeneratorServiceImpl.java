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

package com.hodo.hdcloud.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.codegen.entity.GenConfig;
import com.hodo.hdcloud.codegen.entity.GenFormConf;
import com.hodo.hdcloud.codegen.mapper.GenFormConfMapper;
import com.hodo.hdcloud.codegen.mapper.GeneratorMapper;
import com.hodo.hdcloud.codegen.service.GeneratorService;
import com.hodo.hdcloud.codegen.util.GenUtils;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author hodo
 * @date 2018-07-30
 * <p>
 * 代码生成器
 */
@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
	private final GeneratorMapper generatorMapper;
	private final GenFormConfMapper genFormConfMapper;

	/**
	 * 分页查询表
	 *
	 * @param tableName 查询条件
	 * @param id
	 * @return
	 */
	@Override
	public IPage<List<Map<String, Object>>> getPage(Page page, String tableName, Integer id) {
		DynamicDataSourceContextHolder.setDataSourceType(id);
		return generatorMapper.queryList(page, tableName);
	}

	/**
	 * 生成代码
	 *
	 * @param genConfig 生成配置
	 * @return
	 */
	@Override
	public byte[] generatorCode(GenConfig genConfig) {
		//根据tableName 查询最新的表单配置
		List<GenFormConf> formConfList = genFormConfMapper.selectList(Wrappers.<GenFormConf>lambdaQuery()
				.eq(GenFormConf::getTableName, genConfig.getTableName())
				.orderByDesc(GenFormConf::getCreateTime));

		DynamicDataSourceContextHolder.setDataSourceType(genConfig.getId());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		String tableNames = genConfig.getTableName();
		for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			if (CollUtil.isNotEmpty(formConfList)) {
				GenUtils.generatorCode(genConfig, table, columns, zip, formConfList.get(0));
			} else {
				GenUtils.generatorCode(genConfig, table, columns, zip, null);
			}
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

	private Map<String, String> queryTable(String tableName) {
		return generatorMapper.queryTable(tableName);
	}

	private List<Map<String, String>> queryColumns(String tableName) {
		return generatorMapper.queryColumns(tableName);
	}
}
