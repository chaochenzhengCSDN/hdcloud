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
package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAllocation;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankAllocationService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.FileUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Service
public class HdBankAllocationServiceImpl extends CommonServiceImpl<HdBankAllocation> implements HdBankAllocationService {

    @Override
    public R<HdBankAllocation> page(Map<String, Object> params) {

        return remoteBillService.pageBankAllocation(params);
    }

    @Override
    public R<HdBankAllocation> getById(Serializable id) {

        return remoteBillService.getBankAllocationById(id);
    }

    @Override
    public R updateById(HdBankAllocation entity) {

        return remoteBillService.updateBankAllocationById(entity);
    }

    @Override
    public R save(HdBankAllocation entity) {

        return remoteBillService.saveBankAllocation(entity);
    }

    @Override
    public R removeById(Serializable id) {

        return remoteBillService.removeBankAllocationById(id);
    }


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
                result.put(hdBankTenant.getTenantName(),getValueList(hdBankTenant.getTenantId().toString(),allDicts));
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
