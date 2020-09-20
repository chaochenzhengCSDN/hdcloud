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

        com.hodo.iiot.group2.data.hdcloud.bank.account.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.*;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
public interface HdBankStatementGfService extends IService<HdBankStatementGf> {

    List<Statistics> selectStatics( String startTime,
                                   String endTime,
                                    Integer tenantId,
                                    Integer userId,
                                   List<String> ids);

    List<ComBankStatistics> getComBankStatistics(String dateStart,
                                                 String dateEnd,Integer tenantId);

    List<BankStatistics> getBankStatisticsList(String dateStart,
                                               String dateEnd,
                                               Integer userId,
                                               Integer tenantId);



    List<BankComStatistics> getBankComStatistics(String dateStart,
                                                 String dateEnd,
                                                 Integer tenantId);

    String getBalance(Integer tenantId, String accountId, String accountDate, String by2);


    String getAllAccountId();

    List<Map> getAllBalance(int tenantId, List<String> accountList, String accountDate, int by2);


    List<TyBankStatistics> getBankList(String startTime, String endTime,
                                       String bankName, Integer tenantId);


    List<TyBankStatistics> getCompanyList(String startTime, String endTime,
                                          String companyName, String uerId, Integer tenantId);


    List<TyBankStatistics> getMemberUnitList(String startTime, String endTime, String memberUnitName,
                                             String companyName, String uerId, Integer tenantId);


    List<TyBankStatistics> getBankCompanyList(String startTime, String endTime,
                                              String bankName, String companyName,
                                              String uerId, Integer tenantId);


    List<TyBankStatistics> getBankMemberUnitList(String startTime, String endTime, String bankName,
                                                 String companyName, String memberUnitName, String uerId,
                                                 Integer tenantId);


    List<TyComDetailStatistics> getDetailCompanyList(String startTime, String endTime,
                                                     String member, String company,
                                                     String uerId, Integer tenantId);


    List<TyBankDetailStatistics> getDetailBankList(String startTime, String endTime,
                                                   String accountId, String uerId,
                                                   Integer tenantId);


    List<BankDetailStatistics> getBankDetailList(String dateStart,
                                                 String dateEnd,Integer tenantId,
                                                 String accountId);
    String getBankReportBalance(String dateStart,Integer tenantId,
                                String accountId);

    String getComReportBalance(String dateStart,Integer tenantId,
                               String companyId);


    List<ComDetailStatistics> getComDetailList(String dateStart,
                                               String dateEnd,Integer tenantId,
                                               String companyId);
}
