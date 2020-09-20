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

        com.hodo.iiot.group2.data.hdcloud.bank.account.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdMatchCompany;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
public interface HdMatchCompanyMapper extends BaseMapper<HdMatchCompany> {
    List<String> selectDistinctString(@Param("str") String str, @Param("param") String param,
                                      @Param("val") String val, @Param("tenantId") Integer tenantId);

    List<HdMatchCompany> getHdTymatchCompanyList(@Param("companyId") String companyId, @Param("page") String page,
                                                       @Param("limit") String limit,
                                                       @Param("tenantId") Integer tenantId, @Param("params") Map<String, Object> params);

    List<HdMatchCompany> getAllTymatchCompany(@Param("companyId") String companyId,
                                                    @Param("tenantId") Integer tenantId, @Param("params") Map<String, Object> params);


    String otherVal(@Param("companyId") String companyId,
                    @Param("tenantId") Integer tenantId, @Param("params") Map<String, Object> params);
}
