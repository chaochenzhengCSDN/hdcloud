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

package com.hodo.iiot.group2.hdcloud.bank.account.currency.service;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.CommonService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankStatement;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
public interface HdBankStatementService extends CommonService<HdBankStatement> {
    String searchRemark();

    public Integer getNoMaxNumber(Date accountDate);
    //分账
    public void handFenAccounts(List<HdBankStatement> fenAccounts);
    //划账
    public void handHuaAccounts(List<HdBankStatement> huaAccounts);

    public Integer synNZData(List<HdBankStatement> accountList, List<HdBankPending> pendingList,
            List<String> allsheetIds,String query_begin, String query_end);

    public Integer synNZIncome(List<HdBankStatement> accountList, List<HdBankPending> pendingList,
                            List<String> allsheetIds,String query_begin, String query_end);

    public Integer synNZPay(List<HdBankStatement> accountList, List<HdBankPending> pendingList,
                         List<String> allsheetIds,String query_begin, String query_end);

    public Integer synWZ(List<HdBankStatement> accountList, List<HdBankPending> pendingList,
                      List<String> allrIds,String query_begin, String query_end);

    public void handBankAccountList(List<HdBankStatement> accountList, Date accountDay);

    public R insertEntity(HdBankStatement entity);

    public List<HdBankStatement> getAllStatement(Map<String,Object> params);
}
