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

package com.hodo.iiot.group2.data.hdcloud.cash.account.bill.mapper.cambodia;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hodo.iiot.group2.data.hdcloud.cash.account.bill.entity.HdCashStatement;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
public interface HdCashStatementCambodiaMapper extends BaseMapper<HdCashStatement> {
    List<String> selectDistinctString(@Param("str") String str, @Param("param") String param,
                                      @Param("val") String val, @Param("tenantId") Integer tenantId);

    List<HdCashStatement> selectList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                     @Param("remark") String remark, @Param("unitId") String unitId,
                                     @Param("subjects") String subjects, @Param("page") Integer page,
                                     @Param("limit") Integer limit, @Param("tenantId") Integer tenantId);

    List<HdCashStatement> selectAll(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                    @Param("remark") String remark, @Param("unitId") String unitId,
                                    @Param("subjects") String subjects, @Param("tenantId") Integer tenantId);

    Map<String,Object> selectTotal(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                   @Param("remark") String remark, @Param("unitId") String unitId,
                                   @Param("subjects") String subjects, @Param("tenantId") Integer tenantId);

    String selectMaxNo(@Param("tenantId") Integer tenantId);

    String selectMaxNoByDate(@Param("tenantId") Integer tenantId);

    List<HdCashStatement> selectStatistics(@Param("tenantId") Integer tenantId);


}
