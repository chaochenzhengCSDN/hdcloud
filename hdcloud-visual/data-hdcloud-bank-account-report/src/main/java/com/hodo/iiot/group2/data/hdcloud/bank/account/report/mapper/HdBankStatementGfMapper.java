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

        com.hodo.iiot.group2.data.hdcloud.bank.account.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.report.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
public interface HdBankStatementGfMapper extends BaseMapper<HdBankStatementGf> {

    List<Statistics> selectStatics(@Param("startTime") String startTime,
                                   @Param("endTime") String endTime,
                                   @Param("tenantId") Integer tenantId,
                                   @Param("userId") Integer userId,
                                   @Param("ids")List<String> ids);

    List<BankStatistics> getBankStatisticsList(@Param("startTime")String dateStart,
                                               @Param("endTime")String dateEnd,
                                               @Param("userId")Integer userId,
                                               @Param("tenantId")Integer tenantId);

    List<ComBankStatistics> getComBankStatistics(@Param("startTime")String dateStart,
                                                 @Param("endTime")String dateEnd,
                                                 @Param("tenantId")Integer tenantId);

    List<BankComStatistics> getBankComStatistics(@Param("startTime")String dateStart,
                                                 @Param("endTime")String dateEnd,
                                                 @Param("tenantId")Integer tenantId);


    String getBalance(@Param("tenantId") Integer tenantId, @Param("accountId") String accountId,
                      @Param("accountDate") String accountDate, @Param("by2") String by2);


    String getAllAccountId();

    List<Map> getAllBalance(@Param("tenantId") int tenantId, @Param("accountList") List<String> accountList,
                            @Param("accountDate") String accountDate, @Param("by2") int by2);


    List<TyBankStatistics> getBankList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                       @Param("bankName") String bankName, @Param("tenantId") Integer tenantId);

    List<TyBankStatistics> getCompanyList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                          @Param("companyName") String companyName, @Param("userId") String uerId,
                                          @Param("tenantId") Integer tenantId);

    List<TyBankStatistics> getMemberUnitList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                             @Param("memberUnitName") String memberUnitName,
                                             @Param("companyName") String companyName, @Param("userId") String uerId,
                                             @Param("tenantId") Integer tenantId);

    List<TyBankStatistics> getBankCompanyList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                              @Param("bankName") String bankName, @Param("companyName") String companyName, @Param("userId") String uerId, @Param("tenantId") Integer tenantId);

    List<TyBankStatistics> getBankMemberUnitList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                                 @Param("bankName") String bankName,
                                                 @Param("companyName") String companyName,
                                                 @Param("memberUnitName") String memberUnitName,
                                                 @Param("userId") String uerId, @Param("tenantId") Integer tenantId);

    List<TyComDetailStatistics> getDetailCompanyList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                                     @Param("member") String member, @Param("company") String company,
                                                     @Param("userId") String uerId, @Param("tenantId") Integer tenantId);

    List<TyBankDetailStatistics> getDetailBankList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                                   @Param("accountId") String accountId, @Param("userId") String uerId,
                                                   @Param("tenantId") Integer tenantId);



    List<BankDetailStatistics> getBankDetailList(@Param("startTime")String dateStart,
                                                 @Param("endTime")String dateEnd,@Param("tenantId")Integer tenantId,
                                                 @Param("accountId")String accountId);
    String getBankReportBalance(@Param("startTime")String dateStart,@Param("tenantId")Integer tenantId,
                                @Param("accountId")String accountId);

    String getComReportBalance(@Param("startTime")String dateStart,@Param("tenantId")Integer tenantId,
                               @Param("companyId")String companyId);


    List<ComDetailStatistics> getComDetailList(@Param("startTime")String dateStart,
                                               @Param("endTime")String dateEnd,@Param("tenantId")Integer tenantId,
                                               @Param("companyId")String companyId);

}