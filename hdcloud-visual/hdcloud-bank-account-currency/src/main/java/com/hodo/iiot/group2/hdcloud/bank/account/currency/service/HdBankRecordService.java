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

import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.CommonService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankStatement;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:37:59
 */
public interface HdBankRecordService extends CommonService<HdBankRecord> {
    //封装实体类
    public HdBankRecord getHdBankRecord(HdBankStatement hdBankStatement, String type);

}
