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

        com.hodo.iiot.group2.data.hdcloud.bank.account.report.controller;

import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.TyBankDetailStatistics;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.TyBankStatistics;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.TyComDetailStatistics;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.service.HdBankStatementTyService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.util.jjUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/report/ty")
public class HdBankStatementTyController {

    private final HdBankStatementTyService hdBankStatementTyService;

    /**
     * 获取期初值
     *
     * @return
     */
    @GetMapping(value = "/getBalance")
    public String getBalance(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        Integer tenantId = TenantContextHolder.getTenantId();
        String accountId = jjUtil.handleParams(map, "accountId");
        String accountDate = jjUtil.handleParams(map, "accountDate");
        String by2 = jjUtil.handleParams(map, "by2");
        jjUtil.handleParamsClean(map);
        return hdBankStatementTyService.getBalance(tenantId.toString(), accountId, accountDate, by2);
    }

    /**
     * 获取全部银行期初值
     *
     * @return
     */
    @GetMapping(value = "/getAllBalance")
    public List<Map> getAllBalance(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        Integer tenantId = TenantContextHolder.getTenantId();
        String accountIds = hdBankStatementTyService.getAllAccountId();
        List<String> accountList = Arrays.asList(accountIds.split(","));
        String accountDate = jjUtil.handleParams(map, "accountDate");
        String by2 = jjUtil.handleParams(map, "by2");
        jjUtil.handleParamsClean(map);
        return hdBankStatementTyService.getAllBalance(tenantId, accountList, accountDate, Integer.parseInt(by2));
    }


    @GetMapping(value = "/getBankList")
    public List<TyBankStatistics> getBankList(String startTime, String endTime,
                                              String bankName, String uerId, String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getBankList(startTime, endTime, bankName, uerId, tenantId);
    }

    @GetMapping(value = "/getCompanyList")
    public List<TyBankStatistics> getCompanyList(String startTime, String endTime,
                                                 String companyName, String uerId, String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getCompanyList(startTime, endTime, companyName, uerId, tenantId);
    }

    @GetMapping(value = "/getMemberUnitList")
    public List<TyBankStatistics> getMemberUnitList(String startTime, String endTime, String memberUnitName,
                                                    String companyName, String uerId, String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getMemberUnitList(startTime, endTime, memberUnitName,companyName, uerId, tenantId);
    }

    @GetMapping(value = "/getBankCompanyList")
    public List<TyBankStatistics> getBankCompanyList(String startTime, String endTime,
                                                     String bankName, String companyName,
                                                     String uerId, String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getBankCompanyList(startTime, endTime, bankName,companyName, uerId, tenantId);
    }

    @GetMapping(value = "/getBankMemberUnitList")
    public List<TyBankStatistics> getBankMemberUnitList(String startTime, String endTime, String bankName,
                                                        String companyName, String memberUnitName, String uerId,
                                                        String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getBankMemberUnitList(startTime, endTime, bankName,companyName,memberUnitName, uerId, tenantId);
    }

    @GetMapping(value = "/getDetailCompanyList")
    public List<TyComDetailStatistics> getDetailCompanyList(String startTime, String endTime,
                                                            String member, String company,
                                                            String uerId, String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getDetailCompanyList(startTime, endTime, member,company, uerId, tenantId);
    }

    @GetMapping(value = "/getDetailBankList")
    public List<TyBankDetailStatistics> getDetailBankList(String startTime, String endTime,
                                                          String accountId, String uerId,
                                                          String tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getDetailBankList(startTime, endTime, accountId, uerId, tenantId);
    }

    @GetMapping(value = "/getBankDetailList")
    public List<TyBankDetailStatistics> getBankDetailList(String dateStart,
                                                          String dateEnd, String tenantId,
                                                          String accountId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getBankDetailList(dateStart, dateEnd, tenantId, accountId);
    }

    @GetMapping(value = "/getBankReportBalance")
    public String getBankReportBalance(String dateStart, String tenantId,
                                       String accountId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getBankReportBalance(dateStart, tenantId, accountId);
    }

    @GetMapping(value = "/getComDetailList")
    public List<TyComDetailStatistics> getComDetailList(String dateStart,
                                                        String dateEnd, String tenantId,
                                                        String companyId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getComDetailList(dateStart, dateEnd, tenantId, companyId);
    }

    @GetMapping(value = "/getComReportBalance")
    public String getComReportBalance(String dateStart, String tenantId,
                                      String companyId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);

        return hdBankStatementTyService.getComReportBalance(dateStart, tenantId, companyId);
    }
    
}
