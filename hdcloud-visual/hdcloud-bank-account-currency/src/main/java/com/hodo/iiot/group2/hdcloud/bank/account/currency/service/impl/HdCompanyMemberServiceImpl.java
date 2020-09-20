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
import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdCompanyMemberService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Service
public class HdCompanyMemberServiceImpl extends CommonServiceImpl<HdCompanyMember> implements HdCompanyMemberService {

    @Override
    public R<HdCompanyMember> page(Map<String, Object> params) {
        return remoteBaseService.pageMemberCompany(params);
    }

    @Override
    public R<HdCompanyMember> getById(Serializable id) {
        return remoteBaseService.getMemberCompanyById(id);
    }

    @Override
    public R updateById(HdCompanyMember entity) {
        return remoteBaseService.updateMemberCompanyById(entity);
    }

    @Override
    public R save(HdCompanyMember entity) {
        return remoteBaseService.addMemberCompany(entity);
    }

    @Override
    public R removeById(Serializable id) {
        return remoteBaseService.removeMemberCompanyById(id);
    }
}
