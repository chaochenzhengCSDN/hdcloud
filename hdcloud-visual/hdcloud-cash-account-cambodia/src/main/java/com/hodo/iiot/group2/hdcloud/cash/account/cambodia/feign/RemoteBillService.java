/*
 *
 *      Copyright (c) 2018-2025, hodo All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: 江苏红豆工业互联网有限公司
 *
 */

package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.feign;


import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashStatement;
import org.springframework.cloud.openfeign.FeignClient;
import com.hodo.hdcloud.common.core.util.R;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hodo
 * @date 2018/6/28
 */
@FeignClient(contextId = "remoteBillService", value = ServiceNameConstants.BILL_DATA)
public interface RemoteBillService{


    //银行账单接口
    @GetMapping(value="/hdcashstatement/page2")
    public List<HdCashStatement> pageCashStatement(@RequestParam() Map<String, Object> params);
    //分页查询其他值
    @RequestMapping(value = "/hdcashstatement/other",method = RequestMethod.GET)
    public Map<String,Object> getCashStatementOtherVal(@RequestParam() Map<String, Object> params);
    //连表全查
    @RequestMapping(value = "/hdcashstatement/all2",method = RequestMethod.GET)
    public List<HdCashStatement> getAllCashStatement(@RequestParam() Map<String, Object> params);
    //不连表加入条件查询
    @RequestMapping(value = "/hdcashstatement/all",method = RequestMethod.GET)
    public List<HdCashStatement> getAllSingle(@RequestParam() Map<String, Object> params);

    @RequestMapping(value = "/hdcashstatement/getBalance",method = RequestMethod.GET)
    public String getBeginByEveStatement(@RequestParam("accountId") String accountId,
                                         @RequestParam("accountDate") String accountDate, @RequestParam("by2") String by2);
    @GetMapping(value = "/hdcashstatement/{id}")
    public R<HdCashStatement> getCashStatementById(@PathVariable("id") Serializable id);
    @PutMapping(value = "/hdcashstatement")
    public R updateCashStatementById(@RequestBody HdCashStatement entity);
    @PostMapping(value = "/hdcashstatement")
    public R addCashStatement(@RequestBody HdCashStatement entity);
    @DeleteMapping(value = "/hdcashstatement/{id}")
    public R removeCashStatementById(@PathVariable("id") Serializable id);
    @RequestMapping(value = "/hdcashstatement/selectMaxNo",method = RequestMethod.GET)
    Integer selectMaxNo(@RequestParam("accountDate") String accountDate);
    //账单批量删除
    @PostMapping(value = "/hdcashstatement/removeByIds")
    R batchDelCashStatement(@RequestBody List<String> list);
    @RequestMapping(value = "/hdcashstatement/searchRemark",method = RequestMethod.GET)
    List<String> searchRemark();
    @PostMapping(value = "/hdcashstatement/addList")
    R<String> addCashStatementList(@RequestBody List<HdCashStatement> list);
    @RequestMapping(value = "/hdcashstatement/selStatementBySheet",method = RequestMethod.GET)
    List<HdCashStatement> selStatementBySheet(@RequestParam("sheetid") String sheetid);
    @RequestMapping(value = "/hdcashstatement/selStatementByRid",method = RequestMethod.GET)
    List<HdCashStatement> selStatementByRid(@RequestParam("rid") String rid);
    @RequestMapping(value = "/hdcashstatement/getAllSheetIds",method = RequestMethod.GET)
    //获取所有的sheetids用于同步
    public List<String> getAllSheetIds();
    @RequestMapping(value = "/hdcashstatement/getAllRids",method = RequestMethod.GET)
    //获取所有的rids用于同步
    public List<String> getAllRids();
//    @RequestMapping(value = "/hdcashstatement/batchDelCashRecord",method = RequestMethod.POST)
//    void deleteCashAccountById(String id);
    @RequestMapping(value = "/hdcashstatement/getSaveCount",method = RequestMethod.POST)
    //获取时间内同步前后账单的数量
    public Integer getSaveCount(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime);
    //根据内部账单删除账单记录
    @RequestMapping(value = "/hdcashstatement/deleteBySourceId",method = RequestMethod.GET)
    void deleteBySourceId(@RequestParam("sourceId") Serializable sourceId);
    @RequestMapping(value = "/hdcashstatement/getCashStatementByAccount",method = RequestMethod.GET)
    List<HdCashStatement> getCashStatementByAccount(@RequestParam("accountId") String accountId);


    //租户管理

    //成员单位匹配

    //公司管理

    //成员单位管理

    //内转账单管理





}
