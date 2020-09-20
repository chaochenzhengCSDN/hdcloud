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

package com.hodo.iiot.group2.hdcloud.bank.account.currency.controller;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.log.annotation.SysLog;


import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.BaseContrller;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdCompanyMember;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdBankStatementService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankstatement")
@Api(value = "hdbankstatement", tags = "账单管理")
public class HdBankStatementController extends BaseContrller {


    private final HdBankStatementService hdBankStatementService;
    @Autowired
    private RemoteBillService remoteBillService;
    @Autowired
    private RemoteBaseService remoteBaseService;

    /**
     * 分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankStatementPage(@RequestParam() Map<String,Object> params) {

        return hdBankStatementService.page(params);
    }


    /**
     * 通过id查询bank_account
     * @param id id 查询单个对象
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return hdBankStatementService.getById(id);
    }

    /**
     * 新增bank_account
     * @param hdBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    public R save(@RequestBody HdBankStatement hdBankStatement) {
        if(StringUtil.isEmpty(hdBankStatement.getCompanyMemberId())){
            return R.failed("成员单位 未填寫");
        }
        hdBankStatement.setSourceType("4");
        hdBankStatement.setAccountType("3");
        if(hdBankStatement.getSynaccountDate()==null){
            hdBankStatement.setSynaccountDate(new Date());
        }
        hdBankStatement.setAccountDate(new Date());
        //由于有序号问题，所以把保存和批量保存放一起
        return hdBankStatementService.insertEntity(hdBankStatement);
    }

    /**
     * 按日修改bank_account
     * @param hdBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping("/byDay")
    public R updateByDay(@RequestBody HdBankStatement hdBankStatement) {
        return hdBankStatementService.updateById(hdBankStatement);
    }

    /**
     * 按月修改bank_account
     * @param hdBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping("/byMonth")
    public R updateByMonth(@RequestBody HdBankStatement hdBankStatement) {

        return hdBankStatementService.updateById(hdBankStatement);
    }

    /**
     * 内部账单级联添加即为批量添加直接掉远程服务
     *
     */

