package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.util.DateUtils;
import com.github.wxiaoqi.security.common.util.ExcelUtil;
import com.github.wxiaoqi.security.common.util.MyBeanUtils;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankPendingBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankRecordBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankStatementGfBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.common.MR;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdFenListEntity;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankPendingFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankRecordFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.StringUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("hdBankPending")
@Api(tags = "待处理管理模块")
public class HdBankPendingController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    HdBankPendingBiz hdBankPendingBiz;

    @Autowired
    HdBankRecordBiz hdBankRecordBiz;

    @Autowired
    HdBankStatementGfBiz hdBankStatementGfBiz;

    @Autowired
    HdBankPendingFeign hdBankPendingFeign;

    @Autowired
    HdBankRecordFeign hdBankRecordFeign;



    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询单个对象")
    public R<HdBankPending> get(@PathVariable String id) {
        R<HdBankPending> entityObjectRestResponse = new R();
        HdBankPending o = hdBankPendingFeign.selectById(id).getData();
        if(o!=null){
            if(StringUtil.isNotEmpty(o.getBankAccountId())){
                o = hdBankPendingBiz.addAccountInfo(o);
            }
        }
        entityObjectRestResponse.setData(o);
        return entityObjectRestResponse;
    }



    /**
     * 页面展示
     */
    //添加录入人条件6.24
    @ApiOperation("分页获取数据2")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public MR page(@RequestParam @ApiParam(value = "参数名称(subjects,page,limit,dateStart,dataEnd,remark," +
                                                                                     "bankName,mySubjects)") Map<String, Object> params) {
        //查询列表数据
        //需求更改，待处理都可以看见
        long total = 0;
        List<HdBankPending> hdBankPendingList = new ArrayList<>();
        List<HdBankPending> allBankPendingList = new ArrayList<>();
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        //除了公司其他都为模糊查询,所有都为模糊查询,除了时间
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        params.put("page",page);
        params.put("limit",limit);
        hdBankPendingList = hdBankPendingFeign.getBankPendingList2(params);
        hdBankPendingList = hdBankPendingBiz.addBankAccount(hdBankPendingList);
        Map<String,Object> allOtherVal = hdBankPendingFeign.getOtherVal(params);
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        if(allOtherVal!=null&&allOtherVal.size()>0){
            if(allOtherVal.get("income")!=null){
                sumIncome = new BigDecimal(allOtherVal.get("income").toString());
            }
            if(allOtherVal.get("pay")!=null){
                sumPay = new BigDecimal(allOtherVal.get("pay").toString());
            }
            if(allOtherVal.get("total")!=null){
                total = Integer.valueOf(allOtherVal.get("total").toString());
            }
        }
        return new MR(total, hdBankPendingList,sumIncome, sumPay);
    }





    /**
     * 删除银行待处理账单
     *
     * @return
     */
    @ApiOperation("删除银行待处理账单")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public R doDel(@PathVariable("id") String id) {
        if (StringUtil.isEmpty(id)) {
            return R.failed("参数传递失败");
        }
        int b = 1;
        String message = null;
        int count = 0;
        try {
            b = hdBankPendingFeign.removeById(id).getCode();
            count++;
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行待处理账单删除失败";
            return R.failed(message);
        }
        if(b==0){
            message = "删除成功";
            return R.ok(message);
        }else{
            message = "银行待处理账单删除失败";
            return R.failed(message);
        }
    }





    /**
     * 批量删除银行待处理账单
     *
     * @return
     */
    @ApiOperation("批量删除银行待处理账单")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }

        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                hdBankPendingFeign.removeById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行待处理账单删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }




    /**
     * 划账
     */
    @ApiOperation("划账")
    @RequestMapping(value = "/doHua", method = RequestMethod.POST)
    @ResponseBody
    public R doHua(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String companyId = jo.getString("companyId");
        String accountDate = jo.getString("accountDate");
        String remark = jo.getString("remark");
        String ids = jo.getString("ids");
        //需求更改增加处理人,同步人和处理人不一定一人
        String message = null;
        Date accountDay = null;
        List<HdBankRecord> hdBankRecordList = new ArrayList<>();
        if (StringUtil.isEmpty(companyId)) {
            return R.failed("参数传递失败");
        }
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        //公司是否存在前端已做控制
        try {
            for (String id : ids.split(",")) {
                HdBankStatement bank = new HdBankStatement();
                HdBankPending pending = hdBankPendingFeign.selectById(id).getData();
                MyBeanUtils.copyBeanNotNull2Bean(pending, bank);
                if(StringUtil.isNotEmpty(accountDate)){
                    accountDay = DateUtils.date_sdf.parse(accountDate);
                    bank.setAccountDate(accountDay);
                }
                bank.setId(null);
                bank.setCompanyId(companyId);
                bank.setSourceType("0");
                if (StringUtil.isNotEmpty(remark)) {
                    bank.setRemark(remark);
                }
//                bank.setCreateBy(BaseContextHandler.getUserID());
                bank.setAccountType(bank.getRid()!=null?"0":"1");
                //银行账单录入
                hdBankStatementGfBiz.insertEntityFreeTime(bank);
                //待处理账单删除
                hdBankPendingFeign.removeById(id);
                //批量插入记录表
                hdBankRecordList.add(hdBankRecordBiz.getHdBankRecord(bank, "划账"));
            }
            hdBankRecordBiz.batchSave(hdBankRecordList);
            message = "划账成功";
        } catch (Exception e) {
            e.printStackTrace();
            message = "划账失败";
            return R.failed(500, message);
        }

        return R.ok(message);
    }










    /**
     * 分账
     */
    @ApiOperation("分账")
    @RequestMapping(value = "/doFen", method = RequestMethod.POST)
    @ResponseBody
    public R doFen(@RequestBody String map) {
        //需求更改增加处理人,同步人和处理人不一定一人
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String id = jo.getString("id");
        String accountDate = jo.getString("accountDate");
        String hdFenStr = jo.getString("hdFenStr");
        String message = null;
        Date accountDay = null;
        List<HdBankRecord> hdBankRecords = new ArrayList<>();
        if (com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(id)) {
            return R.failed("传入参数失败");
        }
        if (com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(hdFenStr)) {
            return R.failed("传入参数失败");
        }
        //List<Object> strList = JSONArray.parseArray(str);
        List<HdFenListEntity> hdFenList = new ArrayList<>();
        hdFenList = JSONArray.parseArray(hdFenStr, HdFenListEntity.class);
        try{
            //检查公司是否为空
            for (HdFenListEntity hdFen : hdFenList) {
                if (com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(hdFen.getCompanyId())) {
                    return R.failed("分账有公司名为空");
                }
                if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(hdFen.getMoney())){
                    hdFen.setMoney(new DecimalFormat().parse(hdFen.getMoney()).toString());
                }else{
                    hdFen.setMoney("0.00");
                }
            }
            //先给金额排个序
            Collections.sort(hdFenList, new Comparator<HdFenListEntity>() {
                @Override
                public int compare(HdFenListEntity o1, HdFenListEntity o2) {
                    int a = new BigDecimal(o1.getMoney()).compareTo(new BigDecimal(o2.getMoney()));
                    if (a == 1) {
                        return 1;
                    } else if (a == -1) {
                        return -1;
                    } else {
                        return 0;
                    }
                }

            });

            HdBankPending hdBankPending = hdBankPendingFeign.selectById(id).getData();
            if (hdBankPending == null) {
                return R.failed("数据查询异常");
            }
            //先录入一笔最大的金额进公司
            HdFenListEntity maxFenListEntity = hdFenList.get(hdFenList.size() - 1);
            HdBankStatement maxBankAccount = new HdBankStatement();
            MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, maxBankAccount);
            maxBankAccount.setCompanyId(maxFenListEntity.getCompanyId());
            maxBankAccount.setId(null);
            if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)){
                accountDay = DateUtils.date_sdf.parse(accountDate);
                maxBankAccount.setAccountDate(accountDay);
            }
