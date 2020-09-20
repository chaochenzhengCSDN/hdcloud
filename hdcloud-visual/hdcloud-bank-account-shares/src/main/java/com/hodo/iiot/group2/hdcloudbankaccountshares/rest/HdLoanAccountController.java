package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.wxiaoqi.security.common.util.MyBeanUtils;
import com.github.wxiaoqi.security.common.util.StringUtil;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdLoanAccountBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdLoanAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdLoanAccountDetailXls;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdLoanAccountXls;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdLoanAccountFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("hdLoanAccount")
@Api(tags = "借账管理模块")
public class HdLoanAccountController {

    @Autowired
    HdLoanAccountFeign loanAccountFeign;

    @Autowired
    HdLoanAccountBiz hdLoanAccountBiz;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;


    //重写page
    @ApiOperation(value = "分页获取数据2*")
    @RequestMapping(value = "/findRecords", method = RequestMethod.GET)
    @ResponseBody
    public R page(@RequestParam @ApiParam(value = "参数名称(除了dateStart,dataEnd,moneyStart,moneyEnd,method,bank)") Map<String, Object> params) {
        long total = 0;
        IPage iPage = new Page();
        //根据条件获取总数
        List<HdLoanAccount> hdLoanAccounts = (List)(((LinkedHashMap)loanAccountFeign.getHdLoadAccount(params).getData()).get("records"));
        for (int i=0;i<hdLoanAccounts.size();i++){
            HdLoanAccount loanAccount = JSON.parseObject(JSONObject.toJSONString(hdLoanAccounts.get(i),true),HdLoanAccount.class);
            hdLoanAccounts.set(i,loanAccount);
        }
        total = (Integer)(((LinkedHashMap)loanAccountFeign.getHdLoadAccount(params).getData()).get("total"));
        //加入银行账号和开户行
        if(hdLoanAccounts!=null&&hdLoanAccounts.size()>0){
            for(HdLoanAccount hdLoanAccount:hdLoanAccounts){
                String bankId = hdLoanAccount.getBankId();
                if(StringUtil.isNotEmpty(bankId)){
                    //根据账户id
                    hdLoanAccountBiz.addBankInfo(hdLoanAccount);
                }
            }
        }
        iPage.setRecords(hdLoanAccounts);
        iPage.setTotal(total);
        return R.ok(iPage);
    }




