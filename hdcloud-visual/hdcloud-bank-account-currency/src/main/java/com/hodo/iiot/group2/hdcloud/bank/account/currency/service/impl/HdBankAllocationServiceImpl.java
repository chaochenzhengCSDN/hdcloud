/*
 *    Copyright (c) 2018-2025, hodo All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: 江苏红豆工业互联网有限公司
 */
package com.hodo.iiot.group2.hdcloud.bank.account.currency.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.hodo.hdcloud.common.core.util.R;

import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAllocation;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankAllocationService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.FileUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@Service
public class HdBankAllocationServiceImpl implements HdBankAllocationService {

    @Autowired
    private RemoteBillService remoteBillService;
    @Autowired
    private RemoteBaseService remoteBaseService;
    private synchronized String getPrintMaxNo() throws Exception {
        JSONObject jb = FileUtil.readJsonFromClassPath(FileUtil.RESUME_TEMPLATE);
        if(jb==null){
            throw new Exception();
        }
        Integer maxNo = (Integer)jb.get("maxNo");
        Map<String,Integer> map = new HashedMap();
        map.put("maxNo",maxNo+1);
        String maxNoStr = this.formatNo(maxNo+"");
        Boolean result = FileUtil.writeJsonFromClassPath(FileUtil.RESUME_TEMPLATE,JSONObject.parseObject(JSONObject.toJSONString(map)));
        System.out.println(maxNo+"@@@@@@@@@最大值");
        if(!result){
            throw new Exception();
        }
        return maxNoStr;
    }
    private String formatNo(String num){
        while(num.length()<7){
            num = "0"+num;
        }
        return num;
    }
    public R<HdBankAllocation> page(Map<String,Object> params) {

        return remoteBillService.pageBankAllocation(params);
    }
    //查询id
    public R getById(Serializable id){
        return remoteBillService.getBankAllocationById(id);
    }
    //编辑
    public R updateById(HdBankAllocation entity){
        return remoteBillService.updateBankAllocationById(entity);
    }
    //增加
    public R save(HdBankAllocation entity){

        return remoteBillService.saveBankAllocation(entity);
    }

    //删除
    public R removeById(Serializable id){
        return remoteBillService.removeBankAllocationById(id);
    }

    //批量增加
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R batchSave(List<HdBankAllocation> hdBankAllocations) throws Exception {
        R result = new R();
        for(HdBankAllocation hdBankAllocation:hdBankAllocations){
            //hdBankAllocation.setNumber(getPrintMaxNo());
            hdBankAllocation.setNumber("1");
        }
       return remoteBillService.addBankAllocationList(hdBankAllocations);
    }

    public Map<String,List<HdBankAccount>> getTenantAndDict(){
        Map<String,List<HdBankAccount>> result = null;
        //租户映射表全查
        List<HdBankTenant> allBankTenants = remoteBaseService.selectAllTenants();
        //账户全查
        Map<String,Object> params = new HashedMap();
        List<HdBankAccount> allDicts = remoteBaseService.selectListAll(params);
        //生成对应的租户和账号关系
        if(allBankTenants!=null&&allBankTenants.size()>0){
            result = new HashedMap();
            for(HdBankTenant hdBankTenant:allBankTenants){
                result.put(hdBankTenant.getTenantName(),getValueList(hdBankTenant.getTenantId(),allDicts));
            }
        }
        return result;

    }

    private List<HdBankAccount> getValueList(String tenantId,List<HdBankAccount> allBankAccounts){
        List<HdBankAccount> valueList = new ArrayList<>();
        for(HdBankAccount hdBankAccount:allBankAccounts){
            if(hdBankAccount.getTenantId().equals(tenantId)){
                valueList.add(hdBankAccount);
                //allDicts.remove(hdNzDict);
            }
        }
        return valueList;
    }



}
