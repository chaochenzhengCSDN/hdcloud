/*
 *
 *      Copyright (c) 2018-2025, hodo All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: 江苏红豆工业互联网有限公司
 *
 */

package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign;


import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hodo
 * @date 2018/6/28
 */
@FeignClient(contextId = "remoteReportService", value = ServiceNameConstants.BILL_DATA)
public interface RemoteReportService {
    @GetMapping(value="/hdbankstatementty/getBankList")
    List<BankStatistics> getBankList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                       @RequestParam("bankAccountId") String bankName, @RequestParam("flag") String flag,
                                       @RequestParam("tenantId") String tenantId);

    @GetMapping(value="/hdbankstatementty/getCompanyList")
    List<BankStatistics> getCompanyList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                        @RequestParam("companyId") String companyName, @RequestParam("flag") String flag,
                                        @RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getMemberUnitList")
    List<BankStatistics> getMemberUnitList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                             @RequestParam("companyId") String companyName, @RequestParam("memberCompanyId") String memberUnitName,
                                             @RequestParam("flag") String flag, @RequestParam("tenantId") String tenantId);

    @GetMapping(value="/hdbankstatementty/getBankMemberUnitList")
    List<BankStatistics> getBankMemberUnitList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                                 @RequestParam("bankAccountId") String bankName, @RequestParam("companyId") String companyName,
                                                 @RequestParam("memberCompanyId") String memberUnitName, @RequestParam("flag") String flag,
                                                 @RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getBankCompanyList")
    List<BankStatistics> getBankCompanyList(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                              @RequestParam("bankAccountId") String bankName, @RequestParam("companyId") String companyName,
                                              @RequestParam("flag") String flag, @RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getBankDetailList")
    List<BankDetailStatistics> getBankDetailList(@RequestParam("startTime") String dateStart,
                                                 @RequestParam("endTime") String dateEnd,
                                                 @RequestParam("bankAccountId") String accountId, @RequestParam("tenantId") Integer tenantId);
    @GetMapping(value="/hdbankstatementty/getBankReportBalance")
    String getBankReportBalance(@RequestParam("startTime") String dateStart,
                                @RequestParam("bankAccountId") String accountId, @RequestParam("tenantId") Integer tenantId);
    @GetMapping(value="/hdbankstatementty/getComDetailList")
    List<ComDetailStatistics> getComDetailList(@RequestParam("startTime") String dateStart,
                                               @RequestParam("endTime") String dateEnd,
                                               @RequestParam("companyId") String companyId, @RequestParam("tenantId") Integer tenantId);
    @GetMapping(value="/hdbankstatementty/getComReportBalance")
    String getComReportBalance(@RequestParam("startTime") String dateStart,
                               @RequestParam("companyId") String companyId, @RequestParam("tenantId") Integer tenantId);

    public List<ComBankStatistics> getComBankStatistics(@RequestParam("startTime") String dateStart, @RequestParam("endTime") String dateEnd,@RequestParam("tenantId") Integer tenantId);
    public List<BankComStatistics> getBankComStatistics(@RequestParam("startTime") String dateStart, @RequestParam("endTime") String dateEnd,@RequestParam("tenantId") Integer tenantId);

    List<BankStatistics> getBankStatisticsList(@Param("startTime")String dateStart,
                                               @Param("endTime")String dateEnd,
                                               @Param("userId")String userId,
                                               @Param("tenantId")Integer tenantId);
    List<Statistics> selectStatics(@Param("startTime") String startTime,
                                   @Param("endTime") String endTime,
                                   @Param("tenantId") Integer tenantId,
                                   @Param("userId") String userId,
                                   @Param("ids")List<String> ids);


}


