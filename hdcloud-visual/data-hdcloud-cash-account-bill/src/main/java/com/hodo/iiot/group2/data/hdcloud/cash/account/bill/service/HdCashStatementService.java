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

package com.hodo.iiot.group2.data.hdcloud.cash.account.bill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.entity.HdCashStatement;

import java.util.List;
import java.util.Map;

/**
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
public interface HdCashStatementService extends IService<HdCashStatement> {
    List<HdCashStatement> selectList(String startTime, String endTime,
                                     String remark, String unitId,
                                     String subjects, Integer page,
                                     Integer limit, Integer tenantId);

    List<HdCashStatement> selectAll(String startTime, String endTime,
                                    String remark, String unitId,
                                    String subjects, Integer tenantId);

    Map<String,Object> selectTotal(String startTime, String endTime,
                                   String remark, String unitId,
                                   String subjects, Integer tenantId);

    String selectMaxNo(Integer tenantId);

    String selectMaxNoByDate(Integer tenantId);

    List<HdCashStatement> selectStatistics(Integer tenantId);

}
