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

import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankPendingService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@Service
public class HdBankPendingServiceImpl implements HdBankPendingService {
    @Autowired
    private RemoteBillService remoteBillService;
    @Autowired
    private RemoteBaseService remoteBaseService;
    public R page(Map<String,Object> params) {
        Map<String,Object> result = new HashedMap();
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
    //查询id
    public R getById(Serializable id){
        return remoteBillService.getBankPendingById(id);
    }
    //编辑
    public R updateById(HdBankPending entity){
        return remoteBillService.updateBankPendingById(entity);
    }
    //增加
    public R save(HdBankPending entity){
        return remoteBillService.addBankPending(entity);
    }

    //删除
    public R removeById(Serializable id){
        return remoteBillService.removeBankPendingById(id);
    }

    //分账划账加入公司信息
    public void addMemberCom(String memberComId,HdBankStatement hdBankStatement){
        if(StringUtil.isNotEmpty(memberComId)){
            //根据id查询成员单位
            HdCompanyMember hdCompanyMember = remoteBaseService.getMemberCompanyById(memberComId).getData();
            if(hdCompanyMember!=null){
                hdBankStatement.setCompanyId(hdCompanyMember.getCompanyId());
                hdBankStatement.setCompanyMemberId(memberComId);
            }
        }
    }

    //导出加入银行账户附加信息
    public List<HdBankPending> addBankAccount(List<HdBankPending> hdBankPendingList){
        if(hdBankPendingList!=null&&hdBankPendingList.size()>0){
            for(HdBankPending hdBankPending:hdBankPendingList){
                hdBankPending = addAccountInfo(hdBankPending);
            }
        }
        return hdBankPendingList;
    }

    private HdBankPending addAccountInfo(HdBankPending hdBankPending){
        String accountId = hdBankPending.getBankAccountId();
        if(StringUtil.isNotEmpty(accountId)){
            HdBankAccount hdBankAccount = remoteBaseService.getBankAccountById(accountId).getData();
            if(hdBankAccount!=null){
                hdBankPending.setBankAccount(hdBankAccount.getExternalBankAccountId());
                hdBankPending.setBankName(hdBankAccount.getBankName());
            }
        }
        return hdBankPending;
    }





}
