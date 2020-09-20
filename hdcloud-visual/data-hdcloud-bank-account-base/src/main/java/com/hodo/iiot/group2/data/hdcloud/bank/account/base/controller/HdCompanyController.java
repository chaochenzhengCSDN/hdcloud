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

package

        com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdCompany;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.service.HdCompanyService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.util.jjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
public class HdCompanyController {

    @Autowired
    HdCompanyService hdCompanyService;

    /**
     * 分页查询
     *
     * @return
     */

    public R getHdCompanyPage( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return R.failed("tenantId为空");
        }
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdCompany> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByDesc("create_time");
        return R.ok(hdCompanyService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */

    public List<HdCompany> getHdCompanyList( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList();
        }
        DynamicDataSourceContextHolder.setDataSourceType(9);
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdCompany> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        jjUtil.handleParamsClean(map);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByDesc("code", "create_time");
        List<HdCompany> list = hdCompanyService.list(queryWrapper);
        return list;
    }


    /**
     * 通过id查询bank_account
     *
     * @param id id
     * @return R
     */

    public R getById( String id) {
        return R.ok(hdCompanyService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    public HdCompany selectOne( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdCompany> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdCompanyService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增bank_account
     *
     * @param hdCompany bank_account
     * @return R
     */
    public R save( HdCompany hdCompany) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            Integer tenantId = hodoUser.getTenantId();
            hdCompany.setCreateBy(hodoUser.getUsername());
            hdCompany.setTenantId(tenantId);
            hdCompany.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        boolean b = hdCompanyService.save(hdCompany);
        if (b) {
            return R.ok(b + "");
        } else {
            return R.failed(b + "");
        }


    }

    /**
     * 修改bank_account
     *
     * @param hdCompany bank_account
     * @return R
     */
    public R updateById( HdCompany hdCompany) {
        try {
            boolean b = hdCompanyService.updateById(hdCompany);
            if (b) {
                return R.ok(b + "");
            } else {
                return R.failed(b + "");
            }
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return R.failed("有重复数据");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("修改失败");
        }
    }

    /**
     * 通过id删除bank_account
     *
     * @param id id
     * @return R
     */
    public R removeById( String id) {
        return R.ok(hdCompanyService.removeById(id));
    }


    public R removeByIds( List<String> list) {
        try {
            return R.ok(hdCompanyService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

    }

    /**
     * 通过实体类删除
     */
    public R removeById( Map<String, Object> map) {
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
        return R.ok(hdCompanyService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    public R addList( List<HdCompany> list) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        for (HdCompany hdCompany :
                list) {
            hdCompany.setCreateBy(createBy);
            hdCompany.setTenantId(tenantId);
            hdCompany.setCreateTime(new Date());
        }
        boolean b = hdCompanyService.saveBatch(list);
        if (b) {
            return R.ok(b);
        } else {
            return R.failed(b);
        }
    }
}