    //导出
    @ApiOperation(value = "导出*")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam @ApiParam(value = "参数名称(dateStart,dataEnd,moneyStart,moneyEnd,method,bank)") Map<String, Object> params){
        List<HdLoanAccount> hdLoanAccounts = new ArrayList<>();
        hdLoanAccounts = loanAccountFeign.getAllHdLoadAccounts(params);
        //处理一下银行账户开户行，还有类型
        if(hdLoanAccounts!=null&&hdLoanAccounts.size()>0){
            for(HdLoanAccount hdLoanAccount:hdLoanAccounts){
                if("0".equals(hdLoanAccount.getMethod())){
                    hdLoanAccount.setMethod("抵押");
                }else{
                    hdLoanAccount.setMethod("保证");
                }
                String bankId = hdLoanAccount.getBankId();
                if(StringUtil.isNotEmpty(bankId)){
                    //根据账户id
                    hdLoanAccountBiz.addBankInfo(hdLoanAccount);
                }
                //批复授信期限
                if(StringUtil.isNotEmpty(hdLoanAccount.getDeadlineregion())){
                    hdLoanAccount.setDeadlineregion(hdLoanAccount.getDeadlineregion().replace(",","~"));
                }
            }
        }
        ExcelUtil excelUtil = new ExcelUtil<HdLoanAccount>();
        try {
            excelUtil.print("借账账单记录列表", HdLoanAccount.class, "借账账单记录列表", "导出时间:" + jjUtil.getDateStr(), "借账账单记录列表", hdLoanAccounts, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    //导入
    @ApiOperation(value = "导入*")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ){
        List<HdLoanAccountXls> hdLoanAccountXlss = null;
        List<HdLoanAccountDetailXls> hdLoanAccountDetailXlss = null;
        HdLoanAccountDetailXls hdLoanAccountDetailXls = null;
        List<HdLoanAccount> hdLoanAccounts = new ArrayList<>();
        ExcelUtil excelUtil = new ExcelUtil<HdLoanAccountXls>();
        try{
            hdLoanAccountXlss = excelUtil.importXls(file,HdLoanAccountXls.class);
            int count = 0;
            if(hdLoanAccountXlss!=null){
                for(HdLoanAccountXls entityXls:hdLoanAccountXlss){
                    HdLoanAccount entity = new HdLoanAccount();
                    hdLoanAccountDetailXlss = entityXls.getDetailList();
                    MyBeanUtils.copyBeanNotNull2Bean(entityXls, entity);
                    if(hdLoanAccountDetailXlss!=null&&hdLoanAccountDetailXlss.size()>0){
                        hdLoanAccountDetailXls = hdLoanAccountDetailXlss.get(0);
                        MyBeanUtils.copyBeanNotNull2Bean(hdLoanAccountDetailXls, entity);
                    }
                    count++;
                    //获取银行账户对应银行id
                    if(StringUtil.isEmpty(entity.getBankAccount())){
                        return R.failed("第" + count + "条数据银行账户为空！");
                    }
                    //根据银行账户查询对应id
                    String bankId = hdLoanAccountBiz.getIdByBankAccount(entity.getBankAccount());
                    if(StringUtil.isEmpty(bankId)){
                        return  R.failed("第" + count + "条数据银行账户无法匹配！");
                    }
                    entity.setBankId(bankId);
                    if(StringUtil.isEmpty(entity.getDeadlineregion())){
                        return  R.failed("第" + count + "条数据批复授信期限为空！");
                    }
                    if(StringUtil.isEmpty(entity.getMoney())){
                        return  R.failed("第" + count + "条数据请确认金额！");
                    }
                    if(entity.getLoandate()==null){
                        return  R.failed("第" + count + "条数据请确认贷款日！");
                    }
                    if(entity.getDeaddate()==null){
                        return  R.failed("第" + count + "条数据请确认到账日！");
                    }
                    if(StringUtil.isEmpty(entity.getRate())){
                        return  R.failed("第" + count + "条数据请确认利率！");
                    }
                    if(StringUtil.isEmpty(entity.getMethod())){
                        return  R.failed("第" + count + "条数据请确认借款方式！");
                    }
                    //检查授信时间
                    if(StringUtil.isEmpty(entity.getDeadlineregion())){
                        return  R.failed("第" + count + "条数据请确认批复授信期限！");
                    }
                    if(!entity.getDeadlineregion().contains("~")){
                        return  R.failed("第" + count + "条数据请确认批复授信期限分隔符错误！");
                    }
                    //检查时间期限格式问题
                    String[] deadlineregions = entity.getDeadlineregion().split("~");
                    if(deadlineregions!=null&&deadlineregions.length>0){
                        Boolean start = jjUtil.checkDateFormat(deadlineregions[0]);
                        Boolean end = jjUtil.checkDateFormat(deadlineregions[1]);
                        if(!start||!end){
                            return R.failed("第" + count + "条数据请确认批复授信期限时间格式！");
                        }
                    }
                    if(StringUtil.isNotEmpty(entity.getDeadlineregion())){
                        entity.setDeadlineregion(entity.getDeadlineregion().replace("~",","));
                    }

                    //金额处理
                    entity.setPassedCredit(jjUtil.formatMoney(entity.getPassedCredit()));
                    entity.setUsedCredit(jjUtil.formatMoney(entity.getUsedCredit()));
                    entity.setUseableCredit(jjUtil.formatMoney(entity.getUseableCredit()));
                    entity.setPlanCredit(jjUtil.formatMoney(entity.getPlanCredit()));
                    entity.setMoney(jjUtil.formatMoney(entity.getMoney()));
                    if(StringUtil.isNotEmpty(entity.getRate())){
                        //利率控制四位小数
                        String rateStr = entity.getRate().replace("%","");
                        BigDecimal bGate = new BigDecimal(rateStr);
                        DecimalFormat df = new DecimalFormat("0.0000");
                        if(!entity.getRate().contains("%")){
                            entity.setRate(df.format(bGate)+"%");
                        }
                    }
                    if(StringUtil.isNotEmpty(entity.getMethod())){
                        if("抵押".equals(entity.getMethod())){
                            entity.setMethod("0");
                        }else
                        if("保证".equals(entity.getMethod())){
                            entity.setMethod("1");
                        }
                    }
                    if(StringUtil.isNotEmpty(entity.getDeadlineregion())){
                        entity.setDeadlineregion(entity.getDeadlineregion().replace("~",","));
                    }
                    hdLoanAccounts.add(entity);
                }
                hdLoanAccountBiz.batchSave(hdLoanAccounts);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入出错,请检查字段格式是否正确");
        }
        return R.ok("导入成功");
    }





    //批量删除
    @ApiOperation("批量删除公司名匹配表*")
    @RequestMapping(value = "/batchDel", method = RequestMethod.POST)
    @ResponseBody
    public R batchDel(@RequestBody String map){
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try{
            for(String id:ids.split(",")){
                loanAccountFeign.removeById(id);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
            message = "借账删除失败";
            return R.failed(message);
        }
        message = "你成功删除"+count+"条";
        return R.ok(message);
    }




    //查看重写
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询单个对象")
    public R<HdLoanAccount> get(@PathVariable String id) {
        R<HdLoanAccount> entityObjectRestResponse = new R<>();
        HdLoanAccount o = loanAccountFeign.selectById(id).getData();
        if(o!=null){
            hdLoanAccountBiz.addBankName(o);
        }
        entityObjectRestResponse.setData(o);
        return entityObjectRestResponse;
    }




    //重写添加去除千分符
    @RequestMapping(
            value = {""},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    @ApiOperation("新增单个对象*")
    public R<HdLoanAccount> add(@RequestBody HdLoanAccount entity) {
        entity.setPassedCredit(jjUtil.formatMoney(entity.getPassedCredit()));
        entity.setUsedCredit(jjUtil.formatMoney(entity.getUsedCredit()));
        entity.setUseableCredit(jjUtil.formatMoney(entity.getUseableCredit()));
        entity.setPlanCredit(jjUtil.formatMoney(entity.getPlanCredit()));
        entity.setMoney(jjUtil.formatMoney(entity.getMoney()));
        if(StringUtil.isNotEmpty(entity.getRate())){
            //利率控制四位小数
            String rateStr = entity.getRate().replace("%","");
            BigDecimal bGate = new BigDecimal(rateStr);
            DecimalFormat df = new DecimalFormat("0.0000");
            if(!entity.getRate().contains("%")){
                entity.setRate(df.format(bGate)+"%");
            }
        }
        loanAccountFeign.insertSelective(entity);
        return R.ok(entity);
    }




    //重写编辑去除千分符
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.PUT}
    )
    @ResponseBody
    @ApiOperation("更新单个对象*")
    public R<HdLoanAccount> update(@RequestBody HdLoanAccount entity) {
        entity.setPassedCredit(jjUtil.formatMoney(entity.getPassedCredit()));
        entity.setUsedCredit(jjUtil.formatMoney(entity.getUsedCredit()));
        entity.setUseableCredit(jjUtil.formatMoney(entity.getUseableCredit()));
        entity.setPlanCredit(jjUtil.formatMoney(entity.getPlanCredit()));
        entity.setMoney(jjUtil.formatMoney(entity.getMoney()));
        if(StringUtil.isNotEmpty(entity.getRate())){
            //利率控制四位小数
            String rateStr = entity.getRate().replace("%","");
            BigDecimal bGate = new BigDecimal(rateStr);
            DecimalFormat df = new DecimalFormat("0.0000");
            if(!entity.getRate().contains("%")){
                entity.setRate(df.format(bGate)+"%");
            }
        }
        loanAccountFeign.updateSelectiveById(entity);
        return R.ok(entity);
    }




}
