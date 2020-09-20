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

package com.hodo.iiot.group2.data.hdcloud.cash.account.bill.controller.cambodia;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.entity.HdCashStatement;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.service.impl.cambodia.HdCashStatementCambodiaServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.util.jjUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@RestController
@RequestMapping("/hdcashstatement")
public class HdCashStatementCambodiaController {

    @Autowired
    HdCashStatementCambodiaServiceImpl hdCashStatementService;


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdCashStatementPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Integer tenantId = TenantContextHolder.getTenantId();
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        int current = 1;
        int size = 10;
        if (pagestr != null) {
            current = Integer.parseInt(pagestr);
        }
        if (limitstr != null) {
            size = Integer.parseInt(limitstr);
        }
        QueryWrapper<HdCashStatement> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByDesc("create_time");
        return R.ok(hdCashStatementService.page(new Page(current, size), queryWrapper));
    }


    /**
     * 通过id查询
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(hdCashStatementService.getById(id));
    }

    /**
     * 新增
     *
     * @param hdCashStatement
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('base_hdcashstatement_add')")
    public R save(@RequestBody HdCashStatement hdCashStatement) {
        return R.ok(hdCashStatementService.save(hdCashStatement));
    }

    /**
     * 修改
     *
     * @param hdCashStatement
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('base_hdcashstatement_edit')")
    public R updateById(@RequestBody HdCashStatement hdCashStatement) {
        return R.ok(hdCashStatementService.updateById(hdCashStatement));
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('base_hdcashstatement_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(hdCashStatementService.removeById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @GetMapping("/selectOne")
    public HdCashStatement selectOne(@RequestParam Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdCashStatement> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdCashStatementService.getOne(queryWrapper);
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
        return hdCashStatementService.selectDistinctString(str, param, val, tenantId);

    }

    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdcashaccount_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        try {
            return R.ok(hdCashStatementService.removeByIds(list));
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
        return R.ok(hdCashStatementService.remove(wrapper));
    }


    /**
     * 批量插入
     *
     * @return
     */
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdcashaccount_import')")
    public R addList(@RequestBody List<HdCashStatement> list) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        Integer createById;
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
        for (HdCashStatement hdbankaccount : list) {
            hdbankaccount.setCreateBy(createBy);
            hdbankaccount.setTenantId(tenantId);
            hdbankaccount.setCreateById(createById);
        }

        boolean b = hdCashStatementService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }
    }

    @GetMapping("/page2")
    public List<HdCashStatement> selectList(String startTime, String endTime, String remark, String unitId,
                                            String subjects, Integer page, Integer limit) {
        return hdCashStatementService.selectList(startTime, endTime, remark, unitId, subjects, page, limit,
                TenantContextHolder.getTenantId());
    }

    @GetMapping("/all2")
    public List<HdCashStatement> selectAll(String startTime, String endTime, String remark, String unitId,
                                           String subjects) {
        return hdCashStatementService.selectAll(startTime, endTime, remark, unitId, subjects,
                TenantContextHolder.getTenantId());
    }

    @GetMapping("/other")
    public Map<String,Object> selectTotal(String startTime, String endTime, String remark, String unitId, String subjects) {

        return hdCashStatementService.selectTotal(startTime, endTime, remark, unitId, subjects,
                TenantContextHolder.getTenantId());
    }

    @GetMapping("/selectMaxNo")
    public String selectMaxNo() {
        return hdCashStatementService.selectMaxNo(TenantContextHolder.getTenantId());
    }

    @GetMapping("/selectMaxNoByDate")
    public String selectMaxNoByDate() {
        return hdCashStatementService.selectMaxNoByDate(TenantContextHolder.getTenantId());
    }

    /**
     * 根据单位获取统计数据
     * @return
     */
    @GetMapping("/getUnitList")
    public List<HdCashStatement> selectStatistics() {
        return hdCashStatementService.selectStatistics(TenantContextHolder.getTenantId());
    }
}
