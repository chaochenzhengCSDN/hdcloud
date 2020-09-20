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

package com.hodo.iiot.group2.hdcloud.bank.account.currency.feign;


import com.hodo.iiot.group2.hdcloud.bank.account.currency.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.*;
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

    //调拨单接口
    @GetMapping("/hdbankallocation/page")
    public R pageBankAllocation(@RequestParam() Map<String,Object> params);
    //查询id
    @GetMapping("/hdbankallocation/{id}")
    public R getBankAllocationById(@PathVariable("id") Serializable id);
    //编辑
    @PutMapping("/hdbankallocation")
    public R updateBankAllocationById(@RequestBody HdBankAllocation entity);
    //增加
    @PostMapping("/hdbankallocation")
    public R saveBankAllocation(@RequestBody HdBankAllocation entity);
    //删除
    @DeleteMapping("/hdbankallocation/{id}")
    public R removeBankAllocationById(@PathVariable("id") Serializable id);
    //批量删除
    @PostMapping ("/hdbankallocation/removeByIds")
    R batchDelBankAllocation(@RequestBody List<String> ids);
    //查询所有导出
    @RequestMapping(value = "/hdbankallocation/all",method = RequestMethod.GET)
    public List<HdBankAllocation> getAllBankAllocation(@RequestParam() Map<String,Object> params);
    //批量添加
    @RequestMapping(value = "/hdbankallocation/addList",method = RequestMethod.POST)
    R addBankAllocationList(@RequestBody List<HdBankAllocation> hdBankAllocationList);


    //待处理接口
    @GetMapping("/hdbankpending/page2")
    public List<HdBankPending> pageBankPending(@RequestParam() Map<String,Object> params);
    //获取分页中的总计数据
    @GetMapping("/hdbankpending/other")
    public Map<String,Object> getBankPendingOtherVal(@RequestParam() Map<String,Object> params);

    //查询id
    @GetMapping("/hdbankpending/{id}")
    public R<HdBankPending> getBankPendingById(@PathVariable("id") Serializable id);
    //编辑
    @PutMapping("/hdbankpending")
    public R updateBankPendingById(@RequestBody HdBankPending entity);
    //增加
    @PostMapping("/hdbankpending")
    public R addBankPending(@RequestBody HdBankPending entity);
    //删除
    @DeleteMapping("/hdbankpending/{id}")
    public R removeBankPendingById(@PathVariable("id") Serializable id);
    //批量增加待处理
    @PostMapping(value = "/hdbankpending/addList")
    public R addBankPendingList(@RequestBody List<HdBankPending> hdBankPendingList);
    //账单批量删除
    @RequestMapping(value = "/hdbankpending/removeByIds",method = RequestMethod.POST)
    Integer batchDelBankPending(@RequestBody List<String> ids);
    //导出全查待处理
    @GetMapping(value = "/hdbankpending/all2")
    public List<HdBankPending> getAllBankPending(@RequestParam() Map<String,Object> params);









    //处理记录接口
    @GetMapping("/hdbankrecord/page2")
    public List<HdBankRecord> pageBankRecord(@RequestParam() Map<String,Object> params);
    //获取分页中的总计数据
    @GetMapping("/hdbankrecord/other")
    public Map<String,Object> getBankRecordOtherVal(@RequestParam() Map<String,Object> params);
    //批量添加
    @RequestMapping(value = "/hdbankrecord/addList",method = RequestMethod.POST)
    void addBankRecordList(@RequestBody List<HdBankRecord> hdBankRecordList);
    //批量删除
    @RequestMapping(value = "/hdbankrecord/removeByIds",method = RequestMethod.POST)
    R batchDelBankRecord(@RequestBody List<String> ids);
    //导出全查操作记录
    @RequestMapping(value = "/hdbankrecord/all2",method = RequestMethod.GET)
    public List<HdBankRecord> getAllBankRecord(@RequestParam() Map<String,Object> params);
    @GetMapping(value = "/hdbankrecord/{id}")
    public R<HdBankRecord> getBankRecordById(@PathVariable("id") Serializable id);
    @PutMapping(value = "/hdbankrecord")
    public R updateBankRecordById(@RequestBody HdBankRecord entity);
    @PostMapping(value = "/hdbankrecord")
    public R saveBankRecord(@RequestBody HdBankRecord entity);
    @DeleteMapping(value = "/hdbankrecord/{id}")
    public R removeBankRecordById(@PathVariable("id") Serializable id);





    //银行账单接口
    @GetMapping(value="/hdbankstatementty/page2")
    public List<HdBankStatement> pageBankStatement(@RequestParam() Map<String,Object> params);
    //分页查询其他值
    @RequestMapping(value = "/hdbankstatementty/other",method = RequestMethod.GET)
    public Map<String,Object> getBankStatementOtherVal(@RequestParam() Map<String,Object> params);
    //连表全查
    @RequestMapping(value = "/hdbankstatementty/all2",method = RequestMethod.GET)
    public List<HdBankStatement> getAllBankStatement(@RequestParam() Map<String,Object> params);
    //不连表加入条件查询
    @RequestMapping(value = "/hdbankstatementty/all",method = RequestMethod.GET)
    public List<HdBankStatement> getAllSingle(@RequestParam() Map<String,Object> params);

    @RequestMapping(value = "/hdbankstatementty/getBalance",method = RequestMethod.GET)
    public String getBeginByEveStatement(@RequestParam("accountId") String accountId,
                                         @RequestParam("accountDate") String accountDate,@RequestParam("by2") String by2);
    @GetMapping(value = "/hdbankstatementty/{id}")
    public R<HdBankStatement> getBankStatementById(@PathVariable("id") Serializable id);
    @PutMapping(value = "/hdbankstatementty")
    public R updateBankStatementById(@RequestBody HdBankStatement entity);
    @PostMapping(value = "/hdbankstatementty")
    public R saveBankStatement(@RequestBody HdBankStatement entity);
    @DeleteMapping(value = "/hdbankstatementty/{id}")
    public R removeBankStatementById(@PathVariable("id") Serializable id);
    @RequestMapping(value = "/hdbankstatementty/selectMaxNo",method = RequestMethod.GET)
    Integer selectMaxNo(@RequestParam("accountDate") String accountDate);
    //账单批量删除
    @PostMapping(value = "/hdbankstatementty/removeByIds")
    R batchDelBankStatement(@RequestBody List<String> list);
    @RequestMapping(value = "/hdbankstatementty/searchRemark",method = RequestMethod.GET)
    List<String> searchRemark();
    @PostMapping(value = "/hdbankstatementty/addList")
    R<String> addBankStatementList(@RequestBody List<HdBankStatement> list);
    @RequestMapping(value = "/hdbankstatementty/selStatementBySheet",method = RequestMethod.GET)
    List<HdBankStatement> selStatementBySheet(@RequestParam("sheetid") String sheetid);
    @RequestMapping(value = "/hdbankstatementty/selStatementByRid",method = RequestMethod.GET)
    List<HdBankStatement> selStatementByRid(@RequestParam("rid") String rid);
    @RequestMapping(value = "/hdbankstatementty/getAllSheetIds",method = RequestMethod.GET)
    //获取所有的sheetids用于同步
    public List<String> getAllSheetIds();
    @RequestMapping(value = "/hdbankstatementty/getAllRids",method = RequestMethod.GET)
    //获取所有的rids用于同步
    public List<String> getAllRids();
