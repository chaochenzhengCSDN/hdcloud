package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service;

import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.Statistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019-12-07.
 */
public interface HdReportService {
    List<Statistics> statics(String startTime,
                                   String endTime,
                                   Integer tenantId,
                                   String userId,
                                   List<String> ids);

}