//            maxBankAccount.setCreateBy(BaseContextHandler.getUserID());
            maxBankAccount.setAccountType(maxBankAccount.getRid()!=null?"0":"1");
            hdBankStatementGfBiz.insertEntityFreeTime(maxBankAccount);
            hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(maxBankAccount, "分账"));
            //最后一笔相反操作
            hdFenList.remove(hdFenList.size() - 1);
            //记录操作信息
            //先判断是为支出还是收入
            BigDecimal bPay = hdBankPending.getPay() != null ? hdBankPending.getPay() : BigDecimal.ZERO;
            if (bPay.compareTo(BigDecimal.ZERO) == 0) {
                //支出为0即为收入
                for (HdFenListEntity e : hdFenList) {
                    HdBankStatement bankIncome = new HdBankStatement();
                    HdBankStatement bankPay = new HdBankStatement();
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankIncome);
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankPay);
                    bankIncome.setCompanyId(e.getCompanyId());
                    bankIncome.setIncome(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    //bankIncome.setRid(hdBankPending.getRid());
                    bankIncome.setId(null);
                    bankIncome.setSourceType("1");
                    if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankIncome.setAccountDate(accountDay);
                    }
//                    bankIncome.setCreateBy(BaseContextHandler.getUserID());
                    bankIncome.setAccountType(bankIncome.getRid()!=null?"0":"1");
                    hdBankStatementGfBiz.insertEntityFreeTime(bankIncome);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(bankIncome, "分账"));
                    bankPay.setCompanyId(maxFenListEntity.getCompanyId());
                    bankPay.setIncome(new BigDecimal("0.00"));
                    bankPay.setPay(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    //bankPay.setRid(hdBankPending.getRid());
                    bankPay.setId(null);
                    bankPay.setSourceType("1");
                    if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankPay.setAccountDate(accountDay);
                    }
