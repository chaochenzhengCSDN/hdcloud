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

        com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankRecord;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:37:59
 */
public interface HdBankRecordService extends IService<HdBankRecord> {

    List<HdBankRecord> getHdBankRecordList(String moneyStart, String moneyEnd, String companyName, String startTime,
                                           String endTime, String s, String limit, Integer tenantId,
                                           Map<String, Object> params, String num, String accountId);


    List<HdBankRecord> getAllBankRecord(String moneyStart, String moneyEnd, String companyName, String startTime,
                                        String endTime, Integer tenantId, Map<String, Object> params, String num,
                                        String accountId);

    List<String> selectDistinctString(String str, String param,
                                      String val, Integer tenantId);

    String otherVal(String moneyStart, String moneyEnd, String companyName, String startTime,
                    String endTime, Integer tenantId, Map<String, Object> params, String num,
                    String accountId);

}
