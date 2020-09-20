package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteHdBankTenant", value = ServiceNameConstants.BILL_DATA)
public interface HdBankTenantFeign {



    @GetMapping(value = "/hdbanktenant/gf/all")
    List<HdBankTenant> getHdTenantList(@RequestParam Map<String, Object> map);


    @RequestMapping(value = "/hdbanktenant/gf", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdBankTenant hdBankTenant);




}




