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
package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashStatement;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.HdCashStatementService;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.MyBeanUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 11:23:50
 */
@Service
public class HdCashStatementServiceImpl extends CommonServiceImpl<HdCashStatement> implements HdCashStatementService {
    //编号格式化
    @Override
    public R page(Map<String, Object> params) {
        List<HdCashStatement> hdCashStatementList  = remoteBillService.pageCashStatement(params);
        Map<String,Object> otherVal = remoteBillService.getCashStatementOtherVal(params);
        Map<String,Object> result = new HashedMap<>();
        if(hdCashStatementList!=null&&hdCashStatementList.size()>0){
            for(HdCashStatement hdCashStatement:hdCashStatementList){
                formatNo(hdCashStatement);
            }
            result.put("records",hdCashStatementList);
        }else{
            result.put("records",new ArrayList<>());
        }
        if(otherVal!=null&&otherVal.size()>0){
            result.putAll(otherVal);
        }else{
            result.put("total",0);
            result.put("pay",0.00);
            result.put("income",0.00);
        }
        return R.ok(result);
    }

    @Override
    public R<HdCashStatement> getById(Serializable id) {

        return remoteBillService.getCashStatementById(id);
    }

    @Override
    public R updateById(HdCashStatement entity) {

        return remoteBillService.updateCashStatementById(entity);
    }

    @Override
    public R save(HdCashStatement entity) {

        return remoteBillService.addCashStatement(entity);
    }

    @Override
    public R removeById(Serializable id) {

        return remoteBillService.removeCashStatementById(id);
    }

    //格式化编号
    public void formatNo(HdCashStatement hdCashStatement){
        String no = String.valueOf(hdCashStatement.getNo());
        while(no.length()<6){
            no = "0"+no;
        }
        no = "01 "+no;
        hdCashStatement.setNoVo(no);
    }

    public List<HdCashStatement> getAllCashStatement(Map<String,Object> params){
        List<HdCashStatement> result = null;
        result = remoteBillService.getAllCashStatement(params);
        try{
            if(result!=null&&result.size()>0){
                for(HdCashStatement hdCashStatement:result){
                    formatNo(hdCashStatement);
                    HdCashStatement hdCashStatement101 = new HdCashStatement();
                    MyBeanUtils.copyBeanNotNull2Bean(hdCashStatement,hdCashStatement101);
                    hdCashStatement101.setSubjects("101现金");
                    if(hdCashStatement.getIncome()!=null&&
                            hdCashStatement.getIncome().compareTo(BigDecimal.ZERO)!=0){
                        hdCashStatement101.setPay(hdCashStatement.getIncome());
                        hdCashStatement101.setIncome(BigDecimal.ZERO);
                    }else{
                        hdCashStatement101.setIncome(hdCashStatement.getPay());
                        hdCashStatement101.setPay(BigDecimal.ZERO);
                    }
                    result.add(hdCashStatement101);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return result;


    }
}
