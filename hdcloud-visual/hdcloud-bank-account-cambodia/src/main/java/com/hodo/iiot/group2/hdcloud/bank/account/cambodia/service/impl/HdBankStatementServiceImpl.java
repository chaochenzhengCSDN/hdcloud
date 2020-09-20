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
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankRecordService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.MyBeanUtils;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.StringUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.jjUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 *
 * @author zxw
 * @date 2019-12-04 09:44:09
 */
@Service
public class HdBankStatementServiceImpl implements HdBankStatementService {
    @Autowired
    private RemoteBillService remoteBillService;
    @Autowired
    private RemoteBaseService remoteBaseService;
    @Autowired
    private HdBankRecordService hdBankRecordService;
    @Override
    public R<HdBankStatement> page(Map<String, Object> params) {
        return null;
    }

    @Override
    public R<HdBankStatement> getById(Serializable id) {
        return remoteBillService.getBankStatementById(id);
    }

    @Override
    public R updateById(HdBankStatement entity) {
        return remoteBillService.updateBankStatementById(entity);
    }

    @Override
    public R save(HdBankStatement entity) {
        return null;
    }

    @Override
    public R removeById(Serializable id) {
        return null;
    }

    //重新插入
    @Override
    public R insertEntity(HdBankStatement entity){
        BigDecimal income = entity.getIncome();
        BigDecimal pay = entity.getPay();
        if(income==null||"".equals(income)){
            entity.setIncome(BigDecimal.ZERO);
        }
        if(pay==null||"".equals(pay)){
            entity.setPay(BigDecimal.ZERO);
        }
        Integer num = getNoMaxNumber(entity.getAccountDate());
        entity.setNo(new BigDecimal(num));
        entity.setBy2(""+num);
        List<HdBankStatement> bankStatementList = new ArrayList<HdBankStatement>();
        bankStatementList.add(entity);
        return remoteBillService.addBankStatementList(bankStatementList);
    }


    public synchronized Integer getNoMaxNumber(Date accountDate){
        Integer maxNo = null;
        int No = 1;
        String accountDateStr = null;
        if(accountDate!=null){
            accountDateStr = jjUtil.formatDate(accountDate);
        }
        maxNo = remoteBillService.selectMaxNo(accountDateStr);
        if(maxNo!=null){
            No = maxNo+1;
        }
        return No;
    }


    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public Integer doBatchCZ(String ids) throws Exception {
        int count = 0;
        for(String id:ids.split(",")){
            HdBankStatement hdBankStatement = this.getById(id).getData();
            HdBankStatement hdBankStatementCopy = new HdBankStatement();
            MyBeanUtils.copyBeanNotNull2Bean(hdBankStatement, hdBankStatementCopy);
            hdBankStatementCopy.setAccountType("3");
            hdBankStatementCopy.setSourceType("4");
            hdBankStatementCopy.setAccountDate(new Date());
            hdBankStatementCopy.setSynaccountDate(new Date());
            hdBankStatementCopy.setIncome(hdBankStatement.getIncome().multiply(new
                    BigDecimal(-1)));
            hdBankStatementCopy.setPay(hdBankStatement.getPay().multiply(new
                    BigDecimal(-1)));
            hdBankStatementCopy.setRemark("用于" + jjUtil.formatDate(hdBankStatement.getAccountDate()) + ",凭证编号为：" + hdBankStatement.getNo() + "的账务冲账！");
            this.insertEntity(hdBankStatementCopy);
            count++;
        }
        return count;
    }

