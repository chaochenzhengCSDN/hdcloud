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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdCompany;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdCompanyService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Service
public class HdCompanyServiceImpl extends CommonServiceImpl<HdCompany> implements HdCompanyService {

    @Override
    public R<HdCompany> page(Map<String, Object> params) {

        return remoteBaseService.pageCompany(params);
    }

    @Override
    public R<HdCompany> getById(Serializable id) {
        return remoteBaseService.getCompanyById(id);
    }

    @Override
    public R updateById(HdCompany entity) {
        return remoteBaseService.updateCompanyById(entity);
    }

    @Override
    public R save(HdCompany entity) {
        return remoteBaseService.addCompany(entity);
    }

    @Override
    public R removeById(Serializable id) {
        //判断公司下是否有账单
        List<HashMap> hdBankAccounts =  remoteBaseService.getAccountsByComId(id);
        if(hdBankAccounts!=null&&hdBankAccounts.size()>0){
            return R.failed("某公司下有账单信息,删除会导致统计错误！");
        }
       return remoteBaseService.removeCompanyById(id);

    }
}
