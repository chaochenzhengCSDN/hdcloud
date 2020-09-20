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
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.*;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.service.HdBankStatementGfService;
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
@RequestMapping("/report/gf")
public class HdBankStatementGfController {

    private final HdBankStatementGfService hdBankStatementGfService;

    /**
     * 获取期初值
     *
     * @return
     */
    @GetMapping(value = "/getBalance")
    public String getBalance(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        String accountId = jjUtil.handleParams(map, "accountId");
        String accountDate = jjUtil.handleParams(map, "accountDate");
        String by2 = jjUtil.handleParams(map, "by2");
        jjUtil.handleParamsClean(map);
        return hdBankStatementGfService.getBalance(tenantId, accountId, accountDate, by2);
    }

    /**
     * 获取全部银行期初值
     *
     * @return
     */
    @GetMapping(value = "/getAllBalance")
    public List<Map> getAllBalance(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        String accountIds = hdBankStatementGfService.getAllAccountId();
        List<String> accountList = Arrays.asList(accountIds.split(","));
        String accountDate = jjUtil.handleParams(map, "accountDate");
        String by2 = jjUtil.handleParams(map, "by2");
        jjUtil.handleParamsClean(map);
        return hdBankStatementGfService.getAllBalance(tenantId, accountList, accountDate, Integer.parseInt(by2));
    }

    @GetMapping(value = "/selectStatics")
    List<Statistics> selectStatics(String startTime,
                                    String endTime,
                                    Integer tenantId,
                                   @RequestParam(required = false) Integer userId,
                                   @RequestParam(required = false) String ids) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        List<String> list = null;
        if(ids != null && ids.length()!=0){
            list = Arrays.asList(ids.split(","));
        }
        return hdBankStatementGfService.selectStatics(startTime,
                endTime, tenantId,userId,list);
    }
    @GetMapping(value = "/getComBankStatistics")
    List<ComBankStatistics> getComBankStatistics(String dateStart,
                                                 String dateEnd, Integer tenantId){
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getComBankStatistics(dateStart,
                dateEnd, tenantId);
    }
    @GetMapping(value = "/getBankStatisticsList")
    List<BankStatistics> getBankStatisticsList(String dateStart,
                                               String dateEnd,
                                               @RequestParam(required = false) Integer userId,
                                               Integer tenantId){
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankStatisticsList(dateStart,dateEnd,userId,tenantId);
    }


    @GetMapping(value = "/getBankComStatistics")
    List<BankComStatistics> getBankComStatistics(String dateStart,
                                                 String dateEnd,Integer tenantId){
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankComStatistics(dateStart,dateEnd,tenantId);
    }


    @GetMapping(value = "/getBankList")
    public List<TyBankStatistics> getBankList(String startTime, String endTime,
                                              String bankName, String uerId, Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankList(startTime, endTime, bankName, tenantId);
    }

    @GetMapping(value = "/getCompanyList")
    public List<TyBankStatistics> getCompanyList(String startTime, String endTime,
                                                 String companyName, String uerId, Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getCompanyList(startTime, endTime, companyName, uerId, tenantId);
    }

    @GetMapping(value = "/getMemberUnitList")
    public List<TyBankStatistics> getMemberUnitList(String startTime, String endTime, String memberUnitName,
                                                    String companyName, String uerId, Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getMemberUnitList(startTime, endTime, memberUnitName, companyName, uerId, tenantId);
    }

    @GetMapping(value = "/getBankCompanyList")
    public List<TyBankStatistics> getBankCompanyList(String startTime, String endTime,
                                                     String bankName, String companyName,
                                                     String uerId, Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankCompanyList(startTime, endTime, bankName, companyName, uerId, tenantId);
    }

    @GetMapping(value = "/getBankMemberUnitList")
    public List<TyBankStatistics> getBankMemberUnitList(String startTime, String endTime, String bankName,
                                                        String companyName, String memberUnitName, String uerId,
                                                        Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankMemberUnitList(startTime, endTime, bankName, companyName, memberUnitName, uerId, tenantId);
    }

    @GetMapping(value = "/getDetailCompanyList")
    public List<TyComDetailStatistics> getDetailCompanyList(String startTime, String endTime,
                                                            String member, String company,
                                                            String uerId, Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getDetailCompanyList(startTime, endTime, member, company, uerId, tenantId);
    }

    @GetMapping(value = "/getDetailBankList")
    public List<TyBankDetailStatistics> getDetailBankList(String startTime, String endTime,
                                                          String accountId, String uerId,
                                                          Integer tenantId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getDetailBankList(startTime, endTime, accountId, uerId, tenantId);
    }

    @GetMapping(value = "/getBankDetailList")
    public List<BankDetailStatistics> getBankDetailList(String dateStart,
                                                          String dateEnd, Integer tenantId,
                                                          String accountId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankDetailList(dateStart, dateEnd, tenantId, accountId);
    }

    @GetMapping(value = "/getBankReportBalance")
    public String getBankReportBalance(String dateStart, Integer tenantId,
                                       String accountId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankReportBalance(dateStart, tenantId, accountId);
    }

    @GetMapping(value = "/getComDetailList")
    public List<ComDetailStatistics> getComDetailList(String dateStart,
                                                        String dateEnd, Integer tenantId,
                                                        String companyId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getComDetailList(dateStart, dateEnd, tenantId, companyId);
    }

    @GetMapping(value = "/getComReportBalance")
    public String getComReportBalance(String dateStart, Integer tenantId,
                                      String companyId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getComReportBalance(dateStart, tenantId, companyId);
    }


}
