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
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:22
 */
@Service
public class HdBankAccountServiceImpl extends CommonServiceImpl<HdBankAccount> implements HdBankAccountService {

    public R<HdBankAccount> page(Map<String,Object> params) {

        return remoteBaseService.pageBankAccount(params);
    }

    //查询id
    public R getById(Serializable id){

        return remoteBaseService.getBankAccountById(id);
    }
    //编辑
    public R updateById(HdBankAccount entity){
        return remoteBaseService.updateBankAccountById(entity);
    }
    //增加
    public R save(HdBankAccount entity){
        return remoteBaseService.saveBankAccount(entity);
    }

    //删除
    public R removeById(Serializable id){
        return remoteBaseService.removeBankAccountById(id);
    }






}
