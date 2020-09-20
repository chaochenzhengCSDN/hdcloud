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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdTransferBankStatementService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * @date 2019-12-04 09:44:08
 */
@Service
public class HdTransferBankStatementServiceImpl implements HdTransferBankStatementService {
    @Autowired
    private RemoteBillService remoteBillService;
    @Autowired
    private RemoteBaseService remoteBaseService;
    @Autowired
    private HdBankStatementService hdBankStatementService;
    @Override
    public R page(Map<String, Object> params) {
        Map<String,Object> result = new HashedMap<>();
        List<HdTransferBankStatement> hdTransferBankStatementList = remoteBillService.pageNzAccount(params);
        Map<String,Object> otherVal = remoteBillService.getNzOtherVal(params);
        if(hdTransferBankStatementList!=null&&hdTransferBankStatementList.size()>0){
            result.put("records",hdTransferBankStatementList);
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
    public R<HdTransferBankStatement> getById(Serializable id) {

        return remoteBillService.getNzAccountById(id);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R updateById(HdTransferBankStatement entity) {
        remoteBillService.updateNzAccount(entity);
        remoteBillService.deleteBySourceId(entity.getId());
        this.save(entity);
        return R.ok(null,"编辑成功！");
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R save(HdTransferBankStatement entity) {
        BigDecimal money = entity.getMoney();
        if(money==null||"".equals(money)){
            money = BigDecimal.ZERO;
        }
        Integer no = remoteBillService.selectMaxNo(null);
        entity.setNo(new BigDecimal(no));
        //内转账单添加
        remoteBillService.addNzAccount(entity);
        //账单添加,先封装后添加
        addHdBankStatements(entity);
        return R.ok(null,"保存成功!");
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R removeById(Serializable id) {
        remoteBillService.removeNzAccountById(id);
        remoteBillService.deleteBySourceId(id);
        return R.ok(null,"删除成功！");
    }

    //账单添加
    public void addHdBankStatements(HdTransferBankStatement hdTransferBankStatement){
        HdBankStatement incomeStatement = packIncome(hdTransferBankStatement);
        HdBankStatement payStatement = packPay(hdTransferBankStatement);
        List<HdBankStatement> bankStatementList = new ArrayList<>();
        bankStatementList.add(incomeStatement);
        bankStatementList.add(payStatement);
        remoteBillService.addBankStatementList(bankStatementList);
    }

    //封装账单收入
    public HdBankStatement packIncome(HdTransferBankStatement hdTransferBankStatement) {
        HdBankStatement hdBankStatement = new HdBankStatement();
        //凭证编号重新生成
        //借方科目就是借方公司
        hdBankStatement.setCompanyId(hdTransferBankStatement.getIncomeSubjectId());
        //对方科目
        hdBankStatement.setSubjects(hdTransferBankStatement.getPaySubjectName());
        hdBankStatement.setIncome(hdTransferBankStatement.getMoney());
        hdBankStatement.setPay(BigDecimal.ZERO);
        hdBankStatement.setSourceId(hdTransferBankStatement.getId());
        hdBankStatement.setAccountDate(new Date());
        hdBankStatement.setSynaccountDate(new Date());
        hdBankStatement.setSourceType("3");
        hdBankStatement.setAccountType("2");
        hdBankStatement.setRemark(hdTransferBankStatement.getRemark());
        hdBankStatement.setNo(hdTransferBankStatement.getNo());
        hdBankStatement.setBy2(String.valueOf(hdTransferBankStatement.getNo().intValue()+0.1));
        //根据开户行获取科目，我方对方一样
        //根据开户行获取对应银行
        HdBankAccount hdBankAccount = remoteBaseService.getBankAccountById(
                hdTransferBankStatement.getBankAccountId()).getData();
        if(hdBankAccount!=null){
            hdBankStatement.setBankName(hdBankAccount.getBankName());
            hdBankStatement.setBankAccountId(hdBankAccount.getId());
        }
        return hdBankStatement;
    }

    //封装账单支出
    public HdBankStatement packPay(HdTransferBankStatement hdTransferBankStatement) {
        HdBankStatement hdBankStatement = new HdBankStatement();
        //凭证编号重新生成
        hdBankStatement.setCompanyId(hdTransferBankStatement.getPaySubjectId());
        //对方科目
        hdBankStatement.setSubjects(hdTransferBankStatement.getIncomeSubjectName());
        hdBankStatement.setIncome(BigDecimal.ZERO);
        hdBankStatement.setPay(hdTransferBankStatement.getMoney());
        hdBankStatement.setSourceId(hdTransferBankStatement.getId());
        hdBankStatement.setAccountDate(new Date());
        hdBankStatement.setSynaccountDate(new Date());
        hdBankStatement.setSourceType("3");
        hdBankStatement.setAccountType("2");
        hdBankStatement.setRemark(hdTransferBankStatement.getRemark());
        hdBankStatement.setNo(hdTransferBankStatement.getNo());
        hdBankStatement.setBy2(String.valueOf(hdTransferBankStatement.getNo().intValue()+0.2));
        HdBankAccount hdBankAccount = remoteBaseService.getBankAccountById(
                hdTransferBankStatement.getBankAccountId()).getData();
        if(hdBankAccount!=null){
            hdBankStatement.setBankName(hdBankAccount.getBankName());
            hdBankStatement.setBankAccountId(hdBankAccount.getId());
        }
        return hdBankStatement;
    }

    /********************************批量添加*************************************/
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void batchSave(List<HdTransferBankStatement> hdTransferBankStatements){
        Integer no = hdBankStatementService.getNoMaxNumber(null);
        for(HdTransferBankStatement hdTransferBankStatement:hdTransferBankStatements){
            hdTransferBankStatement.setNo(new BigDecimal(no));
            remoteBillService.addNzAccount(hdTransferBankStatement);
            addHdBankStatements(hdTransferBankStatement);
            no++;
        }
    }

    public List<HdTransferBankStatement> formatAllNo(List<HdTransferBankStatement> hdTransferBankStatements){
        if(hdTransferBankStatements!=null&&hdTransferBankStatements.size()>0){
            for(HdTransferBankStatement hdTransferBankStatement:hdTransferBankStatements){
                formatNo(hdTransferBankStatement,6);
            }
        }
        return hdTransferBankStatements;
    }

    public void formatNo(HdTransferBankStatement hdTransferBankStatement,Integer num){
        String no = String.valueOf(hdTransferBankStatement.getNo());
        while(no.length()<num){
            no = "0"+no;
        }
        hdTransferBankStatement.setBy1(no);
    }


}
