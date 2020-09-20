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
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.util.jjUtil;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdMatchCompany;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.service.HdMatchCompanyService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@RestController
public class HdMatchCompanyController {

    @Autowired
    HdMatchCompanyService hdMatchCompanyService;

    /**
     * 分页查询
     *
     * @return
     */
    public R getHdMatchCompanyPage( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return R.failed("没有租户id");
        }
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdMatchCompany> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return R.ok(hdMatchCompanyService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<HdMatchCompany> getHdMatchCompanyList( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdMatchCompany> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return hdMatchCompanyService.list(queryWrapper);
    }


    /**
     * 通过id查询bank_account
     *
     * @param id id
     * @return R
     */
    public R getById(String id) {
        return R.ok(hdMatchCompanyService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    public HdMatchCompany selectOne( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdMatchCompany> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdMatchCompanyService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增bank_account
     *
     * @param hdMatchCompany bank_account
     * @return R
     */
    public R save( HdMatchCompany hdMatchCompany) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId = null;
        String createBy = null;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdMatchCompany.setCreateBy(createBy);
            hdMatchCompany.setTenantId(tenantId);
            hdMatchCompany.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        return R.ok(hdMatchCompanyService.save(hdMatchCompany));
    }

    /**
     * 修改bank_account
     *
     * @param hdMatchCompany bank_account
     * @return R
     */
    public R updateById( HdMatchCompany hdMatchCompany) {
        boolean b = hdMatchCompanyService.updateById(hdMatchCompany);
        if (b) {
            return R.ok(b);
        } else {
            return R.failed(b);
        }
    }

    /**
     * 通过id删除bank_account
     *
     * @param id id
     * @return R
     */
    public R removeById( String id) {
        return R.ok(hdMatchCompanyService.removeById(id));
    }

    public R removeByIds( List<String> list) {
        try {
            return R.ok(hdMatchCompanyService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("删除失败");
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
        return R.ok(hdMatchCompanyService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    public R addList( List<HdMatchCompany> list) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId = null;
        String createBy = null;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("没有用户信息");
        }
        for (HdMatchCompany hdMatchCompany :
                list) {
            hdMatchCompany.setCreateBy(createBy);
            hdMatchCompany.setTenantId(tenantId);
            hdMatchCompany.setCreateTime(new Date());
        }

        boolean b = hdMatchCompanyService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }


    public List getHdTymatchCompanyList( Map<String, Object> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        jjUtil.handleParamsClean(params);
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return new ArrayList();
            }
        } else {
            return new ArrayList();
        }
        String companyId = jjUtil.handleParams(params, "company_id");
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        List list = hdMatchCompanyService.getHdMatchCompanyList(companyId, String.valueOf((pageInt - 1) * limitInt), limit, tenantId, params);
        return list;

    }



    public List getAllTymatchCompany( Map<String, Object> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        jjUtil.handleParamsClean(params);
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return new ArrayList();
            }
        } else {
            return new ArrayList();
        }
        String companyId = jjUtil.handleParams(params, "company_id");
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");

        return hdMatchCompanyService.getAllMatchCompany(companyId, tenantId, params);

    }


    public Map<String, Object> other( Map<String, Object> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        Map map = new HashMap();
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return map;
            }
        } else {
            return map;
        }
        String companyId = jjUtil.handleParams(params, "company_id");
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        String total = hdMatchCompanyService.otherVal(companyId, tenantId, params);
        map.put("total", Integer.parseInt(total));
        return map;
    }

//    @ApiOperation("分页获取数据2")
//    @GetMapping(value = "/allEx")
//    public List getAllCompanyEx(String company_id,
//                                String customer_name, String bank_id) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Integer tenantId;
//        if (principal instanceof HodoUser) {
//            HodoUser hodoUser = (HodoUser) principal;
//            tenantId = hodoUser.getTenantId();
//            if (tenantId == null || tenantId < 1) {
//                return new ArrayList();
//            }
//        } else {
//            return new ArrayList();
//        }
//
//        return hdMatchCompanyService.getAllCompanyEx(company_id, customer_name, tenantId, bank_id);
//
//    }
}
