package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteBankAccount", value = ServiceNameConstants.BASE_DATA)
public interface HdBankAccountFeign {

    @RequestMapping(value = "/hdbankaccount/gf/{id}", method = RequestMethod.GET)
    R<HdBankAccount> selectById(@PathVariable("id") String id);

    @RequestMapping(value = "/hdbankaccount/gf", method = RequestMethod.POST)
    R<String> insertSelective(@RequestBody HdBankAccount hdBankAccount);

    @RequestMapping(value = "/hdbankaccount/gf/{id}", method = RequestMethod.DELETE)
    R deleteById(@PathVariable("id") String id);

    @RequestMapping(value = "/hdbankaccount/gf/", method = RequestMethod.PUT)
    R updateSelectiveById(@RequestBody HdBankAccount hdBankAccount);

    @RequestMapping(value = "/hdbankaccount/gf/page", method = RequestMethod.GET)
    R getHdBankAccountPage(@RequestParam Map<String,Object> map);

    @RequestMapping(value = "/hdbankaccount/gf/all", method = RequestMethod.GET)
    List<HdBankAccount> getHdBankAccountList(@RequestParam Map<String,Object> map);

    @RequestMapping(value = "/hdbankaccount/gf/addList", method = RequestMethod.POST)
    R batchSave(@RequestBody List<HdBankAccount> list);

    @RequestMapping(value = "/hdbankaccount/gf/all", method = RequestMethod.GET)
    List<HdBankTenant> getTenantIdByName(@RequestParam Map<String,Object> map);

    @RequestMapping(value = "/hdbankaccount/gf/selectStringList", method = RequestMethod.GET)
    List<String > searchBankByCode(@RequestParam("str") String str,@RequestParam("param")String param,@RequestParam("val")String val);

    @RequestMapping(value = "/hdbankaccount/gf/selectStringList", method = RequestMethod.GET)
    List<String > searchBankByName(@RequestParam("str") String str,@RequestParam("param")String param,@RequestParam("val")String val);



    //待实现
//    @RequestMapping(value = "/hdbankaccount/getIdByTerm", method = RequestMethod.GET)
//    String getIdByTerm(@PathVariable("internalBankAccountId")String internalBankAccountId,@PathVariable("externalBankAccountId")String externalBankAccountId,
//                       @PathVariable("tenantId")Integer tenantId);


    //待实现
//    @RequestMapping(value = "/hdbankaccount/getBankNameByWZId", method = RequestMethod.GET)
//    String getBankNameByWZId(@PathVariable("externalBankAccountId") String externalBankAccountId, @PathVariable("userId") String userId,
//                             @PathVariable("tenantId")Integer tenantId);


    //待实现
//    @RequestMapping(value = "/hdbankaccount/", method = RequestMethod.GET)
//    String getNzDictByWbzh(@PathVariable("externalBankAccountId") String externalBankAccountId,
//                             @PathVariable("tenantId")Integer tenantId);


}




