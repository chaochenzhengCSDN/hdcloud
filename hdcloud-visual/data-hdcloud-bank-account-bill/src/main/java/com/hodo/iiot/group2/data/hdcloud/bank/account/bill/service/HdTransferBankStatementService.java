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
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdTransferBankStatement;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
public interface HdTransferBankStatementService extends IService<HdTransferBankStatement> {
    List<String> selectDistinctString(String str, String param,
                                      String val, Integer tenantId);

//    BigDecimal selectMaxNo(Integer tenantId);

     List<HdTransferBankStatement> getTynzbankAccountList(String moneyStart, String moneyEnd,
                                                          String startTime, String endTime,
                                                          String page, String limit, Map<String, Object> params,
                                                          String paysubjectName, String incomesubjectName,
                                                          Integer tenantId);

     List<HdTransferBankStatement> getTynzbankAccountListEx(String moneyStart, String moneyEnd,
                                                            String startTime, String endTime,
                                                            Map<String, Object> params,
                                                            String paysubjectName, String incomesubjectName,
                                                            Integer tenantId);

     String getTynzbankAccountCount(String moneyStart, String moneyEnd,
                                    String startTime, String endTime,
                                    Map<String, Object> params,
                                    String paysubjectName, String incomesubjectName,
                                    Integer tenantId);


}
