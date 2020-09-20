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

package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.mapper.cambodia;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankStatement;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
public interface HdBankStatementCambodiaMapper extends BaseMapper<HdBankStatement> {

    String getBalance(@Param("tenantId") Integer tenantId, @Param("bankAccountId") String bankAccountId,
                      @Param("accountDate") String accountDate, @Param("by2") String by2);

    List<HdBankStatement> getBankStatementListBySql(@Param("moneyStart") String moneyStart,
                                                    @Param("moneyEnd") String moneyEnd,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime,
                                                    @Param("page") String page,
                                                    @Param("limit") String limit,
                                                    @Param("params") Map<String, Object> params,
                                                    @Param("tenantId") Integer tenantId,
                                                    @Param("accountTypeList") List<String> accountTypeList,
                                                    @Param("synStartTime") String synStartTime,
                                                    @Param("synEndTime") String synEndTime);

    List<String>  getSheetIds(@Param("tenantId") int tenantId);

    List<String>  getRIds(@Param("tenantId") int tenantId);

    BigDecimal selectMaxNo(@Param("tenantId") int tenantId);

    String getAllAccountId();

    List<Map> getAllBalance(@Param("tenantId") int tenantId, @Param("accountList") List<String> accountList,
                            @Param("accountDate") String accountDate, @Param("by2") int by2);


    Map<String,Object> getOtherVal(@Param("moneyStart") String moneyStart, @Param("moneyEnd") String moneyEnd,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime,
                                   @Param("params") Map<String, Object> params,
                                   @Param("companyName") String companyName, @Param("tenantId") Integer tenantId,
                                   @Param("bankAccountId") String bankAccountId, @Param("accountTypeList") List<String> accountTypeList,
                                   @Param("synStartTime") String synStartTime, @Param("synEndTime") String synEndTime);

    
    
    List<HdBankStatement> getAllBankAccountBySql(@Param("moneyStart") String moneyStart, @Param("moneyEnd") String moneyEnd,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime,
                                                 @Param("params") Map<String, Object> params,
                                                 @Param("companyName") String companyName, @Param("tenantId") Integer tenantId,
                                                 @Param("bankAccountId") String bankAccountId,
                                                 @Param("accountTypeList") List<String> accountTypeList, @Param("synStartTime") String synStartTime,
                                                 @Param("synEndTime") String synEndTime);


    String getBankReportBalance(@Param("startTime") String dateStart, @Param("tenantId") Integer tenantId,
                                @Param("bankAccountId") String bankAccountId);


    String getComReportBalance(@Param("startTime") String dateStart, @Param("tenantId") Integer tenantId,
                               @Param("companyId") String companyId);

    List<String> selectDistinctString(@Param("str") String str, @Param("param") String param,
                                      @Param("val") String val, @Param("tenantId") Integer tenantId);

    List<HdBankStatement> getBankAccountListBySql(@Param("moneyStart") String moneyStart, @Param("moneyEnd") String moneyEnd,
                                                  @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                  @Param("page") String page, @Param("limit") String limit,
                                                  @Param("params") Map<String, Object> params, @Param("companyName") String companyName,
                                                  @Param("tenantId") Integer tenantId, @Param("bankAccountId") String bankAccountId,
                                                  @Param("accountTypeList") List<String> accountTypeList, @Param("synStartTime") String synStartTime,
                                                  @Param("synEndTime") String synEndTime);}
