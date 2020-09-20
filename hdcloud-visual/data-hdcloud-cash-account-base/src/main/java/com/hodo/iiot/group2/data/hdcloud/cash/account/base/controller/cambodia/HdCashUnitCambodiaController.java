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

package com.hodo.iiot.group2.data.hdcloud.cash.account.base.controller.cambodia;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.cash.account.base.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.cash.account.base.entity.HdCashUnit;
import com.hodo.iiot.group2.data.hdcloud.cash.account.base.service.impl.cambodia.HdCashUnitCambodiaServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.cash.account.base.util.jjUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@RestController
@RequestMapping("/hdcashunit" )
public class HdCashUnitCambodiaController {

    @Autowired
    HdCashUnitCambodiaServiceImpl hdCashUnitService;


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getHdCashUnitPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Integer tenantId = TenantContextHolder.getTenantId();
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        int current =1;
        int size = 10;
        if(pagestr != null){
            current = Integer.parseInt(pagestr);
        }
        if(limitstr != null){
            size = Integer.parseInt(limitstr);
        }
        QueryWrapper<HdCashUnit> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return R.ok(hdCashUnitService.page(new Page(current,size), queryWrapper));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return R.ok(hdCashUnitService.getById(id));
    }

    /**
     * 新增
     * @param hdCashUnit 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('base_hdcashunit_add')" )
    public R save(@RequestBody HdCashUnit hdCashUnit) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return R.ok(hdCashUnitService.save(hdCashUnit));
    }

    /**
     * 修改
     * @param hdCashUnit 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('base_hdcashunit_edit')" )
    public R updateById(@RequestBody HdCashUnit hdCashUnit) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return R.ok(hdCashUnitService.updateById(hdCashUnit));
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('base_hdcashunit_del')" )
    public R removeById(@PathVariable Integer id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return R.ok(hdCashUnitService.removeById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @GetMapping("/selectOne")
    public HdCashUnit selectOne(@RequestParam Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdCashUnit> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdCashUnitService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询单个字段
     *
     * @return
     */
    @GetMapping("/selectStringList")
    public List<String> selectDistinctString(String str, String param, String val) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList();
        }
        return hdCashUnitService.selectDistinctString(str, param, val, tenantId);

    }

    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdcashaccount_batch_del')")
    public R removeByIds(@RequestBody  List<String> list) {
        try {
            return R.ok(hdCashUnitService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("删除失败");
        }

    }

    /**
     * 通过实体类删除
     */
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdcashaccount_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        QueryWrapper wrapper = new QueryWrapper();
        Integer tenantId;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        wrapper.eq("tenant_id", tenantId);
        jjUtil.handleAllParamsToWrapper(map, wrapper);
        return R.ok(hdCashUnitService.remove(wrapper));
    }


    /**
     * 批量插入
     *
     * @return
     */
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdcashaccount_import')")
    public R addList(@RequestBody  List<HdCashUnit> list) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId ;
        String createBy ;
        Integer createById ;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            createById = hodoUser.getId();
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("没有用户信息");
        }
        for (HdCashUnit hdbankaccount : list) {
            hdbankaccount.setCreateBy(createBy);
            hdbankaccount.setTenantId(tenantId);
            hdbankaccount.setCreateById(createById);
        }

        boolean b = hdCashUnitService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }


    }


}
