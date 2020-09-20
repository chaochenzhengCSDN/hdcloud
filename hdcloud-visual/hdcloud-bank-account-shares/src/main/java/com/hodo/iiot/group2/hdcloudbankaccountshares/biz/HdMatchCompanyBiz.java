package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.github.ag.core.context.BaseContextHandler;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdMatchCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdMatchCompanyFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HdMatchCompanyBiz {

    @Autowired
    HdMatchCompanyFeign hdMatchCompanyFeign;

    //根据外转的公司查询是否存在
    public List<HdMatchCompany> getMatchCompanyByTerm(String customerName) {
        return hdMatchCompanyFeign.getMatchCompanyByTerm(customerName);
    }



}
