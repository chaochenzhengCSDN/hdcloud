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
package

        com.hodo.iiot.group2.data.hdcloud.bank.account.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdMatchCompanyMember;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.mapper.HdMatchCompanyMemberMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.service.HdMatchCompanyMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Service
public class HdMatchCompanyMemberServiceImpl extends ServiceImpl<HdMatchCompanyMemberMapper, HdMatchCompanyMember> implements HdMatchCompanyMemberService {
    @Override
    public List<String> selectDistinctString(String str, String param, String val, Integer tenantId) {
        return baseMapper.selectDistinctString(str,param,val,tenantId);
    }

    public List<HdMatchCompanyMember> getHdTymatchCompanyList(String companyId, String page, String limit,
                                                          Integer tenantId, Map<String, Object> params) {
        return baseMapper.getHdTymatchCompanyList(companyId, page, limit, tenantId, params);
    }

    public List<HdMatchCompanyMember> getAllTymatchCompany(String companyId, Integer tenantId, Map<String, Object> params) {
        return baseMapper.getAllTymatchCompany(companyId, tenantId, params);
    }

    public List<HdMatchCompanyMember> getAllCompanyEx(String companyId, String customerName, Integer tenantId,String bankId) {
        return baseMapper.getAllCompanyEx(companyId,customerName,tenantId,bankId);
    }

    public String otherVal(String companyId, Integer tenantId, Map<String, Object> params) {
        return baseMapper.otherVal(companyId, tenantId, params);
    }
}
