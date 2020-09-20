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

        com.hodo.iiot.group2.data.hdcloud.bank.account.bill.mapper.ty;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:37:59
 */
public interface HdBankRecordTyMapper extends BaseMapper<HdBankRecord> {

    List<HdBankRecord> getHdBankRecordList(@Param("moneyStart") String moneyStart, @Param("moneyEnd") String moneyEnd,
                                           @Param("companyId") String companyId, @Param("startTime") String startTime,
                                           @Param("endTime") String endTime, @Param("page") String page, @Param("limit") String limit,
                                           @Param("tenantId") Integer tenantId, @Param("params") Map<String, Object> params,
                                           @Param("num") String num, @Param("accountId") String accountId);

    List<HdBankRecord> getAllBankRecord(@Param("moneyStart") String moneyStart, @Param("moneyEnd") String moneyEnd,
                                        @Param("companyName") String companyName, @Param("startTime") String startTime,
                                        @Param("endTime") String endTime,
                                        @Param("tenantId") Integer tenantId, @Param("params") Map<String, Object> params,
                                        @Param("num") String num, @Param("accountId") String accountId);

    List<String> selectDistinctString(@Param("str") String str, @Param("param") String param,
                                      @Param("val") String val, @Param("tenantId") Integer tenantId);

    String otherval(@Param("moneyStart") String moneyStart, @Param("moneyEnd") String moneyEnd,
                    @Param("companyName") String companyName, @Param("startTime") String startTime,
                    @Param("endTime") String endTime,
                    @Param("tenantId") Integer tenantId, @Param("params") Map<String, Object> params,
                    @Param("num") String num, @Param("accountId") String accountId);
}
