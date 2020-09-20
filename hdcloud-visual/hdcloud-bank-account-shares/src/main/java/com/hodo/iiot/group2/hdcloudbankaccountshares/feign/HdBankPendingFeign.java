package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteBankPending", value = ServiceNameConstants.BILL_DATA)
public interface HdBankPendingFeign {

    @RequestMapping(value = "/hdbankpending/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);

    @RequestMapping(value = "/hdbankpending/gf/{id}", method = RequestMethod.GET)
    R<HdBankPending> selectById(@PathVariable("id") String id);

    @RequestMapping(value = "/hdbankpending/gf", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdBankPending hdBankPending);


    @RequestMapping(value = "/hdbankpending/gf", method = RequestMethod.PUT)
    R updateById(@RequestBody HdBankPending hdBankPending);


    @RequestMapping(value = "/hdbankpending/gf/addList", method = RequestMethod.POST)
    Boolean addList(@RequestBody List<HdBankPending> list);


    //待处理
    @RequestMapping(value = "/hdbankpending/gf/getBankPendingList", method = RequestMethod.GET)
    List<HdBankPending> getBankPendingList(@RequestParam("moneyStart")String moneyStart,@RequestParam("moneyEnd")String moneyEnd,@RequestParam("startTime")String startTime,
                                           @RequestParam("endTime")String endTime, @RequestParam("page")String page, @RequestParam("limit")String limit,
                                           @RequestParam("tenantId")String tenantId, @RequestParam("params") Map<String, Object> params,@RequestParam("bankName")String bankName);


    @GetMapping(value = "/hdbankpending/gf/page2")
    List<HdBankPending> getBankPendingList2(@RequestParam("params") Map<String, Object> params);



    @RequestMapping(value = "/hdbankpending/gf/other", method = RequestMethod.GET)
    Map<String,Object> getOtherVal(@RequestParam("params") Map<String, Object> params);


    //待处理
    @RequestMapping(value = "/hdbankpending/gf/all2", method = RequestMethod.GET)
    List<HdBankPending> getAllBankPending(@RequestParam("params") Map<String, Object> params);










}




