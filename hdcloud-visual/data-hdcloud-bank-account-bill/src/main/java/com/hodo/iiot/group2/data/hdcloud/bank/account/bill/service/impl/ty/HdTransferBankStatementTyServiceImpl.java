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

        com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.ty;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.mapper.ty.HdTransferBankStatementTyMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.HdTransferBankStatementService;
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
public class HdTransferBankStatementTyServiceImpl extends ServiceImpl<HdTransferBankStatementTyMapper, HdTransferBankStatement> implements HdTransferBankStatementService {
    @Override
    public List<String> selectDistinctString(String str, String param, String val, Integer tenantId) {
        return baseMapper.selectDistinctString(str,param,val,tenantId);
    }

//    @Override
//    public BigDecimal selectMaxNo(Integer tenantId) {
//        return baseMapper.selectMaxNo(tenantId);
//    }


    public List<HdTransferBankStatement> getTynzbankAccountList(String moneyStart,String moneyEnd,
                                                          String startTime,String endTime,
                                                          String page,String limit,Map<String,Object> params,
                                                          String paysubjectName, String incomesubjectName,
                                                                Integer tenantId){
        return baseMapper.getNzbankAccountList( moneyStart, moneyEnd,
                startTime, endTime,
                page, limit, params,
                paysubjectName,  incomesubjectName,tenantId);

    }

    public List<HdTransferBankStatement> getTynzbankAccountListEx(String moneyStart,String moneyEnd,
                                                            String startTime,String endTime,
                                                            Map<String,Object> params,
                                                            String paysubjectName,String incomesubjectName,
                                                                  Integer tenantId){
        return baseMapper.getNzbankAccountListEx( moneyStart, moneyEnd,
                startTime, endTime,
                params,
                paysubjectName, incomesubjectName,tenantId);

    }

    public String getTynzbankAccountCount(String moneyStart,String moneyEnd,
                                          String startTime,String endTime,
                                          Map<String,Object> params,
                                          String paysubjectName,String incomesubjectName,
                                          Integer tenantId){
        return baseMapper.getNzbankAccountCount( moneyStart, moneyEnd,
                startTime, endTime,
                params,
                paysubjectName, incomesubjectName,tenantId);

    }
}
