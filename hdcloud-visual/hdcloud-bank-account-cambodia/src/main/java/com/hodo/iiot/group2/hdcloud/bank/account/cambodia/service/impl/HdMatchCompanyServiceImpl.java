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
package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdMatchCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdMatchCompanyService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Service
public class HdMatchCompanyServiceImpl extends CommonServiceImpl<HdMatchCompany> implements HdMatchCompanyService {

    @Override
    public R page(Map<String, Object> params) {
        Map<String,Object> result = new HashedMap<>();
        List<HdMatchCompany> hdMatchCompanyList = remoteBaseService.pageMatchCompany(params);
        Map<String,Object> otherVal = remoteBaseService.getMatchCompanyOtherVal(params);
        if(hdMatchCompanyList!=null&&hdMatchCompanyList.size()>0){
            result.put("records",hdMatchCompanyList);
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
    public R getById(Serializable id) {
        return remoteBaseService.getMatchCompanyByComId(id);
    }

    @Override
    public R updateById(HdMatchCompany entity) {
        return remoteBaseService.updateMatchCompanyById(entity);
    }

    @Override
    public R save(HdMatchCompany entity) {
        return remoteBaseService.addMatchCompany(entity);
    }

    @Override
    public R removeById(Serializable id) {
        return remoteBaseService.removeMatchCompanyById(id);
    }
}
