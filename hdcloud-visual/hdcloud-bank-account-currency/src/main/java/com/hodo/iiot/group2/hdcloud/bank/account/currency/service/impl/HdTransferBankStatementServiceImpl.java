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


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdTransferBankStatementService;
import org.apache.commons.collections.map.HashedMap;
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
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@Service
public class HdTransferBankStatementServiceImpl implements HdTransferBankStatementService {
    @Autowired
    private RemoteBillService remoteBillService;
    @Autowired
    private RemoteBaseService remoteBaseService;
    @Autowired
    private HdBankStatementService hdBankStatementService;
    /********************************基础操作*****************************************/
    //分页
    public R page(Map<String,Object> params) {
        Map<String,Object> result = new HashedMap();
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
    //添加接口
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R save(HdTransferBankStatement entity){
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
        return R.ok("保存成功");
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
        //新增成员单位
        hdBankStatement.setCompanyMemberId(hdTransferBankStatement.getMemberIncomeSubjectId());
        //对方科目
        hdBankStatement.setSubjects(hdTransferBankStatement.getMemberPaySubjectName());
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
        //新增成员单位
        hdBankStatement.setCompanyMemberId(hdTransferBankStatement.getMemberPaySubjectId());
        //对方科目
        hdBankStatement.setSubjects(hdTransferBankStatement.getMemberIncomeSubjectName());
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

    //查询id
    public R<HdTransferBankStatement> getById(Serializable id){
        return remoteBillService.getNzAccountById(id);
    }
    //编辑
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R updateById(HdTransferBankStatement entity){
        remoteBillService.updateNzAccount(entity);
        //先删除后添加
        remoteBillService.deleteBySourceId(entity.getId());
        this.save(entity);
        return R.ok("编辑成功");
    }

    //删除
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public R removeById(Serializable id){
        //删除内账
        remoteBillService.removeNzAccountById(id);
        //删除内账关联账单
        remoteBillService.deleteBySourceId(id);
        return R.ok("删除成功");
    }


    //批量添加

    //封装序号
    //重新封装序号
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


    /**********************************根据对方单位code获取公司******************************/
    //原根据名称查询改成code查询
    public HdCompanyMember getSubjectEntity(String code){
        return remoteBaseService.getMembercomByCode(code);
    }



}
