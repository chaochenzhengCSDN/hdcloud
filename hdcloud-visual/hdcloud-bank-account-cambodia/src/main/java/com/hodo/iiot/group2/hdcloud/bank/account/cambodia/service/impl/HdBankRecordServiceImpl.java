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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankRecordService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Service
public class HdBankRecordServiceImpl extends CommonServiceImpl<HdBankRecord> implements HdBankRecordService {

    @Override
    public R page(Map<String, Object> params) {
        Map<String,Object> result = new HashedMap<>();
        List<HdBankRecord> hdBankRecordList = remoteBillService.pageBankRecord(params);
        Map<String,Object> otherVal = remoteBillService.getBankRecordOtherVal(params);
        if(hdBankRecordList!=null&&hdBankRecordList.size()>0){
            for(HdBankRecord hdBankRecord:hdBankRecordList){
                formatNo(hdBankRecord);
            }
            result.put("records",hdBankRecordList);
        }else{
            result.put("records",new ArrayList<>());
        }
        if(otherVal!=null&&otherVal.size()>0){
            result.putAll(otherVal);
        }else{
            result.put("total",0);
        }
        return R.ok(result);
    }

    @Override
    public R<HdBankRecord> getById(Serializable id) {

        return remoteBillService.getBankRecordById(id);
    }

    @Override
    public R updateById(HdBankRecord entity) {

        return remoteBillService.updateBankRecordById(entity);
    }

    @Override
    public R save(HdBankRecord entity) {

        return remoteBillService.saveBankRecord(entity);
    }

    @Override
    public R removeById(Serializable id) {
        return remoteBillService.removeBankRecordById(id);
    }

    /**
     * 封装操作记录
     */
    public HdBankRecord getHdBankRecord(HdBankStatement hdBankStatement, String type) {
        boolean flag = true;
        if (hdBankStatement.getPay() != null) {
            BigDecimal bpay = hdBankStatement.getPay() != null ? hdBankStatement.getPay() : BigDecimal.ZERO;
            flag = bpay.compareTo(BigDecimal.ZERO) == 0;
        }
        HdBankRecord hdBankRecord = new HdBankRecord();
        hdBankRecord.setCompanyId(hdBankStatement.getCompanyId());
        hdBankRecord.setFlag(flag ? "0" : "1");
        hdBankRecord.setNumber(hdBankStatement.getNo() != null ? hdBankStatement.getNo().toString() :
                "");
        hdBankRecord.setMoney(flag ? hdBankStatement.getIncome() != null ? hdBankStatement.getIncome().toString() :
                "" : hdBankStatement.getPay() != null ? hdBankStatement.getPay().toString() :
                "");
        //新增賬戶信息
        hdBankRecord.setBankAccountId(hdBankStatement.getBankAccountId());
        hdBankRecord.setRemark(hdBankStatement.getRemark());
        hdBankRecord.setOperType(type);
        hdBankRecord.setCreateTime(new Date());
        return hdBankRecord;
    }


    //导出
    public void formatNo(HdBankRecord hdBankRecord){
        String no = String.valueOf(hdBankRecord.getNumber());
        while(no.length()<6){
            no = "0"+no;
        }
        hdBankRecord.setBy1(no);
    }
}