//                    bankPay.setCreateBy(BaseContextHandler.getUserID());
                    bankPay.setAccountType(bankPay.getRid()!=null?"0":"1");
                    hdBankStatementGfBiz.insertEntityFreeTime(bankPay);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(bankPay, "分账"));
                }
            } else {
                //支出不为0即为支出
                for (HdFenListEntity e : hdFenList) {
                    HdBankStatement bankIncome = new HdBankStatement();
                    HdBankStatement bankPay = new HdBankStatement();
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankIncome);
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, bankPay);
                    bankIncome.setCompanyId(e.getCompanyId());
                    bankIncome.setPay(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    //bankIncome.setRid(hdBankPending.getRid());
                    bankIncome.setId(null);
                    bankIncome.setSourceType("1");
                    if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankIncome.setAccountDate(accountDay);
                    }
//                    bankIncome.setCreateBy(BaseContextHandler.getUserID());
                    bankIncome.setAccountType(bankIncome.getRid()!=null?"0":"1");
                    hdBankStatementGfBiz.insertEntityFreeTime(bankIncome);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(bankIncome, "分账"));
                    bankPay.setCompanyId(maxFenListEntity.getCompanyId());
                    bankPay.setPay(new BigDecimal("0.00"));
                    //bankPay.setRid(hdBankPending.getRid());
                    bankPay.setIncome(e.getMoney()!=null?new BigDecimal(e.getMoney()):BigDecimal.ZERO);
                    bankPay.setId(null);
                    bankPay.setSourceType("1");
                    if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)){
                        accountDay = DateUtils.date_sdf.parse(accountDate);
                        bankPay.setAccountDate(accountDay);
                    }
