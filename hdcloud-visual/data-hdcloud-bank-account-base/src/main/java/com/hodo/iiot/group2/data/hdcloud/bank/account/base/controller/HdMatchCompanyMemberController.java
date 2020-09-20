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
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdMatchCompanyMember;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.service.HdMatchCompanyMemberService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */

@RestController
public class HdMatchCompanyMemberController {

    @Autowired
    HdMatchCompanyMemberService hdMatchCompanyMemberService;

    /**
     * 分页查询
     *
     * @return
     */
    public R getHdMatchCompanyMemberPage( Map<String, Object> map) throws ParseException {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return R.failed("没有租户id");
        }
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdMatchCompanyMember> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return R.ok(hdMatchCompanyMemberService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<HdMatchCompanyMember> getHdMatchCompanyMemberList( Map<String, Object> map) throws ParseException {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdMatchCompanyMember> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return hdMatchCompanyMemberService.list(queryWrapper);
    }


    /**
     * 通过id查询bank_account
     *
     * @param id id
     * @return R
     */
    public R getById( String id) {
        return R.ok(hdMatchCompanyMemberService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    public HdMatchCompanyMember selectOne( Map<String, Object> map) throws ParseException {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdMatchCompanyMember> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdMatchCompanyMemberService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增bank_account
     *
     * @param hdMatchCompanyMember bank_account
     * @return R
     */
    public R save( HdMatchCompanyMember hdMatchCompanyMember) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdMatchCompanyMember.setCreateBy(createBy);
            hdMatchCompanyMember.setTenantId(tenantId);
            hdMatchCompanyMember.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        try {
            boolean b = hdMatchCompanyMemberService.save(hdMatchCompanyMember);
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
            return R.failed("新增失败");
        }

    }

    /**
     * 修改bank_account
     *
     * @param hdMatchCompanyMember bank_account
     * @return R
     */
    public R updateById( HdMatchCompanyMember hdMatchCompanyMember) {
        try {
            boolean b = hdMatchCompanyMemberService.updateById(hdMatchCompanyMember);
            if (b) {
                return R.ok(b);
            } else {
                return R.failed(b);
            }
        } catch (DuplicateKeyException e) {
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
        return R.ok(hdMatchCompanyMemberService.removeById(id));
    }

    public R removeByIds( List<String> list) {
        try {
            return R.ok(hdMatchCompanyMemberService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("删除失败");
        }

    }

    /**
     * 通过实体类删除
     */
    public R removeById( Map<String, Object> map) throws ParseException {
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
        return R.ok(hdMatchCompanyMemberService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    public R addList( List<HdMatchCompanyMember> list) {
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
            return R.failed("没有用户信息");
        }
        for (HdMatchCompanyMember hdMatchCompanyMember :
                list) {
            hdMatchCompanyMember.setCreateBy(createBy);
            hdMatchCompanyMember.setTenantId(tenantId);
            hdMatchCompanyMember.setCreateTime(new Date());
        }

        boolean b = hdMatchCompanyMemberService.saveBatch(list);
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
        List list = hdMatchCompanyMemberService.getHdTymatchCompanyList(companyId, String.valueOf((pageInt - 1) * limitInt), limit, tenantId, params);
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

        return hdMatchCompanyMemberService.getAllTymatchCompany(companyId, tenantId, params);

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
        String total = hdMatchCompanyMemberService.otherVal(companyId, tenantId, params);
        map.put("total", Integer.parseInt(total));
        return map;
    }


    public List getAllCompanyEx(String company_id,
                                String customer_name, String bank_id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return new ArrayList();
            }
        } else {
            return new ArrayList();
        }

        return hdMatchCompanyMemberService.getAllCompanyEx(company_id, customer_name, tenantId, bank_id);

    }

}
