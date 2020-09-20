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
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdBankAccount;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.service.HdBankAccountService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.util.jjUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2019-10-29 15:28:22
 */
@RestController
public class HdBankAccountController {

    @Autowired
    HdBankAccountService hdBankAccountService;

    /**
     * 分页查询
     *
     * @return
     */
    public R getHdBankAccountPage( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return R.failed("没有租户id");
        }

        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAccount> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("bank_code");
        return R.ok(hdBankAccountService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<HdBankAccount> getHdBankAccountList(Map<String, Object> map) {

        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList();
        }
        jjUtil.handleParams(map, "limit");
        jjUtil.handleParams(map, "page");
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAccount> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("bank_code");
        return hdBankAccountService.list(queryWrapper);
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<HdBankAccount> getHdBankAccountList2( Map<String, Object> map) {
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAccount> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.orderByDesc("bank_code");
        return hdBankAccountService.list(queryWrapper);
    }

    /**
     * 查一条
     *
     * @return
     */
    public HdBankAccount selectOne( Map<String, Object> map) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAccount> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdBankAccountService.getOne(queryWrapper);
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
    public List<String> selectDistinctString(String str, String param, String val) {
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList();
        }
        return hdBankAccountService.selectDistinctString(str, param, val, tenantId);

    }


    /**
     * 通过id查询bank_account
     *
     * @param id id
     * @return R
     */
    public R getById( String id) {
        return R.ok(hdBankAccountService.getById(id));
    }

    /**
     * 新增bank_account
     *
     * @param hdBankAccount bank_account
     * @return R
     */
    public R save( HdBankAccount hdBankAccount) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdBankAccount.setCreateBy(createBy);
            hdBankAccount.setTenantId(tenantId);
            hdBankAccount.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }


        boolean b = hdBankAccountService.save(hdBankAccount);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed(b + "");
        }

    }

    /**
     * 修改bank_account
     *
     * @param hdBankAccount bank_account
     * @return R
     */
    public R updateById( HdBankAccount hdBankAccount) {
        boolean b = hdBankAccountService.updateById(hdBankAccount);
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
        return R.ok(hdBankAccountService.removeById(id));
    }


    public R removeByIds( List<String> list) {
        try {
            return R.ok(hdBankAccountService.removeByIds(list));
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
        return R.ok(hdBankAccountService.remove(wrapper));
    }


    /**
     * 批量插入
     *
     * @return
     */
    public R addList( List<HdBankAccount> list) {
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
        for (HdBankAccount hdbankaccount : list) {
            hdbankaccount.setCreateBy(createBy);
            hdbankaccount.setTenantId(tenantId);
            hdbankaccount.setCreateTime(new Date());
        }

        boolean b = hdBankAccountService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }


    }


}