//                    bankPay.setCreateBy(BaseContextHandler.getUserID());
                    bankPay.setAccountType(bankPay.getRid()!=null?"0":"1");
                    hdBankStatementGfBiz.insertEntityFreeTime(bankPay);
                    //记录操作信息
                    hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(bankPay, "分账"));
                }
            }
            //删除原分账记录
            hdBankPendingFeign.removeById(id);
            //批量记录操作信息
            hdBankRecordBiz.batchSave(hdBankRecords);


        }catch (ParseException e) {
            e.printStackTrace();
            return R.failed("同步至某日日期格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            message = "分账失败";
            return R.failed(message);
        }

        return R.ok(message);
    }





    /**
     * 导出
     */
    //添加录入人条件6.24
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
//    @IgnoreUserToken
//    @IgnoreClientToken
    public void exportXls(@RequestParam Map<String, Object> params) throws ParseException {
        List<HdBankPending> allBankPendingList = hdBankPendingBiz.getAllBankPending(params);
        allBankPendingList = hdBankPendingBiz.addBankAccount(allBankPendingList);
        ExcelUtil excelUtil = new ExcelUtil<HdBankPending>();
        try {
            excelUtil.print("银行待处理记录列表", HdBankPending.class, "银行待处理记录列表", "导出时间:" + jjUtil.getDateStr(), "银行待处理记录列表", allBankPendingList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /*********************************标准版本二次修改*********************************/
    //修改日期，无须修改编号，录入记录，同时修改摘要
    @RequestMapping(value = "/changeAccountDate", method = RequestMethod.POST)
    @ResponseBody
    public R changeAccountDate(String ids, String accountDate){
        if(com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        if(com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(accountDate)){
            return R.failed(1003,"参数传递失败");
        }
        Date accountDay = null;
        String remark = "";
        if (com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)) {
            try {
                accountDay = DateUtils.date_sdf.parse(accountDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return R.failed("同步至某日日期格式错误");
            }
        }
        for(String id : ids.split(",")){
            HdBankPending hdBankPending = hdBankPendingFeign.selectById(id).getData();
            if(hdBankPending==null){
                return R.failed("数据获取异常");
            }
            hdBankPending.setAccountDate(accountDay);
            if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(hdBankPending.getRemark())){
                remark = hdBankPending.getRemark();
            }
            remark = remark+";九恒星记账日期:"+hdBankPending.getAccountDate();
            hdBankPending.setRemark(remark);
            hdBankPending.setCreateBy(BaseContextHandler.getUserID());
            hdBankPendingFeign.updateById(hdBankPending);
            HdBankRecord hdbankRecord = hdBankRecordBiz.getHdBankRecord(hdBankPending,"待处理修改九恒星日期");
            hdBankRecordFeign.insertSelective(hdbankRecord);
        }
        return R.ok("修改成功");
    }


    //待处理查看总收入和总支出
    @ApiOperation("新查询总收入和总支出")
    @RequestMapping(value = "/getSumMoney", method = RequestMethod.POST)
    @ResponseBody
    public R<Map<String,Object>> getSumMoney(String ids){
        if(com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        Map<String,Object> result = new HashedMap();
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        for(String id:ids.split(",")){
            HdBankPending hdBankPending = hdBankPendingFeign.selectById(id).getData();
            if(hdBankPending!=null){
                sumIncome = sumIncome.add(hdBankPending.getIncome());
                sumPay = sumPay.add(hdBankPending.getPay());
            }

        }
        result.put("sumIncome",sumIncome);
        result.put("sumPay",sumPay);
        result.put("num",ids.split(",").length);
        return R.ok(result,"获取成功");
//        return new ObjectRestResponse<>(200,"获取成功").data(result);
    }




    //新的分账功能
    @ApiOperation("新分账")
    @RequestMapping(value = "/doNewFen", method = RequestMethod.POST)
    @ResponseBody
    public R doNewFen(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String id = jo.getString("id");
        String accountDate = jo.getString("accountDate");
        String hdFenStr = jo.getString("hdFenStr");
        String message = null;
        Date accountDay = null;
        List<HdBankRecord> hdBankRecords = new ArrayList<>();
        if (com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(id)) {
            return R.failed("传入参数失败");
        }
        if (com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(hdFenStr)) {
            return R.failed("传入参数失败");
        }
        //List<Object> strList = JSONArray.parseArray(str);
        List<HdFenListEntity> hdFenList = new ArrayList<>();
        hdFenList = JSONArray.parseArray(hdFenStr, HdFenListEntity.class);
        try{
            HdBankPending hdBankPending = hdBankPendingFeign.selectById(id).getData();
            if (hdBankPending == null) {
                return R.failed("数据查询异常");
            }
            //检查公司是否为空
//            HdBankRecord hdBankRecord = hdBankRecordBiz.getHdBankRecord(hdBankPending, "分账");
//            hdBankRecords.add(hdBankRecord);
            for (HdFenListEntity hdFen : hdFenList) {
                if (com.github.wxiaoqi.security.common.util.StringUtil.isEmpty(hdFen.getCompanyId())) {
                    return R.failed("分账有公司名为空");
                }
                if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(hdFen.getMoney())){
                    hdFen.setMoney(new DecimalFormat().parse(hdFen.getMoney()).toString());
                }else{
                    hdFen.setMoney("0.00");
                }
                HdBankStatement hdBankAccount  = new HdBankStatement();
                MyBeanUtils.copyBeanNotNull2Bean(hdBankPending, hdBankAccount);
                if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(accountDate)){
                    accountDay = DateUtils.date_sdf.parse(accountDate);
                    hdBankAccount.setAccountDate(accountDay);
                }
                hdBankAccount.setId(null);
                hdBankAccount.setCompanyId(hdFen.getCompanyId());
                hdBankAccount.setSourceType("1");
                //hdBankAccountBiz.insertEntityFreeTime(hdBankAccount);

                BigDecimal bPay = hdBankPending.getPay() != null ? hdBankPending.getPay() : BigDecimal.ZERO;
                if (bPay.compareTo(BigDecimal.ZERO) == 0) {
                    hdBankAccount.setIncome(hdFen.getMoney()!=null?new BigDecimal(hdFen.getMoney()):BigDecimal.ZERO);
                }else{
                    hdBankAccount.setPay(hdFen.getMoney()!=null?new BigDecimal(hdFen.getMoney()):BigDecimal.ZERO);
                }
                hdBankStatementGfBiz.insertEntityFreeTime(hdBankAccount);
                hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(hdBankAccount, "分账"));
            }
            //删除原分账记录
            hdBankPendingFeign.removeById(id);
            //批量记录操作信息
            hdBankRecordBiz.batchSave(hdBankRecords);
        }catch (ParseException e) {
            e.printStackTrace();
            return R.failed("同步至某日日期格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            message = "分账失败";
            return R.failed(message);
        }

        return R.ok(message);
    }







}
