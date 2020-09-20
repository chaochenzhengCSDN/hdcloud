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
package com.hodo.iiot.group2.data.hdcloud.cash.account.bill.service.impl.cambodia;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.entity.HdCashStatement;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.mapper.cambodia.HdCashStatementCambodiaMapper;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.service.HdCashStatementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@Service
public class HdCashStatementCambodiaServiceImpl extends ServiceImpl<HdCashStatementCambodiaMapper, HdCashStatement>
        implements HdCashStatementService {
    public List<String> selectDistinctString(String str, String param, String val, Integer tenantId) {
        return baseMapper.selectDistinctString(str, param, val, tenantId);
    }

    @Override
    public List<HdCashStatement> selectList(String startTime, String endTime, String remark, String unitId,
                                            String subjects, Integer page, Integer limit, Integer tenantId) {
        return baseMapper.selectList(startTime,endTime,remark,unitId,subjects,page,limit,tenantId);
    }

    @Override
    public List<HdCashStatement> selectAll(String startTime, String endTime, String remark, String unitId,
                                           String subjects, Integer tenantId) {
        return baseMapper.selectAll(startTime,endTime,remark,unitId,subjects,tenantId);
    }

    @Override
    public Map<String,Object> selectTotal(String startTime, String endTime, String remark, String unitId, String subjects,
                                          Integer tenantId) {
        return baseMapper.selectTotal(startTime,endTime,remark,unitId,subjects,tenantId);
    }

    @Override
    public String selectMaxNo(Integer tenantId) {
        return baseMapper.selectMaxNo(tenantId);
    }

    @Override
    public String selectMaxNoByDate(Integer tenantId) {
        return baseMapper.selectMaxNoByDate(tenantId);
    }

    @Override
    public List<HdCashStatement> selectStatistics(Integer tenantId) {
        return baseMapper.selectStatistics(tenantId);
    }
}
