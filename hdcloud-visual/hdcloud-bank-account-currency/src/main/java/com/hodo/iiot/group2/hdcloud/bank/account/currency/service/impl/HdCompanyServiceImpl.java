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
package com.hodo.iiot.group2.hdcloud.bank.account.currency.service.impl;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdCompanyService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Service
public class HdCompanyServiceImpl implements HdCompanyService {
    @Autowired
    private RemoteBaseService remoteBaseService;
    @Override
    public R<HdCompany> page(Map<String, Object> params) {
        return remoteBaseService.pageCompany(params);
    }

    @Override
    public R<HdCompany> getById(Serializable id) {
        return remoteBaseService.getCompanyById(id);
    }


    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R updateById(HdCompany entity) {
        //更新成员单位的公司code
        Map<String,Object> params = new HashedMap();
        params.put("company_id",entity.getId());
        List<HdCompanyMember> hdCompanyMemberList = remoteBaseService.getAllMemberCompany(params);
        if(hdCompanyMemberList.size()>0){
            for(HdCompanyMember hdCompanyMember:hdCompanyMemberList){
                hdCompanyMember.setCompanyCode(entity.getCode());
                hdCompanyMember.setCompanyName(entity.getName());
                hdCompanyMember.setCompanyId(entity.getId());
                remoteBaseService.updateMemberCompanyById(hdCompanyMember);
            }
        }

        return remoteBaseService.updateCompanyById(entity);
    }

    @Override
    public R save(HdCompany entity) {
        return remoteBaseService.addCompany(entity);
    }

    @Override
    public R removeById(Serializable id) {
        List<HashMap> hdBankAccounts =  remoteBaseService.getAccountsByComId(id);
        if(hdBankAccounts!=null&&hdBankAccounts.size()>0){
            return R.failed("某公司下有账单信息,删除会导致统计错误！");
        }
        Map params = new HashMap();
        params.put("companyId",id);
        //根据公司id查询是否有子公司
        List<HdCompanyMember> hdCompanyMembers = remoteBaseService.getAllMemberCompany(params);
        long total  = hdCompanyMembers.size();
        if(total != 0){
            return R.failed("某公司下有成员单位,不能删除！");
        }
        remoteBaseService.removeCompanyById(id);
        return R.ok("删除成功");
    }
}
