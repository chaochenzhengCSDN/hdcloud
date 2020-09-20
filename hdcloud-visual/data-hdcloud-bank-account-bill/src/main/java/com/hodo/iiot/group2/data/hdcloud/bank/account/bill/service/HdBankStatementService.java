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
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankStatement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
public interface HdBankStatementService extends IService<HdBankStatement> {

//    List<HdBankStatement> getBankStatementListBySql(String moneyStart, String moneyEnd, String startTime,
//                                                      String endTime, String page, String limit,
//                                                      Map<String, Object> params, Integer tenantId,
//                                                      List<String> accountTypeList, String synStartTime,
//                                                      String synEndTime);

//     List<HdBankStatement> getAllBankAccountBySql(String moneyStart, String moneyEnd,
//                                                    String startTime,
//                                                    String endTime,
//                                                    Map<String, Object> params,
//                                                    Integer tenantId,
//                                                    String accountId,
//                                                    List<String> accountTypeList, String synStartTime,
//                                                    String synEndTime);
//
//     Map<String,Object> getOtherVal(String moneyStart, String moneyEnd,
//                                    String startTime,
//                                    String endTime,
//                                    Map<String, Object> params,
//                                    Integer tenantId,
//                                    String accountId, List<String> accountTypeList,
//                                    String synStartTime, String synEndTime);


    String getBalance(String tenantId, String accountId, String accountDate, String by2);

    List<String> getSheetIds(int tenantId);

    List<String> getRIds(int tenantId);

    BigDecimal selectMaxNoByDate(String accountDate, int tenantId);

    BigDecimal selectMaxNo(int tenantId);

    String getAllAccountId();

    List<Map> getAllBalance(int tenantId, List<String> accountList, String accountDate, int by2);



    String getBankReportBalance(String dateStart, String tenantId,
                                String accountId);


     String getBalance(Integer tenantId, String accountId, String accountDate, String by2) ;

     String getBankReportBalance(String dateStart, Integer tenantId,
                                       String accountId) ;



     String getComReportBalance(String dateStart, Integer tenantId,
                                      String companyId) ;


    String getComReportBalance(String dateStart, String tenantId,
                               String companyId);

    List<String> selectDistinctString(String str, String param,
                                      String val, Integer tenantId);

}
