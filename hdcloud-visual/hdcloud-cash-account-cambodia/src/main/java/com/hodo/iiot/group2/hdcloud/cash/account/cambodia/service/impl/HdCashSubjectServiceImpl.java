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
package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashSubject;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.HdCashSubjectService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@Service
public class HdCashSubjectServiceImpl extends CommonServiceImpl<HdCashSubject> implements HdCashSubjectService {

    @Override
    public R<HdCashSubject> page(Map<String, Object> params) {

        return remoteBaseService.pageCashRemark(params);
    }

    @Override
    public R<HdCashSubject> getById(Serializable id) {

        return remoteBaseService.getCashSubjectById(id);
    }

    @Override
    public R updateById(HdCashSubject entity) {

        return remoteBaseService.updateCashSubjectById(entity);
    }

    @Override
    public R save(HdCashSubject entity) {

        return remoteBaseService.addCashSubject(entity);
    }

    @Override
    public R removeById(Serializable id) {

        return remoteBaseService.removeCashSubjectById(id);
    }
}
