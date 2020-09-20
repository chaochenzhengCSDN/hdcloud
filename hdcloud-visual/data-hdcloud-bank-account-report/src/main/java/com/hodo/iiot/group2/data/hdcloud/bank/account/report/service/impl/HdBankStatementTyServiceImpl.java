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

        com.hodo.iiot.group2.data.hdcloud.bank.account.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.HdBankStatementTy;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.TyBankDetailStatistics;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.TyBankStatistics;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.TyComDetailStatistics;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.mapper.HdBankStatementTyMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.service.HdBankStatementTyService;
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
public class HdBankStatementTyServiceImpl extends ServiceImpl<HdBankStatementTyMapper, HdBankStatementTy>
        implements HdBankStatementTyService {

    @Override
    public String getBalance(String tenantId, String accountId, String accountDate, String by2) {
        return this.baseMapper.getBalance(tenantId, accountId, accountDate, by2);
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
    public List<TyBankStatistics> getBankList(String startTime, String endTime,
                                              String bankAccountId, String uerId, String tenantId) {
        return baseMapper.getBankList(startTime, endTime, bankAccountId, uerId, tenantId);
    }

    @Override
    public List<TyBankStatistics> getCompanyList(String startTime, String endTime,
                                                 String companyName, String uerId, String tenantId) {
        return baseMapper.getCompanyList(startTime, endTime, companyName, uerId, tenantId);
    }

    @Override
    public List<TyBankStatistics> getMemberUnitList(String startTime, String endTime, String memberUnitName,
                                                    String companyName, String uerId, String tenantId) {
        return baseMapper.getMemberUnitList(startTime, endTime, memberUnitName, companyName, uerId, tenantId);
    }

    @Override
    public List<TyBankStatistics> getBankCompanyList(String startTime, String endTime,
                                                     String bankName, String companyName,
                                                     String uerId, String tenantId) {
        return baseMapper.getBankCompanyList(startTime, endTime, bankName, companyName, uerId, tenantId);
    }

    @Override
    public List<TyBankStatistics> getBankMemberUnitList(String startTime, String endTime, String bankName,
                                                        String companyName, String memberUnitName, String uerId,
                                                        String tenantId) {
        return baseMapper.getBankMemberUnitList(startTime, endTime, bankName, companyName, memberUnitName, uerId, tenantId);
    }

    @Override
    public List<TyComDetailStatistics> getDetailCompanyList(String startTime, String endTime,
                                                            String member, String company,
                                                            String uerId, String tenantId) {
        return baseMapper.getDetailCompanyList(startTime, endTime, member, company, uerId, tenantId);
    }

    @Override
    public List<TyBankDetailStatistics> getDetailBankList(String startTime, String endTime,
                                                          String accountId, String uerId,
                                                          String tenantId) {
        return baseMapper.getDetailBankList(startTime, endTime, accountId, uerId, tenantId);
    }

    @Override
    public List<TyBankDetailStatistics> getBankDetailList(String dateStart,
                                                          String dateEnd, String tenantId,
                                                          String accountId) {
        return baseMapper.getBankDetailList(dateStart, dateEnd, tenantId, accountId);
    }

    @Override
    public String getBankReportBalance(String dateStart, String tenantId,
                                       String accountId) {
        return baseMapper.getBankReportBalance(dateStart, tenantId, accountId);
    }

    @Override
    public List<TyComDetailStatistics> getComDetailList(String dateStart,
                                                        String dateEnd, String tenantId,
                                                        String companyId) {
        return baseMapper.getComDetailList(dateStart, dateEnd, tenantId, companyId);
    }

    @Override
    public String getComReportBalance(String dateStart, String tenantId,
                                      String companyId) {
        return baseMapper.getComReportBalance(dateStart, tenantId, companyId);
    }


}