    public String searchRemark() {
        //String sql = "SELECT DISTINCT REMARK from HD_BANK_ACCOUNT WHERE REMARK IS NOT NULL";
        List<String> list = remoteBillService.searchRemark();
        String str = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                str += (String) list.get(i) + ",";
            }
        }
        return str;
    }

    //全查用于导出
    public List<HdBankStatement> getAllBankStatement(Map<String,Object> params){
        List<HdBankStatement> hdBankStatementList = remoteBillService.getAllBankStatement(params);
        if(hdBankStatementList!=null&&hdBankStatementList.size()>0){
            //计算期初
            for(HdBankStatement hdBankStatement:hdBankStatementList){
                //计算每一笔账单前的期初，参数成员单位，日期，编号
                formatNo(hdBankStatement);
            }
        }
        return hdBankStatementList;
    }
    /*******************************同步****************************************/
    //内部互转同步
    public Integer synNZData(List<HdBankStatement> accountList,
                             List<HdBankPending> pendingList,
                             List<String> allSheetIds,
                             String query_begin, String query_end) {
        int num = 0;
        List<List<Object>> result = null;
        try {
            //查询所有内部账单号然后拼接查询条件
            String sql = sqlBuild(sqlBuildParam1());
            Map<String,Object> params = new HashedMap();
            params.put("sql",sql);
            params.put("query_begin",query_begin);
            params.put("query_end",query_end);
            result = remoteBillService.syncData(params);
            String companyA = "";
            String companyB = "";
            if(result!=null){
                for(List<Object> rs:result){
                    num++;
                    if (!allSheetIds.contains(checkObj(rs.get(0)))) {
                        String explain = "";
                        if (StringUtil.isNotEmpty(checkObj(rs.get(4)))) {
                            explain = checkObj(rs.get(4)).trim();
                        }
                        //System.out.println("互相内转explain:" + explain+",income:" + rs.getString(6));
                        if (explain != null) {
                            if (getCompanyExplain(explain).contains("付")) {
                                companyA = getCompanyName(getCompanyExplain(explain), "pay");
                                companyB = getCompanyName(getCompanyExplain(explain), "income");
                                //查询A公司和B公司
                                Map<String,Object> paramCode = new HashedMap<>();
                                paramCode.put("code",companyA);
                                HdCompany hdCompanyA = remoteBaseService.getCompanyByCode(paramCode);
                                paramCode.put("code",companyB);
                                HdCompany hdCompanyB = remoteBaseService.getCompanyByCode(paramCode);
                                if (hdCompanyA != null) {
                                    //支出公司存在,加入账单数组
                                    accountList.add(
                                            saveAccount(jjUtil.parseDate(checkObj(rs.get(3))),
                                                    explain, new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5))),
                                                    hdCompanyA.getId(), checkObj(rs.get(0)), checkObj(rs.get(40)),"1",
                                                    checkObj(rs.get(7)),checkObj(rs.get(39)),hdCompanyA.getId()));
                                } else {
                                    //支出公司不存在，加入待处理数组
                                    pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))),
                                            explain, new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5))),
                                            "待处理", checkObj(rs.get(0)),
                                            checkObj(rs.get(7)), checkObj(rs.get(39)), checkObj(rs.get(40)),"1"));
                                }
                                if (hdCompanyB != null) {
                                    //收入公司存在，加入账单数组
                                    accountList.add(saveAccount(jjUtil.parseDate(checkObj(rs.get(3))),
                                            explain, new BigDecimal(checkObj(rs.get(5))), new BigDecimal("0"),
                                            hdCompanyB.getId(), checkObj(rs.get(0)), checkObj(rs.get(39)),"1",
                                            checkObj(rs.get(9)),checkObj(rs.get(40)),hdCompanyB.getId()));
                                } else {
                                    //收入公司不存在，加入待处理数组
                                    pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))),
                                            explain, new BigDecimal(checkObj(rs.get(5))), new BigDecimal("0"),
                                            "待处理", checkObj(rs.get(0)),
                                            checkObj(rs.get(9)), checkObj(rs.get(40)), checkObj(rs.get(39)),"1"));
                                }
                            } else {
                                //摘要第一个逗号前字符串不带付字
                                pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))), explain, new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5))), "待处理", checkObj(rs.get(0)), checkObj(rs.get(7)), checkObj(rs.get(39)), checkObj(rs.get(40)),"1"));
                                pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))), explain, new BigDecimal(checkObj(rs.get(5))), new BigDecimal("0"), "待处理", checkObj(rs.get(0)), checkObj(rs.get(9)), checkObj(rs.get(40)), checkObj(rs.get(39)),"1"));
                            }

                        } else {
                            //摘要为空
                            pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))), explain, new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5))), "待处理", checkObj(rs.get(0)), checkObj(rs.get(7)), checkObj(rs.get(39)), checkObj(rs.get(40)),"1"));
                            pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))), explain, new BigDecimal(checkObj(rs.get(5))), new BigDecimal("0"), "待处理", checkObj(rs.get(0)), checkObj(rs.get(9)), checkObj(rs.get(40)), checkObj(rs.get(39)),"1"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    //内部转外部同步,支出
    public Integer synNZPay(List<HdBankStatement> accountList,
                            List<HdBankPending> pendingList,
                            List<String> allSheetIds,
                            String query_begin, String query_end) {
        int num = 0;
        List<List<Object>> result = null;
        try {
            String sqlPay = sqlBuild(sqlBuildParam2());
            System.out.println(sqlPay+"-------------");
            Map<String,Object> params = new HashedMap();
            params.put("sql",sqlPay);
            params.put("query_begin",query_begin);
            params.put("query_end",query_end);
            result = remoteBaseService.syncData(params);
            String companyName = "";
            if(result!=null){
                for(List<Object> rs:result){
                    num++;
                    if (!allSheetIds.contains(checkObj(rs.get(0)))) {
                        String explain = "";
                        if (StringUtil.isNotEmpty(checkObj(rs.get(4)))) {
                            explain = checkObj(rs.get(4)).trim();
                        }
                        if (explain != null) {
                            //System.out.println("内转支出explain:" + rs.getString(5) + ",money:" + rs.getString(6));
                            //获取收支公司名，第一个逗号之前的字符串带付字
                            //根据是否带付字获取支出公司名称：
                            //有付字，截取付字之前的为支出公司名称；无付字，直接为支出公司名称
                            if (getCompanyExplain(explain).contains("付")) {
                                companyName = getCompanyName(getCompanyExplain(explain), "pay");
                            } else {
                                companyName = getCompanyExplain(explain);
                            }
                            System.out.println("摘要名称-----"+companyName);
                            //匹配支出公司
                            //HdCompany hdCompany = hdCompanyBiz.getCompanyByCode(companyName);
                            Map<String,Object> paramsCode = new HashedMap<>();
                            paramsCode.put("code",companyName);
                            HdCompany hdCompany = remoteBaseService.getCompanyByCode(paramsCode);
                            if (hdCompany != null) {
                                //支出公司存在，加入账单数组
                                if (StringUtil.isNotEmpty(checkObj(rs.get(20)))) {
                                    explain += checkObj(rs.get(20));
                                } else {
                                    explain += checkObj(rs.get(40));
                                }
                                accountList.add(saveAccount(jjUtil.parseDate(checkObj(rs.get(3))),
                                        explain, new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5))),
                                        hdCompany.getId(), checkObj(rs.get(0)), checkObj(rs.get(40)),"1",checkObj(rs.get(7)),checkObj(rs.get(39)),
                                        null));
                            } else {
                                //支出公司不存在，加入待处理数组
                                pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))),
                                        explain,
                                        new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5)))
                                        , "待处理", checkObj(rs.get(0)),
                                        checkObj(rs.get(7)), checkObj(rs.get(39)),
                                        checkObj(rs.get(40)),"1"));
                            }
                        } else {
                            pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))),
                                    explain,
                                    new BigDecimal("0"), new BigDecimal(checkObj(rs.get(5)))
                                    , "待处理", checkObj(rs.get(0)),
                                    checkObj(rs.get(7)), checkObj(rs.get(39)),
                                    checkObj(rs.get(40)),"1"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("内转付2:"+num);
        return num;
    }

    //内部转外部同步,收入
    public Integer synNZIncome(List<HdBankStatement> accountList,
                               List<HdBankPending> pendingList,
                               List<String> allSheetIds,
                               String query_begin, String query_end) {
        int num = 0;
        List<List<Object>> result = null;
        try {
            String sqlIncome = sqlBuild(sqlBuildParam3());
            System.out.println(sqlIncome+"--------");
            Map<String,Object> params = new HashedMap();
            params.put("sql",sqlIncome);
            params.put("query_begin",query_begin);
            params.put("query_end",query_end);
            result = remoteBaseService.syncData(params);
            String companyName = "";
            if(result!=null){
                for(List<Object> rs:result){
                    num++;
                    if (!allSheetIds.contains(checkObj(rs.get(0)))) {
                        String explain = "";
                        if (StringUtil.isNotEmpty(rs.get(4))) {
                            explain = rs.get(4).toString().trim();
                        }
                        //System.out.println("内转收入explain:" + explain+",income:" + rs.getString(6));
                        if (explain != null) {
                            //获取收支公司名，第一个逗号之前的字符串带付字
                            //根据是否带付字获取收入公司名称：
                            //有付字，截取付字之前的为收入公司名称；无付字，直接为收入公司名称
                            if (getCompanyExplain(explain).contains("付")) {
                                companyName = getCompanyName(getCompanyExplain(explain), "income");
                            } else {
                                companyName = getCompanyExplain(explain);
                            }
                            //HdCompany hdCompany = hdCompanyBiz.getCompanyByCode(companyName);
                            Map<String,Object> paramsCode = new HashedMap<>();
                            paramsCode.put("code",companyName);
                            HdCompany hdCompany = remoteBaseService.getCompanyByCode(paramsCode);
                            if (hdCompany != null) {
                                //收入公司存在，加入账单数组
                                accountList.add(saveAccount(jjUtil.parseDate(checkObj(rs.get(3))), explain , new BigDecimal(checkObj(rs.get(5))),
                                        new BigDecimal("0"), hdCompany.getId(),
                                        checkObj(rs.get(0)), checkObj(rs.get(39)),"1",checkObj(rs.get(9)),checkObj(rs.get(40)),null));
                            } else {
                                //收入公司不存在，加入待处理数组
                                pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))), explain, new BigDecimal(checkObj(rs.get(5))),
                                        new BigDecimal("0"), "待处理",
                                        checkObj(rs.get(0)), checkObj(rs.get(9)),
                                        checkObj(rs.get(40)), checkObj(rs.get(39)),"1"));
                            }
                        } else {
                            pendingList.add(savePend(jjUtil.parseDate(checkObj(rs.get(3))), explain,
                                    new BigDecimal(checkObj(rs.get(5))),
                                    new BigDecimal("0"), "待处理",
                                    checkObj(rs.get(0)), checkObj(rs.get(9)),
                                    checkObj(rs.get(40)), checkObj(rs.get(39)),"1"));
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("内转收3:"+num);
        return num;
    }

    //外转
    public Integer synWZ(List<HdBankStatement> accountList,
                         List<HdBankPending> pendingList,
                         List<String> allRids,
                         String query_begin, String query_end) {
        int num = 0;
        List<List<Object>> result = null;
        HdBankStatement hdBankAccountAdd = null;
        HdCompany hdCompany = null;
        try {
            String sqlWZ = sqlBuildWZ(sqlBuildWZParam());
            System.out.println(sqlWZ+"----------------");
            Map<String,Object> params = new HashedMap();
            params.put("sql",sqlWZ);
            params.put("query_begin",query_begin);
            params.put("query_end",query_end);
            result = remoteBaseService.syncData(params);
            //while (rs.next()) {
            if(result!=null){
                for(List<Object> rs:result){
                    num++;
                    String info = "";
                    String companyId = "";
                    String explain = "";
                    String opAcntName = "";
                    String acntNo = "";
                    String superCompId = "";
                    if (StringUtil.isNotEmpty(checkObj(rs.get(0)))) {
                        explain = checkObj(rs.get(0)).trim();
                    }
                    if (StringUtil.isNotEmpty(checkObj(rs.get(5)))) {
                        opAcntName = checkObj(rs.get(5)).trim();
                    }
                    if (StringUtil.isNotEmpty(checkObj(rs.get(8)))) {
                        acntNo = checkObj(rs.get(8)).trim();
                    }
                    if (!allRids.contains(checkObj(rs.get(2)))) {
                        //1、先按摘要匹配
                        info = explain;
                        //System.out.println("外转账单explain:" + explain+",money:" + rs.getString(5));
                        if (explain != null) {
                            //九恒星中中国银行摘要会自动变成OBSS011310531050GIRO000001209448内二//内二;内二
                            //针对此情况提取匹配公司
                            //摘要匹配公司
                            if (checkObj(rs.get(6)).equals("中国银行") && explain.contains(";")&&
                                    explain.contains("//")) {
                                Pattern p = Pattern.compile("\\//(.*?)\\;");;//正则表达式，取=和|之间的字符串，不包括=和|
                                Matcher m = p.matcher(explain);
                                if( m.find() ){
                                    //System.out.println( m.group(1) );
                                    String companyName = getCompanyName(getCompanyExplain(m.group(1)), "pay");
                                    //hdCompany = hdCompanyBiz.getCompanyByCode(companyName);
                                    Map<String,Object> paramsCode = new HashedMap<>();
                                    paramsCode.put("code",companyName);
                                    hdCompany  = remoteBaseService.getCompanyByCode(paramsCode);
                                    if (hdCompany != null) {
                                        //superCompId = hdCompany.getCompanyId();
                                        companyId = hdCompany.getId();
                                    }
                                }else{
                                    //System.out.println(" 没有匹配到内容....");
                                }
                            } else {
                                String companyName = getCompanyName(getCompanyExplain(explain), "pay");
                                //hdCompany = hdCompanyBiz.getCompanyByCode(companyName);
                                Map<String,Object> paramsCode = new HashedMap<>();
                                paramsCode.put("code",companyName);
                                hdCompany = remoteBaseService.getCompanyByCode(paramsCode);
                                if (hdCompany != null) {
                                    //superCompId = hdCompanyMember.getCompanyId();
                                    companyId = hdCompany.getId();
                                }
                            }
                            info += ":" + companyId;
                            if (StringUtil.isNotEmpty(companyId)) {
                                // 如果匹配成功
                                hdBankAccountAdd = getHdBankEntity(rs, superCompId,companyId);
                                if (hdBankAccountAdd != null) {
                                    accountList.add(hdBankAccountAdd);
                                }
                                //一旦匹配成功，无论结果如何，此次循环结束
                                continue;
                            }
                            //2、再按外单位匹配
                            info += ";" + opAcntName + ":" + acntNo;
                            if (StringUtil.isNotEmpty(opAcntName)) {
                                companyId = getMatchCompanyId(opAcntName, acntNo);
                                info += ":" + companyId;
                                if (StringUtil.isNotEmpty(companyId)) {
                                    hdCompany = remoteBaseService.getCompanyById(companyId).getData();
                                    // 如果匹配外部维护公司
                                    if(hdCompany!=null){
                                        hdBankAccountAdd = getHdBankEntity(rs, hdCompany.getId(),null);
                                        if (hdBankAccountAdd != null) {
                                            accountList.add(hdBankAccountAdd);
                                        }
                                    }
                                    //一旦匹配成功，无论结果如何，此次循环结束
                                    continue;
                                }
                            }
                            info += "匹配失败";
                            // 进入待转数据
                            HdBankPending hdBankPending = new HdBankPending();
                            hdBankPending.setAccountDate(jjUtil.parseDate(checkObj(rs.get(3))));
                            hdBankPending.setSynaccountDate(jjUtil.parseDate(checkObj(rs.get(3))));
                            hdBankPending.setRid(checkObj(rs.get(2)));
                            hdBankPending.setRemark(explain);

                            //银行通过外转id获取银行id和银行账户id
                            //hdBankPending.setBankname(rs.getString(7));
                            hdBankPending.setMySubjects(checkObj(rs.get(7)));
                            hdBankPending.setCompanyName("待处理");
                            String dirflag = checkObj(rs.get(1));
                            hdBankPending.setSubjects(opAcntName);
                            hdBankPending.setAccountType("0");
                            // 数值为支出
                            if ("1".equals(dirflag)) {
                                hdBankPending.setIncome(new BigDecimal("0.00"));
                                hdBankPending.setPay(new BigDecimal(checkObj(rs.get(4))==null ? "0.00" : checkObj(rs.get(4))));
                            }
                            // 数值为收入
                            if ("2".equals(dirflag)) {
                                hdBankPending.setPay(new BigDecimal("0.00"));
                                hdBankPending.setIncome(new BigDecimal(checkObj(rs.get(4))==null? "0.00" : checkObj(rs.get(4))));
                            }
                            //7.2增加银行账户id用于统计
                            if(StringUtil.isNotEmpty(checkObj(rs.get(8)))){
                                HdBankAccount hdBankAccount = remoteBaseService.getAccountByTerm("",checkObj(rs.get(8)));
                                if(hdBankAccount!=null){
                                    hdBankPending.setBankAccountId(hdBankAccount.getId());
                                    hdBankPending.setBankName(hdBankAccount.getBankName());
                                }
                            }
                            pendingList.add(hdBankPending);
                        }else{
                            //如果摘要为空，如何处理
                            // 进入待转数据
                            HdBankPending hdBankPending = new HdBankPending();
                            hdBankPending.setAccountDate(jjUtil.parseDate(checkObj(rs.get(3))));
                            hdBankPending.setSynaccountDate(jjUtil.parseDate(checkObj(rs.get(3))));
                            hdBankPending.setRid(checkObj(rs.get(2)));
                            hdBankPending.setRemark(explain);

                            //银行通过外转id获取银行id和银行账户id
                            //hdBankPending.setBankname(rs.getString(7));
                            hdBankPending.setMySubjects(checkObj(rs.get(7)));
                            hdBankPending.setCompanyName("待处理");
                            String dirflag = checkObj(rs.get(1));
                            hdBankPending.setSubjects(opAcntName);
                            hdBankPending.setAccountType("0");
                            // 数值为支出
                            if ("1".equals(dirflag)) {
                                hdBankPending.setIncome(new BigDecimal("0.00"));
                                hdBankPending.setPay(new BigDecimal(checkObj(rs.get(4)) == null ? "0.00" : checkObj(rs.get(4))));
                            }
                            // 数值为收入
                            if ("2".equals(dirflag)) {
                                hdBankPending.setPay(new BigDecimal("0.00"));
                                hdBankPending.setIncome(new BigDecimal(checkObj(rs.get(4)) == null ? "0.00" : checkObj(rs.get(4))));
                            }
                            //7.2增加银行账户id用于统计
                            if(StringUtil.isNotEmpty(checkObj(rs.get(8)))){
                                HdBankAccount hdBankAccount = remoteBaseService.getAccountByTerm("",checkObj(rs.get(8)));
                                if(hdBankAccount!=null){
                                    hdBankPending.setBankAccountId(hdBankAccount.getId());
                                    hdBankPending.setBankName(hdBankAccount.getBankName());
                                }
                            }
                            pendingList.add(hdBankPending);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("外转:"+num);
        return num;
    }

    // 判断是否匹配外部账户维护表
    private String getMatchCompanyId(String opAcntName, String acntNo) {
        List<HdMatchCompany> matchComList = new ArrayList<HdMatchCompany>();
        String matchComName = "";
        //根据外部单位查找内部匹配公司
        if(StringUtil.isNotEmpty(opAcntName)){
            matchComList = remoteBaseService.getMatchCompanyByTerm(opAcntName);
            if (matchComList != null && matchComList.size() > 0) {
                //循环找到对应的内公司名称
                for (HdMatchCompany matchCom : matchComList) {
                    if(StringUtil.isNotEmpty(matchCom.getCompanyCode())){
                        matchComName = matchCom.getCompanyCode();
                        break;
                    }
                }
            }
        }
        //没有匹配到结果返回“”
        return matchComName;
    }
    //获取支出或者收入公司名
    private static String getCompanyName(String explain, String flag) {
        //判断是否含有”付“字
        if (explain.indexOf("付") > 0) {
            if ("income".equals(flag)) {
                //收入
                explain = explain.substring(explain.indexOf("付") + 1);
            } else {
                //支出
                explain = explain.substring(0, explain.indexOf("付"));
            }
        }
        return explain;
    }

    //外转封装
    public HdBankStatement getHdBankEntity(List<Object> rs,String superCompId, String comId) throws SQLException, ParseException {
        HdBankStatement hdBank = new HdBankStatement();
        hdBank.setAccountDate(jjUtil.parseDate(checkObj(rs.get(3))));
        hdBank.setSynaccountDate(jjUtil.parseDate(checkObj(rs.get(3))));
        hdBank.setCompanyId(comId);
        hdBank.setCompanyMemberId(null);
        hdBank.setRid(checkObj(rs.get(2)));
        hdBank.setRemark(checkObj(rs.get(0)));
        hdBank.setSubjects(checkObj(rs.get(5)));
        hdBank.setMySubjects(checkObj(rs.get(7)));
        hdBank.setAccountType("0");
        hdBank.setSourceType("0");
        String dirflag = checkObj(rs.get(1));
        // 数值为支出
        if ("1".equals(dirflag)) {
            hdBank.setIncome(new BigDecimal("0.00"));
            hdBank.setPay(new BigDecimal(checkObj(rs.get(4))==null? "0.00" : checkObj(rs.get(4))));
        }
        // 数值为收入
        if ("2".equals(dirflag)) {
            hdBank.setPay(new BigDecimal("0.00"));
            hdBank.setIncome(new BigDecimal(checkObj(rs.get(4))==null ? "0.00" : checkObj(rs.get(4))));
        }
        //7.2增加银行账户id用于统计
        if(StringUtil.isNotEmpty(checkObj(rs.get(8)))){
            //根据wbzh获取实体类
            HdBankAccount hdBankAccount = remoteBaseService.getAccountByTerm("",checkObj(rs.get(8)));
            hdBank.setBankName("同步外部账号异常");
            hdBank.setBankAccountId("同步外部账号异常");
            if(hdBankAccount!=null){
                hdBank.setBankName(hdBankAccount.getBankName());
                hdBank.setBankAccountId(hdBankAccount.getId());
            }
            //System.out.println("测试3："+getHdNzDictByTerm("",checkObj(rs.get(8)))+"---------"+checkObj(rs.get(8)));
        }
        return hdBank;
    }

    //从九恒星拉取数据进行封装
    private HdBankStatement saveAccount(
            Date date, String explain, BigDecimal income, BigDecimal pay,
            String companyName, String sheetId, String subjects,String accountType,String nzId,
            String mySubjects,String memberCompany
    ) {
        HdBankStatement entity = new HdBankStatement();
        //entity.setTenantId(BaseContextHandler.getTenantID());
        //entity.setCreateBy(userId);
        entity.setAccountDate(date);
        entity.setSynaccountDate(date);
        entity.setRemark(explain);
        entity.setIncome(income);
        entity.setPay(pay);
        entity.setCompanyId(companyName);
        entity.setCompanyMemberId(memberCompany);
        entity.setSubjects(subjects);
        entity.setSheetid(sheetId);
        entity.setAccountType(accountType);
        entity.setSourceType("0");
        //7.2增加银行账户id用于统计
        if(StringUtil.isNotEmpty(nzId)){
            HdBankAccount hdBankAccount = getHdBankAccountByTerm(nzId,"");
            if(hdBankAccount!=null){
                entity.setBankAccountId(hdBankAccount.getId());
                entity.setBankName(hdBankAccount.getBankName());
            }
        }
        entity.setMySubjects(mySubjects);
        return entity;
    }

    //从九恒星拉取数据进行封装
    private HdBankPending savePend(
            Date date, String explain, BigDecimal income, BigDecimal pay,
            String companyName, String sheetId, String nzId,
            String mySubjects, String subjects,String accountType
    ) {
        HdBankPending entity = new HdBankPending();
        entity.setAccountDate(date);
        entity.setSynaccountDate(date);
        entity.setRemark(explain);
        entity.setIncome(income);
        entity.setPay(pay);
        entity.setMySubjects(mySubjects);
        entity.setSubjects(subjects);
        //HdNzDict hdNzDict = hdNzDictBiz.selectById(nzId);
        entity.setCompanyName(companyName);
        entity.setSheetid(sheetId);
        entity.setAccountType(accountType);
        //7.2增加银行账户id用于统计
        if(StringUtil.isNotEmpty(nzId)){
            //内转账号根据nzid获取银行
            HdBankAccount hdBankAccount = getHdBankAccountByTerm(nzId,"");
            if(hdBankAccount!=null){
                entity.setBankAccountId(hdBankAccount.getId());
                entity.setBankName(hdBankAccount.getBankName());
            }
        }
        return entity;
    }

    public HdBankAccount getHdBankAccountByTerm(String nzId,String wzId){
        return remoteBaseService.getAccountByTerm(nzId,wzId);
    }
    private String checkObj(Object o){
        if(o!=null){
            return o.toString();
        }
        return null;
    }
    private String sqlBuildWZ(String sqlBuildParam) {
        return "select explain,DIRFLAG,rid,ACTDATE,amount,OpAcntName,BANKNAME,ACNTNAME,ACNTNO from NSTCSA.VW_BP_RECORD where "
                + sqlBuildParam + " AND ACTDATE between (to_date(?,'yyyy-mm-dd'))" + " and (to_date(?,'yyyy-mm-dd'))"
                + " and (explain not in('资金上存','资金上收','用款','资金下拨','上收') or explain is null) and (OpAcntName!='红豆集团财务有限公司' or OpAcntName is null) order by ACTDATE desc";
    }

    private String sqlBuild(String sqlBuildParam) {
        return "SELECT * FROM NSTCSA.VW_CNTBUSSSHEET where " + sqlBuildParam +
                " AND ACTDATE between TO_DATE(?, 'yyyy-mm-dd') and " +
                "TO_DATE(?,'yyyy-mm-dd') AND (explain not in('资金上存','资金上收'," +
                "'用款','资金下拨','上收') or explain is null) and (extno is null or(extno is not null and extname is null and extbank is null)) order by ACTDATE desc";
    }
    //判断是否含有逗号，若有去第一个逗号前的内容
    private static String getCompanyExplain(String explain) {
        int a = explain.indexOf(",");
        int b = explain.indexOf("，");
        if (a > 0 && b > 0) {
            if (a > b) {
                return explain.substring(0, b);
            } else {
                return explain.substring(0, a);
            }
        }
        if (a > 0 && b == -1) {
            return explain.substring(0, a);
        }
        if (a == -1 && b > 0) {
            return explain.substring(0, b);
        }
        return explain;
    }
    //拼接查询语句1
    private String sqlBuildParam1() {
        Map params = new HashedMap();
        List<HdBankAccount> hdBankAccountList = remoteBaseService.selectListAllByTenant(params);
        StringBuffer sb = new StringBuffer("(JNO IN(");
        sqlBuildNzDict(sb, hdBankAccountList);
        sb.append(" and DNO IN(");
        sqlBuildNzDict(sb, hdBankAccountList);
        sb.append(")");
        return sb.toString();
    }

    private String sqlBuildParam2() {
        Map params = new HashedMap();
        List<HdBankAccount> hdBankAccountList = remoteBaseService.selectListAllByTenant(params);
        StringBuffer sb = new StringBuffer("(JNO IN(");
        sqlBuildNzDict(sb, hdBankAccountList);
        sb.append(" and DNO NOT IN(");
        sqlBuildNzDict(sb, hdBankAccountList);
        sb.append(")");
        return sb.toString();
    }

    private String sqlBuildParam3() {
        Map params = new HashedMap();
        List<HdBankAccount> hdNzDictList = remoteBaseService.selectListAllByTenant(params);
        StringBuffer sb = new StringBuffer("(JNO NOT IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(" and DNO IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(")");
        return sb.toString();
    }

    private String sqlBuildWZParam() {
        Map params = new HashedMap();
        List<HdBankAccount> hdBankAccountList = remoteBaseService.selectListAllByTenant(params);
        StringBuffer sb = new StringBuffer("(ACNTNO in(");
        sqlBuildWzDict(sb, hdBankAccountList);
        sb.append(")");
        return sb.toString();
    }
    //拼接查询语句外转子类
    private StringBuffer sqlBuildWzDict(StringBuffer sb, List<HdBankAccount> hdBankAccountList) {
        if (hdBankAccountList != null) {
            for (HdBankAccount hdBankAccount : hdBankAccountList) {
                sb.append(hdBankAccount.getExternalBankAccountId() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        return sb;
    }

    //拼接查询语句内转子类
    private StringBuffer sqlBuildNzDict(StringBuffer sb, List<HdBankAccount> hdBankAccountList) {
        if (hdBankAccountList != null) {
            for (HdBankAccount hdBankAccount : hdBankAccountList) {
                sb.append("'" + hdBankAccount.getInternalBankAccountId() + "',");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        return sb;
    }
    //封裝handBankAccountList
    public void handBankAccountList(List<HdBankStatement> accountList, Date accountDay) {
        Map<String, Map<Integer, Object>> mapResult = new HashedMap();
        Integer maxNo = getNoMaxNumber(accountDay);
        for (HdBankStatement hdBankStatement : accountList) {
            try {
                if (accountDay != null) {
                    hdBankStatement.setAccountDate(accountDay);
                }
                //如果年月日相同修改記賬日期
                BigDecimal income = hdBankStatement.getIncome();
                BigDecimal pay = hdBankStatement.getPay();
                if (income == null || "".equals(income)) {
                    hdBankStatement.setIncome(BigDecimal.ZERO);
                }
                if (pay == null || "".equals(pay)) {
                    hdBankStatement.setPay(BigDecimal.ZERO);
                }

                //逐个添加No
                hdBankStatement.setBy2(String.valueOf(maxNo));
                hdBankStatement.setNo(new BigDecimal(maxNo));
                maxNo++;
            } catch (Exception e) {
                //System.out.println("錯誤信息" + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
    }
    /******************************划账分账************************************/
    //划账還原处理
    public void handHuaAccounts(List<HdBankStatement> huaAccounts){
        if(huaAccounts!=null&&huaAccounts.size()>0){
            HdBankPending hdBankPending = new HdBankPending();
            List<HdBankRecord> hdBankRecords = new ArrayList<>();
            try{
                for(HdBankStatement hdBankStatement:huaAccounts){
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankStatement, hdBankPending);
                    hdBankPending.setCompanyName("待处理");
                    hdBankPending.setNo(null);
                    //hdBankPending.setCreateBy(BaseContextHandler.getUserID());
                    if("2".equals(hdBankStatement.getSourceType())){
                        hdBankRecords.add(hdBankRecordService.getHdBankRecord(hdBankStatement, "划账后还原"));
                    }else{
                        hdBankRecords.add(hdBankRecordService.getHdBankRecord(hdBankStatement, "九恒星还原"));
                    }

                    remoteBillService.addBankPending(hdBankPending);
                    remoteBillService.removeBankStatementById(hdBankStatement.getId());
                }
                //批量记录操作信息
                remoteBillService.addBankRecordList(hdBankRecords);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void handFenAccounts(List<HdBankStatement> fenAccounts){
        //如果分账中rid或者sheetid相同，按一笔还原
        Set<String> fenSheets = new HashSet<>();
        Set<String> fenRids = new HashSet<>();
        if(fenAccounts!=null&&fenAccounts.size()>0){
            for(HdBankStatement hdBankStatement:fenAccounts){
                if(StringUtil.isNotEmpty(hdBankStatement.getSheetid())){
                    fenSheets.add(hdBankStatement.getSheetid());
                }
                if(StringUtil.isNotEmpty(hdBankStatement.getRid())){
                    fenRids.add(hdBankStatement.getRid());
                }
            }
        }
        handFenAccountsByFlag("0",fenSheets);
        handFenAccountsByFlag("1",fenRids);
    }

    //根据标示分账处理
    private void handFenAccountsByFlag(String flag,Set<String> ids){
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        List<HdBankStatement> hdBankStatements  = new ArrayList<>();
        List<HdBankRecord> hdBankRecords = new ArrayList<>();
        HdBankPending hdBankPending = new HdBankPending();
        try {
            if (ids != null && ids.size() > 0) {
                for (String id : ids) {
                    //根据sheetId获取对应所有账单
                    if("0".equals(flag)){
                        hdBankStatements = remoteBillService.selStatementBySheet(id);
                    }else {
                        hdBankStatements = remoteBillService.selStatementByRid(id);
                    }
                    if (hdBankStatements != null && hdBankStatements.size() > 0) {
                        for (HdBankStatement hdBankStatement : hdBankStatements) {
                            sumIncome = sumIncome.add(hdBankStatement.getIncome());
                            sumPay = sumPay.add(hdBankStatement.getPay());
                            hdBankRecords.add(hdBankRecordService.getHdBankRecord(hdBankStatement, "分账后还原"));
                        }
                        //获取第一家公司账单，把值赋给还原的账单
                        HdBankStatement hdBankStatement0 = hdBankStatements.get(0);
                        //生成待处理账单
                        MyBeanUtils.copyBeanNotNull2Bean(hdBankStatement0, hdBankPending);
                        if (sumIncome.subtract(sumPay).compareTo(BigDecimal.ZERO) > 0) {
                            //收入大于支出
                            hdBankPending.setIncome(sumIncome.subtract(sumPay));
                            hdBankPending.setPay(BigDecimal.ZERO);
                        } else {
                            //收入小于支出
                            hdBankPending.setIncome(BigDecimal.ZERO);
                            hdBankPending.setPay(sumPay.subtract(sumIncome));
                        }
                        hdBankPending.setCompanyName("待处理");
                        hdBankPending.setNo(null);
                        //hdBankPending.setCreateBy(BaseContextHandler.getUserID());
                        remoteBillService.addBankPending(hdBankPending);
                        //批量删除账单
                        deleteAll(hdBankStatements);
                    }
                    //批量记录操作信息
                    remoteBillService.addBankRecordList(hdBankRecords);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //分账后批量删除账单
    private void deleteAll(List<HdBankStatement> hdBankStatements){
        if(hdBankStatements!=null&&hdBankStatements.size()>0){
            for(HdBankStatement hdBankStatement:hdBankStatements){
                remoteBillService.removeBankStatementById(hdBankStatement.getId());
            }
        }
    }


    /******************************辅助接口*************************************/
    public void formatNo(HdBankStatement hdBankStatement){
        String no = String.valueOf(hdBankStatement.getNo());
        while(no.length()<6){
            no = "0"+no;
        }
        hdBankStatement.setBy1(no);
    }

}
