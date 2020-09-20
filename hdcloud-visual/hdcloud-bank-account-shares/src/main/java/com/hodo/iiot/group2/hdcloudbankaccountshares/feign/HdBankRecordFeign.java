package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteBankRecord", value = ServiceNameConstants.BILL_DATA)
public interface HdBankRecordFeign {

    @RequestMapping(value = "/hdbankrecord/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);

    @RequestMapping(value = "/hdbankrecord/gf/addList", method = RequestMethod.POST)
    Boolean addList(@RequestBody List<HdBankRecord> list);

    @RequestMapping(value = "/hdbankrecord/gf", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdBankRecord hdBankRecord);


    //待处理
    @RequestMapping(value = "/hdbankrecord/gf/all2", method = RequestMethod.GET)
    List<HdBankRecord> getAllBankRecord(@RequestParam("params") Map<String, Object> params);

    //待处理
    @RequestMapping(value = "/hdbankrecord/gf/", method = RequestMethod.GET)
    List<HdBankRecord> getHdBankRecordList(@RequestParam("moneyStart")String moneyStart,@RequestParam("moneyEnd")String moneyEnd,@RequestParam("companyName")String companyName, @RequestParam("startTime")String startTime,
                                           @RequestParam("endTime")String endTime, @RequestParam("page")String page, @RequestParam("limit")String limit,
                                           @RequestParam("tenantId")String tenantId,@RequestParam("params") Map<String, Object> params,@RequestParam("num")String num,@RequestParam("accountId")String accountId);


    //待处理
    @GetMapping(value = "/hdbankrecord/gf/page2")
    List<HdBankRecord> getHdBankRecordList2(@RequestParam("params") Map<String, Object> params);





}




