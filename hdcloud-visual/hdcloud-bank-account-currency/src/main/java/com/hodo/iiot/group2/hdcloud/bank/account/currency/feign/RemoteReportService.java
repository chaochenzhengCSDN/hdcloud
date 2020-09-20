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

package com.hodo.iiot.group2.hdcloud.bank.account.currency.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author hodo
 * @date 2018/6/28
 */
@FeignClient(contextId = "remoteReportService", value = ServiceNameConstants.BILL_DATA)
public interface RemoteReportService {
    @GetMapping(value="/hdbankstatementty/getBankList")
    List<TyBankStatistics> getBankList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,
                                       @RequestParam("bankAccountId") String bankName,@RequestParam("flag") String flag,
                                       @RequestParam("tenantId") String tenantId);

    @GetMapping(value="/hdbankstatementty/getCompanyList")
    List<TyBankStatistics> getCompanyList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,
                                          @RequestParam("companyId") String companyName,@RequestParam("flag") String flag,
                                          @RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getMemberUnitList")
    List<TyBankStatistics> getMemberUnitList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,
                                             @RequestParam("companyId") String companyName,@RequestParam("memberCompanyId") String memberUnitName,
                                             @RequestParam("flag") String flag,@RequestParam("tenantId") String tenantId);

    @GetMapping(value="/hdbankstatementty/getBankMemberUnitList")
    List<TyBankStatistics> getBankMemberUnitList(@RequestParam("startTime") String startTime,@RequestParam("endTime")String endTime,
                                                 @RequestParam("bankAccountId") String bankName,@RequestParam("companyId") String companyName,
                                                 @RequestParam("memberCompanyId") String memberUnitName, @RequestParam("flag") String flag,
                                                 @RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getBankCompanyList")
    List<TyBankStatistics> getBankCompanyList(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,
                                              @RequestParam("bankAccountId") String bankName,@RequestParam("companyId") String companyName,
                                              @RequestParam("flag") String flag,@RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getBankDetailList")
    List<TyBankDetailStatistics> getBankDetailList(@RequestParam("startTime")String dateStart,
                                                   @RequestParam("endTime")String dateEnd,
                                                   @RequestParam("bankAccountId")String accountId,@RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getBankReportBalance")
    String getBankReportBalance(@RequestParam("startTime")String dateStart,
                                @RequestParam("bankAccountId")String accountId,@RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getComDetailList")
    List<TyComDetailStatistics> getComDetailList(@RequestParam("startTime")String dateStart,
                                                 @RequestParam("endTime")String dateEnd,
                                                 @RequestParam("companyId")String companyId,@RequestParam("tenantId") String tenantId);
    @GetMapping(value="/hdbankstatementty/getComReportBalance")
    String getComReportBalance(@RequestParam("startTime")String dateStart,
                               @RequestParam("companyId")String companyId,@RequestParam("tenantId") String tenantId);

}


