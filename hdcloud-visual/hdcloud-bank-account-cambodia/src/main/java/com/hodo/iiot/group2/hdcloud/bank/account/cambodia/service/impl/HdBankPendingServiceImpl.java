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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankPendingService;
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
public class HdBankPendingServiceImpl extends CommonServiceImpl<HdBankPending> implements HdBankPendingService {

    @Override
    public R page(Map<String, Object> params) {
        Map<String,Object> result = new HashedMap<>();
        List<HdBankPending> hdBankPendingList = remoteBillService.pageBankPending(params);
        Map<String,Object> otherVal = remoteBillService.getBankPendingOtherVal(params);
        if(hdBankPendingList!=null&&hdBankPendingList.size()>0){
            result.put("records",hdBankPendingList);
        }else{
            result.put("records",new ArrayList<>());
        }
        if(otherVal!=null&&otherVal.size()>0){
            result.putAll(otherVal);
        }else{
            result.put("total",0);
            result.put("pay",0.00);
            result.put("income",0.00);
        }
        return R.ok(result);
    }

    @Override
    public R<HdBankPending> getById(Serializable id) {
        return remoteBillService.getBankPendingById(id);
    }

    @Override
    public R updateById(HdBankPending entity) {
        return remoteBillService.updateBankPendingById(entity);
    }

    @Override
    public R save(HdBankPending entity) {
        return remoteBillService.addBankPending(entity);
    }

    @Override
    public R removeById(Serializable id) {
        return remoteBillService.removeBankPendingById(id);
    }



}
