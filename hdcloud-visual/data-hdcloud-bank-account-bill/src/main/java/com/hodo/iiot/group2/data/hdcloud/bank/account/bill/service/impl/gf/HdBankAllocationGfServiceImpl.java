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

        com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.gf;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankAllocation;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.mapper.gf.HdBankAllocationGfMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.HdBankAllocationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@Service
public class HdBankAllocationGfServiceImpl extends ServiceImpl<HdBankAllocationGfMapper, HdBankAllocation> implements HdBankAllocationService {
    @Override
    public List<String> selectDistinctString(String str, String param, String val, Integer tenantId) {
        return baseMapper.selectDistinctString(str, param, val, tenantId);
    }
}