    /**
     * 通过id删除bank_account
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable String id) {
        return hdBankStatementService.removeById(id);
    }

    /**
     * 银行财务冲账
     */
    @ApiOperation("银行财务冲账")
    @RequestMapping(value = "/doBatchCZ", method = RequestMethod.GET)
    @ResponseBody
    public R doBatchCZ(String ids) {
        int count = 0;
        try{
            for (String id : ids.split(",")) {
                HdBankStatement hdBankStatement = hdBankStatementService.getById(id).getData();
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

                hdBankStatementCopy.setRemark("用于" + sdf.format(hdBankStatement.getAccountDate()) + ",凭证编号为：" + hdBankStatement.getNo() + "的账务冲账！");
                hdBankStatementService.insertEntity(hdBankStatementCopy);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
            R.failed("银行账务信息冲账失败");
        }
        return R.ok("您成功冲账" + count + "条记录！");
    }
    /**
     * 查询所有的摘要
     */
    @ApiOperation("查询所有的摘要")
    @RequestMapping(value = "/doSearch", method = RequestMethod.GET)
    @ResponseBody
    public R doSearch() {
        String str = hdBankStatementService.searchRemark();
        return R.ok(str);
    }


    /**
     * 批量删除银行账务信息表
     *
     * @return
     */
    @ApiOperation("批量删除银行账务信息表")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            List<String> idStrs = new ArrayList<>();
            for (String id:ids.split(",")){
                idStrs.add(id);
            }
            R r = remoteBillService.batchDelBankStatement(idStrs);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行账务信息表删除失败";
            return R.failed( message);
        }
//        message = "您成功删除" + count + "条";
//        return R.ok(message);
    }
    /**
     * 导出excel
     *
     * @throws ParseException
     */
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam Map<String, Object> params) throws ParseException {
        String moneyStart = jjUtil.handleParams(params,"moneyStart");
        String moneyEnd = jjUtil.handleParams(params,"moneyEnd");
        List<HdBankStatement> allBankStatementList = null;
        if(StringUtil.isEmpty(moneyStart)){
            moneyStart=null;
        }
        if(StringUtil.isEmpty(moneyEnd)){
            moneyEnd=null;
        }
        params.put("moneyStart",moneyStart);
        params.put("moneyEnd",moneyEnd);
        jjUtil.handleParams(params,"limit");
        jjUtil.handleParams(params,"page");
        allBankStatementList = hdBankStatementService.getAllStatement(params);
        if(allBankStatementList==null){
            allBankStatementList = new ArrayList<>();
            System.out.println("账单查询失败");
        }
        ExcelUtil excelUtil = new ExcelUtil<HdBankStatement>();
        try {
            excelUtil.print("银行账单记录列表", HdBankStatement.class, "银行账单记录列表","导出时间:" + jjUtil.getDateStr(), "银行账单记录列表", allBankStatementList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    /**
     * 修改摘要
     */
    @ApiOperation("修改摘要")
    @RequestMapping(value = "/updateRemark", method = RequestMethod.POST)
    @ResponseBody
    public R updateRemark(@RequestBody Map<String,Object> params ) {

        if (params.get("id")==null) {
            return R.failed("参数传递失败");
        }
        String id = params.get("id").toString();
        String remark = "";
        if(params.get("remark")!=null){
            remark = params.get("remark").toString();
        }
//        if (StringUtil.isEmpty(remark)) {
//            return new ObjectRestResponse<>(1003, "参数传递失败");
//        }
        String message = null;
        message = "修改备注成功";
        try {
            HdBankStatement hdBankStatement = hdBankStatementService.getById(id).getData();
            hdBankStatement.setRemark(remark);
            //hdBankAccount.setCreateBy(BaseContextHandler.getUserID());
            hdBankStatementService.updateById(hdBankStatement);
        } catch (Exception e) {
            e.printStackTrace();
            message = "修改备注失败";
            return R.failed( message);
        }

        return R.ok(null,message);
    }
    /**
     * 同步功能
     */
    @RequestMapping(value = "/synData", method = RequestMethod.GET)
    @ResponseBody
    public R<Map<String, Object>> synData(String query_begin,
                                                           String query_end, String query_to) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("时间:"+format.format(new Date())+"进入次数-----");
        Map<String, Object> result = new HashedMap();
        int synCount = 0;
        Integer due = 0;
        Integer saveCount = 0;
        try{
            Integer saveCountBefore = remoteBillService.getSaveCount(query_begin, query_end)!=null?
                    remoteBillService.getSaveCount(query_begin, query_end):0;
            Date accountDay = null;
            if (StringUtil.isNotEmpty(query_to)) {
                try {
                    System.out.println("同步至:"+query_to);
                    accountDay = DateUtils.date_sdf.parse(query_to);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return R.failed("同步至某日日期格式错误");
                }
            }
            List<HdBankStatement> accountList = new ArrayList<>();
            List<HdBankPending> pendingList = new ArrayList<>();
            //查询本地所有的sheetid,查询所有的rid
            List<String> allSheetIds = remoteBillService.getAllSheetIds() != null ? remoteBillService.getAllSheetIds() :
                    new ArrayList<>();
            List<String> allRIds = remoteBillService.getAllRids() != null ? remoteBillService.getAllRids() :
                    new ArrayList<>();
            hdBankStatementService.synNZData(accountList, pendingList, allSheetIds, query_begin, query_end);
            //一笔生成两笔需要相加除以2
            due = (accountList.size() + pendingList.size()) / 2;
            //System.out.println(accountList.size() + "------互转" + pendingList.size());
            hdBankStatementService.synNZIncome(accountList, pendingList, allSheetIds, query_begin, query_end);
            //System.out.println(accountList.size() + "------内转外" + pendingList.size());
            hdBankStatementService.synNZPay(accountList, pendingList, allSheetIds, query_begin, query_end);
            //System.out.println(accountList.size() + "------外转内" + pendingList.size());

            hdBankStatementService.synWZ(accountList, pendingList, allRIds, query_begin, query_end);
            System.out.println(accountList.size() + "------外转" + pendingList.size());

            //System.out.println(accountList.size()+"------4"+pendingList.size());
            //int saveCount=0;
            System.out.println("賬單保存開始");
            if (accountList != null && accountList.size() > 0) {
                synCount = accountList.size();
                //重新封裝賬單加序號
                //按照時間排個序
                Collections.sort(accountList, new Comparator<HdBankStatement>() {
                    @Override
                    public int compare(HdBankStatement o1, HdBankStatement o2) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date dt1 = o1.getAccountDate();
                            Date dt2 = o2.getAccountDate();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                hdBankStatementService.handBankAccountList(accountList, accountDay);
                remoteBillService.addBankStatementList(accountList);
            }
            //System.out.println(synCount+"-----------"+saveCount+"待處理保存開始:");
            if (pendingList != null && pendingList.size() > 0) {
                synCount += pendingList.size();
                for (HdBankPending hdBankPending : pendingList) {
                    if (accountDay != null) {
                        hdBankPending.setAccountDate(accountDay);
                    }
                }
                remoteBillService.addBankPendingList(pendingList);

            }
            //同步后
            Integer saveCountAfter = remoteBillService.getSaveCount(query_begin, query_end)!=null?
                    remoteBillService.getSaveCount(query_begin, query_end):0;
            System.out.println(saveCountAfter + "@@@@@@@@@@" + synCount);
            //实际同步数量
            saveCount = saveCountAfter - due - saveCountBefore;
            if (synCount == 0) {
                saveCount = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("同步失败");
        }
        result.put("synCount", synCount - due);
        result.put("save", saveCount);
        System.out.println("时间:"+format.format(new Date())+
                "同步拿到的数据:"+(synCount-due)+"保存数据:"+ saveCount);
        return R.ok(result, "同步成功");
    }


    /**
     * 账单还原
     */
    //账单还原，只能单个还原，无法判断，银行和摘要无法匹配，如果是九恒星同步的可以还原吗，还原回去要一模一样吗
    //不用把摘要存起来，还原分账和账单一分为二的区别，分账的话相加肯定大于0，不是分账相加等于0
    @ApiOperation("新账单还原")
    @RequestMapping(value = "/returnAccount", method = RequestMethod.POST)
    @ResponseBody
    public R returnAccount(@ApiParam("请求参数") String ids,String accessType){
        List<HdBankStatement> fenAccounts = new ArrayList<>();
        List<HdBankStatement> huaAccounts = new ArrayList<>();
        String msg = "账单还原处理成功";
        if(StringUtil.isEmpty(ids)){
            return R.failed("获取参数失败");
        }
        if(StringUtil.isEmpty(accessType)){
            return R.failed("获取参数失败");
        }

        try{
            for(String id : ids.split(",")){
                HdBankStatement hdBankStatement = hdBankStatementService.getById(id).getData();
                //根据类型判断时间是否过期
                Date accountDate = hdBankStatement.getAccountDate();
                if(accountDate==null){
                    return R.failed("记账时间获取异常");
                }
                long day = jjUtil.getMultiDay(accountDate);
                if("0".equals(accessType)){
                    //管理员为一个月内,当前时间大于
                    if(day>30){
                        return R.failed("还原时间超时一个月");
                    }
                }else{
                    if(day>1){
                        return R.failed("还原时间超时一天");
                    }
                }
                if(hdBankStatement!=null){
                    if("1".equals(hdBankStatement.getSourceType())){
                        fenAccounts.add(hdBankStatement);
                    }
                    if("0".equals(hdBankStatement.getSourceType())||
                            "2".equals(hdBankStatement.getSourceType())){
                        huaAccounts.add(hdBankStatement);
                    }
                }
            }
            hdBankStatementService.handFenAccounts(fenAccounts);
            hdBankStatementService.handHuaAccounts(huaAccounts);
        }catch (Exception e){
            e.printStackTrace();
            msg = "有账单处理失败";
            return R.failed(msg);
        }
        return R.ok(msg);
    }


    /**
     * 根据sourceId删除账单
     */
    @RequestMapping(value = "/deleteBySourceId", method = RequestMethod.POST)
    @ResponseBody
    public void deleteBySourceId(@RequestParam("sourceId") String sourceId){
        if(StringUtil.isNotEmpty(sourceId)){
            remoteBillService.deleteBySourceId(sourceId);
        }
    }

    /**
     * 导入
     */
    /****************************新增导入功能*******************************/
    //导入
    @ApiOperation("导入*")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ){
        List<HdBankStatement> hdBankStatementsEx;
        ExcelUtil excelUtil = new ExcelUtil<HdBankStatement>();
        try{
            hdBankStatementsEx = excelUtil.importXls(file,HdBankStatement.class);
            int count = 0;
            if(hdBankStatementsEx!=null&&hdBankStatementsEx.size()>0){
                //获取最大编号
                int maxNo = hdBankStatementService.getNoMaxNumber(null);
                for(HdBankStatement hdBankStatementEx:hdBankStatementsEx){
                    count++;
                    //公司名必填
                    if(StringUtil.isEmpty(hdBankStatementEx.getCompanyMemberCode())){
                        return R.failed("第"+count+"条账单公司编码为空");
                    }
                    //对方单位名称必填
                    if(StringUtil.isEmpty(hdBankStatementEx.getSubjects())){
                        return R.failed("第"+count+"条账单对方单位名称为空");
                    }
                    //摘要必填
//                    if(StringUtil.isEmpty(hdBankAccountEx.getRemark())){
//                        return new ObjectRestResponse<>(1004,"第"+count+"条账单摘要为空");
//                    }
                    //收入和支出不能同时为空
                    if(hdBankStatementEx.getIncome()==null){
                        hdBankStatementEx.setIncome(BigDecimal.ZERO);
                    }
                    if(hdBankStatementEx.getPay()==null){
                        hdBankStatementEx.setPay(BigDecimal.ZERO);
                    }
                    if(hdBankStatementEx.getIncome().compareTo(BigDecimal.ZERO)==0&&
                            hdBankStatementEx.getPay().compareTo(BigDecimal.ZERO)==0){
                        return R.failed("第"+count+"条账单收入或支出必须为有效值");
                    }
                    //银行账号必填
                    if(StringUtil.isEmpty(hdBankStatementEx.getBankAccount())){
                        return R.failed("第"+count+"条账单银行账户为空");
                    }
                    //如果到账日期为空当前日期，记账日期为当前日期
                    if(hdBankStatementEx.getSynaccountDate()==null){
                        hdBankStatementEx.setSynaccountDate(new Date());
                    }
                    hdBankStatementEx.setAccountDate(new Date());
                    //凭证编号+1
                    hdBankStatementEx.setNo(new BigDecimal(maxNo));
                    hdBankStatementEx.setBy2(String.valueOf(maxNo));
                    //银行账号获取id同时获取银行名称
                    HdBankAccount hdBankAccount = remoteBaseService.getAccountByTerm(null,hdBankStatementEx.getBankAccount());
                    if(hdBankAccount==null){
                        return R.failed("第"+count+"条账单银行账户无法匹配");
                    }
                    hdBankStatementEx.setBankAccountId(hdBankAccount.getId());
                    hdBankStatementEx.setBankName(hdBankAccount.getBankName());
                    //公司名称转id
//                    HdCompany hdCompany = baseMetaFeign.getComById(hdBankAccountEx.getCompanyName());
//                    if(hdCompany==null){
//                        return new ObjectRestResponse<>(1004,"第"+count+"条账单公司系统无法匹配");
//                    }
//                    hdBankAccountEx.setCompanyName(hdCompany.getId());
                    //公司名称改成成员单位匹配
                    //改成公司编码匹配
                    HdCompanyMember hdCompanyMember = remoteBaseService.getMembercomByCode(hdBankStatementEx.getCompanyMemberCode());
                    if(hdCompanyMember==null){
                        return R.failed("第"+count+"条账单成员单位编码系统无法匹配");
                    }
                    hdBankStatementEx.setCompanyMemberId(hdCompanyMember.getId());
                    hdBankStatementEx.setCompanyId(hdCompanyMember.getCompanyId());
                    //账单类型为手工帐
                    hdBankStatementEx.setAccountType("3");
                    hdBankStatementEx.setSourceType("4");
                    //hdBankAccountEx.setCreateBy(BaseContextHandler.getUserID());
                    //hdBankAccountEx.setTenantId(BaseContextHandler.getTenantID());
                    maxNo++;
                }
                remoteBillService.addBankStatementList(hdBankStatementsEx);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入失败,请检查导入格式是否正确");
        }
        return R.ok(null,"导入成功");
    }

    /********************************************新增缺单**********************************/
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @GetMapping("/missingOrder")
    public R missingOrder(@RequestParam("id") String id){
        //判断是否存在id
        if(StringUtil.isEmpty(id)){
            return R.failed("参数传递失败");
        }
        String message = null;
        message = "设置成功";
        try {
            HdBankStatement hdBankStatement = hdBankStatementService.getById(id).getData();
            if("0".equals(hdBankStatement.getBy3())){
                hdBankStatement.setBy3("1");
            }else{
                hdBankStatement.setBy3("0");
            }
            hdBankStatementService.updateById(hdBankStatement);
        } catch (Exception e) {
            e.printStackTrace();
            message = "设置失败";
            return R.failed( message);
        }

        return R.ok(message);
    }


}
