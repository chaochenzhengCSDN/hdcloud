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

package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.Statistics;
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
    List<Statistics> selectStatics(@Param("startTime") String startTime,
                             @Param("endTime") String endTime,
                             @Param("tenantId") Integer tenantId,
                             @Param("userId") String userId,
                             @Param("ids")List<String> ids);

}