//    @RequestMapping(value = "/hdbankstatementty/batchDelBankRecord",method = RequestMethod.POST)
//    void deleteBankAccountById(String id);
    @RequestMapping(value = "/hdbankstatementty/getSaveCount",method = RequestMethod.POST)
    //获取时间内同步前后账单的数量
    public Integer getSaveCount(@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime);
    //根据内部账单删除账单记录
    @RequestMapping(value = "/hdbankstatementty/deleteBySourceId",method = RequestMethod.GET)
    void deleteBySourceId(@RequestParam("sourceId") Serializable sourceId);
    @RequestMapping(value = "/hdbankstatementty/getBankStatementByAccount",method = RequestMethod.GET)
    List<HdBankStatement> getBankStatementByAccount(@RequestParam("accountId") String accountId);


    //租户管理

    //成员单位匹配

    //公司管理

    //成员单位管理

    //内转账单管理
    //添加
    @GetMapping(value="/hdtransferbankstatement/page2",consumes="application/json")
    public List<HdTransferBankStatement> pageNzAccount(@RequestParam() Map<String,Object> params);

    //获取分页中的总计数据
    @GetMapping(value="/hdtransferbankstatement/other",consumes="application/json")
    public Map<String,Object> getNzOtherVal(@RequestParam() Map<String,Object> param);
    @PostMapping(value = "/hdtransferbankstatement")
    R addNzAccount(@RequestBody HdTransferBankStatement hdTransferBankStatement);
    //编辑
    @PutMapping(value = "/hdtransferbankstatement")
    R updateNzAccount(@RequestBody HdTransferBankStatement hdTransferBankStatement);
    //删除
    @DeleteMapping(value = "/hdtransferbankstatement/{id}")
    R removeNzAccountById(@PathVariable("id") Serializable id);
    //查询
    @GetMapping("/hdtransferbankstatement/{id}")
    public R<HdTransferBankStatement> getNzAccountById(@PathVariable("id") Serializable id);
    @RequestMapping(value = "/hdtransferbankstatement/all2",method = RequestMethod.GET)
    List<HdTransferBankStatement> getAllNzbankAccountList(@RequestParam("params") Map<String,Object> params);
    //查询九恒星数据
    @RequestMapping(value = "/syncData",method = RequestMethod.POST)
    List<List<Object>> syncData(@RequestBody Map<String, Object> params);




}
