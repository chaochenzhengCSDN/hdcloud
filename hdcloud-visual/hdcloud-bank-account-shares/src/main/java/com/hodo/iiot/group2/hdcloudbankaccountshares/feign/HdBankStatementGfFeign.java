package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankStatement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteBankStatement", value = ServiceNameConstants.BILL_DATA)
public interface HdBankStatementGfFeign {


    @RequestMapping(value = "/hdbankstatementty/gf/{id}", method = RequestMethod.GET)
    R<HdBankStatement> selectById(@PathVariable("id") String id);


    @RequestMapping(value = "/hdbankstatementty/gf/all", method = RequestMethod.GET)
    List<HdBankStatement> getHdBankStatementGf(@RequestParam(value = "map") Map<String,Object> map);


    @RequestMapping(value = "/hdbankstatementty/gf", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdBankStatement hdBankStatement);


    @RequestMapping(value = "/hdbankstatementty/gf", method = RequestMethod.PUT)
    R updateSelectiveById(@RequestBody HdBankStatement hdBankStatement);


    @PostMapping(value = "/hdbankstatementty/gf/addList")
    R addList(@RequestBody List<HdBankStatement> list);


    @RequestMapping(value = "/hdbankstatementty/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);


    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/selectMaxNo", method = RequestMethod.GET)
    Integer selectMaxNoFreeTime(@RequestParam("accountDate") String accountDate);

    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/selectMaxNo", method = RequestMethod.GET)
    BigDecimal selectMaxNo();

    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/searchRemark", method = RequestMethod.GET)
    List<String> searchRemark(@RequestParam("tenantId") String tenantId,@RequestParam("userId") String userId);


    @RequestMapping(value = "/hdbankstatementty/gf/delete", method = RequestMethod.DELETE)
    Boolean delete(@RequestBody Map<String,Object> map);


    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/getSaveCount", method = RequestMethod.GET)
    Integer getSaveCount(@RequestParam("startTime")String startTime,@RequestParam("endTime") String endTime,@RequestParam("tenantId") Integer tenantId);


    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/getSheetIdsInBankAccount", method = RequestMethod.GET)
    List<String> getSheetIdsInBankAccount();

    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/getSheetIdsInBankPend", method = RequestMethod.GET)
    List<String> getSheetIdsInBankPend();


    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/getRIdsInBankAccount", method = RequestMethod.GET)
    List<String> getRIdsInBankAccount();


    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/getRIdsInBankPend", method = RequestMethod.GET)
    List<String> getRIdsInBankPend();


    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/page2", method = RequestMethod.GET)
    List<HdBankStatement> getBankAccountListBySql(@RequestParam Map<String, Object> map);



    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/all2", method = RequestMethod.GET)
    List<HdBankStatement> getAllBankAccountBySql(@RequestParam("params") Map<String, Object> params);



    //待处理
    @RequestMapping(value = "/hdbankstatementty/gf/other", method = RequestMethod.GET)
    Map<String,Object> getOtherVal(@RequestParam("params") Map<String, Object> params);






}




