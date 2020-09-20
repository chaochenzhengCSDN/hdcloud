package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdLoanAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.PageEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteHdLoanAccount", value = ServiceNameConstants.BILL_DATA)
public interface HdLoanAccountFeign {



    @RequestMapping(value = "/hdloanaccount/gf/all", method = RequestMethod.GET)
    R getHdLoanAccountList(@RequestParam(value = "map") Map<String, Object> map);


    @RequestMapping(value = "/hdloanaccount/gf/{id}", method = RequestMethod.GET)
    R<HdLoanAccount> selectById(@PathVariable("id") String id);


    @RequestMapping(value = "/hdloanaccount/gf", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdLoanAccount hdLoanAccount);


    @RequestMapping(value = "/hdloanaccount/gf", method = RequestMethod.PUT)
    R updateSelectiveById(@RequestBody HdLoanAccount hdLoanAccount);


    @RequestMapping(value = "/hdloanaccount/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);


    //待实现
    @GetMapping(value = "/hdloanaccount/gf/page")
    R getHdLoadAccount(@RequestParam("params")Map<String,Object> params);


    //待实现
    @GetMapping(value = "/hdloanaccount/gf/all")
    List<HdLoanAccount> getTotal(@RequestParam("params")Map<String,Object> params);



    //待实现
    @GetMapping(value = "/hdloanaccount/gf/all")
    List<HdLoanAccount> getAllHdLoadAccounts(@RequestParam("params")Map<String,Object> params);




}




