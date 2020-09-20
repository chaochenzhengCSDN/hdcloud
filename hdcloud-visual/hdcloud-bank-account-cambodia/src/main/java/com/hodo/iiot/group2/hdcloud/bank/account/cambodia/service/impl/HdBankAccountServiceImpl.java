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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankAccountService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.jjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Service
public class HdBankAccountServiceImpl extends CommonServiceImpl<HdBankAccount> implements HdBankAccountService {

    @Override
    public R page(Map<String, Object> params) {

        return remoteBaseService.pageBankAccount(params);
    }

    @Override
    public R<HdBankAccount> getById(Serializable id) {

        return remoteBaseService.getBankAccountById(id);
    }

    @Override
    public R updateById(HdBankAccount entity) {
        return remoteBaseService.updateBankAccountById(entity);
    }

    @Override
    public R save(HdBankAccount entity) {
        return remoteBaseService.saveBankAccount(entity);
    }

    @Override
    public R removeById(Serializable id) {

        return remoteBaseService.removeBankAccountById(id);
    }

    /*************************************************同步******************************************/





}
