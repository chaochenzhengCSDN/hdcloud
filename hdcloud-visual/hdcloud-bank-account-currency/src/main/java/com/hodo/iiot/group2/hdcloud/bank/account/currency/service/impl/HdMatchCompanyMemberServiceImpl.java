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
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdMatchCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdMatchCompanyMemberService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Service
public class HdMatchCompanyMemberServiceImpl extends CommonServiceImpl<HdMatchCompanyMember>  implements HdMatchCompanyMemberService {

    @Override
    public R page(Map<String, Object> params) {
        Map<String,Object> result = new HashedMap();
        List<HdMatchCompanyMember> hdMatchCompanyMemberList = remoteBaseService.pageMatchCompanyMember(params);
        Map<String,Object> otherVal = remoteBaseService.getMatchCompanyMemberOtherVal(params);
        if(hdMatchCompanyMemberList!=null&&hdMatchCompanyMemberList.size()>0){
            result.put("records",hdMatchCompanyMemberList);
        }else{
            result.put("records",new ArrayList<>());
        }
        if(otherVal!=null&&otherVal.size()>0){
            result.putAll(otherVal);
        }else{
            result.put("total",0);
        }
        return R.ok(result);
    }

    @Override
    public R<HdMatchCompanyMember> getById(Serializable id) {
        R<HdMatchCompanyMember> result = remoteBaseService.getMemberMatchCompanyByComId(id);
        HdMatchCompanyMember hdMatchCompanyMember = result.getData();
        if(hdMatchCompanyMember!=null){
            //獲取成員單位id
            String memberCompanyId = hdMatchCompanyMember.getCompanyMemberId();
            HdCompanyMember hdCompanyMember = remoteBaseService.getMemberCompanyById(memberCompanyId).getData();
            if(hdCompanyMember!=null){
                hdMatchCompanyMember.setCompanyId(hdCompanyMember.getCompanyId());
            }
            result.setData(hdMatchCompanyMember);
        }

        return  result;
    }

    @Override
    public R updateById(HdMatchCompanyMember entity) {

        return remoteBaseService.updateMatchCompanyMemberById(entity);
    }

    @Override
    public R save(HdMatchCompanyMember entity) {

        return remoteBaseService.addMatchCompanyMember(entity);
    }

    @Override
    public R removeById(Serializable id) {

        return remoteBaseService.removeMatchCompanyMemberById(id);
    }
}
