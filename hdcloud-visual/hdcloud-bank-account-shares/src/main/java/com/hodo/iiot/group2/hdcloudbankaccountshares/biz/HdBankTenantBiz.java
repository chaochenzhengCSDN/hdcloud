package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;


import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankTenantFeign;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HdBankTenantBiz {

    @Autowired
    HdBankTenantFeign hdBankTenantFeign;

    //根据租户id获取抬头
    public HdBankTenant getBankNameByTenant(String tenantId){
        Map<String,Object> map = new HashedMap();
        map.put("tenantId",tenantId);
        List<HdBankTenant> hdBankTenantList = hdBankTenantFeign.getHdTenantList(map);
        if(hdBankTenantList!=null&&hdBankTenantList.size()>0){
            return hdBankTenantList.get(0);
        }
        return null;
    }


}
