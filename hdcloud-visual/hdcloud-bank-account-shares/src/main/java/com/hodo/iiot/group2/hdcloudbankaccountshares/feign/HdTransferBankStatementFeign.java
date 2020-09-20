package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdTransferBankStatement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteHdTransferBankStatement", value = ServiceNameConstants.BILL_DATA)
public interface HdTransferBankStatementFeign {



    @RequestMapping(value = "/hdtransferbankstatement/gf/all", method = RequestMethod.GET)
    R getHdTransferList(@RequestParam(value = "map") Map<String,Object> map);


    @RequestMapping(value = "/hdtransferbankstatement/gf/{id}", method = RequestMethod.GET)
    R<HdTransferBankStatement> selectById(@PathVariable("id") String id);


    @RequestMapping(value = "/hdtransferbankstatement/gf", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdTransferBankStatement hdTransferBankStatement);


    @RequestMapping(value = "/hdtransferbankstatement/gf", method = RequestMethod.PUT)
    R updateSelectiveById(@RequestBody HdTransferBankStatement hdTransferBankStatement);


    @RequestMapping(value = "/hdtransferbankstatement/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);


    //待实现
    @RequestMapping(value = "/hdtransferbankstatement/gf/getNzbankAccountListEx", method = RequestMethod.GET)
    List<HdTransferBankStatement> getNzbankAccountListEx(@RequestParam("moneyStart") String moneyStart,@RequestParam("moneyEnd")String moneyEnd,@RequestParam("startTime")String startTime,
                                                         @RequestParam("endTime") String endTime,@RequestParam("params") Map<String,Object> params,@RequestParam("paysubjectName")String paysubjectName,
                                                         @RequestParam("incomesubjectName")String incomesubjectName);


    //待实现
    @RequestMapping(value = "/hdtransferbankstatement/gf/page2", method = RequestMethod.GET)
    List<HdTransferBankStatement> getNzbankAccountList(@RequestParam("params")Map<String,Object> params);


    //待实现
    @RequestMapping(value = "/hdtransferbankstatement/gf/all2", method = RequestMethod.GET)
    List<HdTransferBankStatement> getNzbankAccountListAll(@RequestParam("params")Map<String,Object> params);


    //待实现
    @RequestMapping(value = "/hdtransferbankstatement/gf/other", method = RequestMethod.GET)
    Map<String,Object> getNzbankAccountCount(@RequestParam("params")Map<String,Object> params);


    //待处理
    @RequestMapping(value = "/hdtransferbankstatement/gf/selectMaxNo", method = RequestMethod.GET)
    BigDecimal selectMaxNo();


}




