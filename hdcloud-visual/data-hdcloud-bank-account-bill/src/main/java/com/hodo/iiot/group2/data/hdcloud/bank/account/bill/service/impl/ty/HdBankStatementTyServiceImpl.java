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
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankStatement;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.mapper.ty.HdBankStatementTyMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.HdBankStatementService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Service
public class HdBankStatementTyServiceImpl extends ServiceImpl<HdBankStatementTyMapper, HdBankStatement>
        implements HdBankStatementService {


    public List<HdBankStatement> getBankStatementListBySql(String moneyStart, String moneyEnd, String startTime,
                                                             String endTime, String page, String limit,
                                                             Map<String, Object> params, Integer tenantId,
                                                             List<String> accountTypeList, String synStartTime,
                                                             String synEndTime) {
        return this.baseMapper.getBankStatementListBySql(moneyStart, moneyEnd, startTime, endTime,
                page, limit, params, tenantId, accountTypeList,
                synStartTime, synEndTime);
    }


    public List<HdBankStatement> getAllBankAccountBySql(String moneyStart, String moneyEnd, String startTime,
                                                          String endTime, Map<String, Object> params, Integer tenantId,
                                                          String accountId, List<String> accountTypeList,
                                                          String synStartTime, String synEndTime) {
        return this.baseMapper.getAllBankAccountBySql( moneyStart, moneyEnd, startTime, endTime,params,
                tenantId, accountId, accountTypeList, synStartTime, synEndTime);
    }


    public Map<String, Object> getOtherVal(String moneyStart, String moneyEnd, String startTime, String endTime,
                                           Map<String, Object> params, Integer tenantId, String accountId,
                                           List<String> accountTypeList, String synStartTime, String synEndTime) {
        return this.baseMapper.getOtherVal(moneyStart,moneyEnd,startTime,endTime,params,
                tenantId,accountId,accountTypeList,synStartTime,synEndTime);
    }


    @Override
    public String getBalance(String tenantId, String accountId, String accountDate, String by2) {
        return this.baseMapper.getBalance(tenantId, accountId, accountDate, by2);
    }


    @Override
    public List<String> getSheetIds(int tenantId) {
        return baseMapper.getSheetIds(tenantId);
    }

    @Override
    public List<String> getRIds(int tenantId) {
        return baseMapper.getRIds(tenantId);
    }

    @Override
    public BigDecimal selectMaxNoByDate(String accountDate, int tenantId) {
        return baseMapper.selectMaxNoByDate(accountDate,tenantId);
    }

    @Override
    public BigDecimal selectMaxNo(int tenantId) {
        return baseMapper.selectMaxNo(tenantId);
    }

    @Override
    public String getAllAccountId() {
        return baseMapper.getAllAccountId();
    }

    @Override
    public List<Map> getAllBalance(int tenantId, List<String> accountList, String accountDate, int by2) {
        return baseMapper.getAllBalance(tenantId, accountList, accountDate, by2);
    }



    @Override
    public String getBankReportBalance(String dateStart, String tenantId,
                                       String accountId) {
        return baseMapper.getBankReportBalance(dateStart, tenantId, accountId);
    }

    @Override
    public String getBalance(Integer tenantId, String accountId, String accountDate, String by2) {
        return null;
    }

    @Override
    public String getBankReportBalance(String dateStart, Integer tenantId, String accountId) {
        return null;
    }

    @Override
    public String getComReportBalance(String dateStart, Integer tenantId, String companyId) {
        return null;
    }


    @Override
    public String getComReportBalance(String dateStart, String tenantId,
                                      String companyId) {
        return baseMapper.getComReportBalance(dateStart, tenantId, companyId);
    }

    @Override
    public List<String> selectDistinctString(String str, String param, String val, Integer tenantId) {
        return baseMapper.selectDistinctString(str,param,val,tenantId);
    }
}
