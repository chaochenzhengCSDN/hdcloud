package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.ExcelUtil;
import com.github.wxiaoqi.security.common.util.StringUtil;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankTenantBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdReportBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.common.MR;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.FileUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.RedissonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("hdReport")
@Api(tags = "报表管理模块")
public class HdReportController {


    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    private HdReportBiz hdReportBiz;
    @Autowired
    HdBankTenantBiz hdBankTenantBiz;
    @Autowired
    private RedissonUtil redissonUtil;



    @ApiOperation("获取公司报表*")
    @RequestMapping(value = "/getComStatisticsList", method = RequestMethod.GET)
    @ResponseBody
    public MR getComStatisticsList(String dateStart, String dateEnd) {
        long total = 0;
        Integer tenantId = TenantContextHolder.getTenantId();
        Statistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        List<Statistics> statisticsList = new ArrayList<>();
        try{
//            User user = iUserFeign.getById(userId);
//            if (user != null) {
//                if (StringUtil.isNotEmpty(user.getAttr1()) &&
//                        ("1").equals(user.getAttr1())) {
//                    userId = null;
//                }
//            }
            if (StringUtil.isEmpty(dateStart)) {
                return new MR(total, new ArrayList<Statistics>(), BigDecimal.ZERO, BigDecimal.ZERO,null,null);
            }
            if (StringUtil.isEmpty(dateEnd)) {
                return new MR(total, new ArrayList<Statistics>(), BigDecimal.ZERO, BigDecimal.ZERO,null,null);
            }
            statisticsList = hdReportBiz.statics(dateStart, dateEnd, tenantId,null);
            if(statisticsList!=null&&statisticsList.size()>0){
                last = statisticsList.get(statisticsList.size()-1);
                sumIncome = last.getCurrentIncome();
                sumPay = last.getCurrentPay();
                sumBegin = last.getBeginBalance();
                sumEnd = last.getEndBalance();
                statisticsList.remove(statisticsList.size()-1);
                total = statisticsList.size();
            }
        }catch (Exception e){
            e.printStackTrace();
            return MR.failed("获取失败");
        }
        return new MR(total, statisticsList, sumIncome, sumPay,sumBegin,sumEnd);
    }

    @ApiOperation("导出公司报表*")
    @RequestMapping(value = "/exportStatisticsXls", method = RequestMethod.GET)
    public void exportStatisticsXls(String dateStart, String dateEnd,String userName){
        Integer tenantId = TenantContextHolder.getTenantId();

        Statistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        List<Statistics> statisticsList = hdReportBiz.statics(dateStart, dateEnd, tenantId,null);
        ExcelUtil excelUtil = new ExcelUtil<BankStatistics>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            excelUtil.print("公司账单报表", Statistics.class, "公司账单报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "公司账单报表", statisticsList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //公司报表
    @ApiOperation("获取各个公司所有银行报表明细*")
    @RequestMapping(value = "/getComBankStatistics", method = RequestMethod.GET)
    public MR getComBankStatistics(String dateStart, String dateEnd){
        long total = 0;
        String tenantId = BaseContextHandler.getTenantID();
        ComBankStatistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        List<ComBankStatistics> comBankstatistics = new ArrayList<>();
        if (StringUtil.isEmpty(dateStart)) {
            return MR.failed("起始时间获取失败");
        }
        if (StringUtil.isEmpty(dateEnd)) {
            return MR.failed("截止时间获取失败");
        }
        try{
            comBankstatistics = hdReportBiz.getComBankStatistics(dateStart,
                    dateEnd);
            if(comBankstatistics!=null&&comBankstatistics.size()>0){
                last = comBankstatistics.get(comBankstatistics.size()-1);
                sumIncome = last.getCurrentIncome();
                sumPay = last.getCurrentPay();
                sumBegin = last.getBeginBalance();
                sumEnd = last.getEndBalance();
                comBankstatistics.remove(comBankstatistics.size()-1);
                total = comBankstatistics.size();
            }
        }catch (Exception e){
            e.printStackTrace();
            return MR.failed("获取失败");
        }
        return new MR(total, comBankstatistics, sumIncome, sumPay,sumBegin,sumEnd);
    }


    //导出
    @ApiOperation("导出各个公司所有银行报表明细*")
    @RequestMapping(value = "/exportComBankStatistics", method = RequestMethod.GET)
    public void exportComBankStatistics(String dateStart, String dateEnd){
        long total = 0;
        String tenantId = BaseContextHandler.getTenantID();
        List<ComBankStatistics> comBankstatistics = new ArrayList<>();
        try{
            comBankstatistics = hdReportBiz.getComBankStatistics(dateStart,
                    dateEnd);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ExcelUtil excelUtil = new ExcelUtil<ComBankStatistics>();
            excelUtil.print("公司账户报表明细", ComBankStatistics.class, "公司账户报表明细", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "公司账户报表明细", comBankstatistics, request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }







    //银行余额报表
    @ApiOperation("获取银行报表*")
    @RequestMapping(value = "/getBankStatistcs", method = RequestMethod.GET)
    @ResponseBody
    public MR getBankStatistcs(String dateStart,
                                                                                              String dateEnd,String page,String limit) {
        int total = 0;
        BankStatistics last;
        if (StringUtil.isEmpty(dateStart)) {
            return new MR(total, new ArrayList<BankStatistics>(), BigDecimal.ZERO, BigDecimal.ZERO,null,null);
        }
        if (StringUtil.isEmpty(dateEnd)) {
            return new MR(total, new ArrayList<BankStatistics>(), BigDecimal.ZERO, BigDecimal.ZERO,null,null);
        }
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal sumPassedCredit = BigDecimal.ZERO;
        BigDecimal sumUsedCredit = BigDecimal.ZERO;
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        List<BankStatistics> result = new ArrayList<>();
        List<BankStatistics> tempList;
        Map<String, List<BankStatistics>> temp;
        List<BankStatistics> bankStatisticsList = new ArrayList<>();
        try {
            bankStatisticsList = hdReportBiz.getBankStatistics(
                    dateStart,dateEnd,null, TenantContextHolder.getTenantId());
            //重新封装一下如果有同名银行
            if (bankStatisticsList != null && bankStatisticsList.size() > 0) {
                last = bankStatisticsList.get(bankStatisticsList.size()-1);
                sumIncome = last.getCurrentIncome();
                sumPay = last.getCurrentPay();
                sumBegin = last.getBeginBalance();
                sumEnd = last.getEndBalance();
                sumPassedCredit = last.getPassedCredit();
                sumUsedCredit = last.getUsedCredit();
                bankStatisticsList.remove(bankStatisticsList.size()-1);
                total = bankStatisticsList.size();
                int pageNo = Integer.parseInt(page);
                int pageSize = Integer.parseInt(limit);
                int fromIndex = (pageNo-1) * pageSize;
                int toIndex = fromIndex + pageSize;
                if(toIndex > total){
                    toIndex = total;
                }
                if(fromIndex <= total) {
                    bankStatisticsList = bankStatisticsList.subList(fromIndex, toIndex);
                }else{
                    bankStatisticsList = new ArrayList<>();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return MR.failed("获取失败");
        }
        return new MR(total,bankStatisticsList,sumIncome,sumPay,sumBegin,sumEnd,sumPassedCredit,sumUsedCredit);
    }




    //导出银行报表
    @ApiOperation("导出银行报表*")
    @RequestMapping(value = "/exportBankXls", method = RequestMethod.GET)
    public void exportBankXls(String dateStart,String dateEnd,String userName) {
        List<BankStatistics> bankStatisticsList = hdReportBiz.getBankStatistics(
                dateStart,dateEnd,null,TenantContextHolder.getTenantId());
        BigDecimal lastNormal = BigDecimal.ZERO;
        BigDecimal lastSpecial = BigDecimal.ZERO;
        BigDecimal lastLc = BigDecimal.ZERO;
        BigDecimal lastBasic = BigDecimal.ZERO;
        BigDecimal lastLoan = BigDecimal.ZERO;
        BigDecimal lastTotal = BigDecimal.ZERO;
        //重新封装一下如果有同名银行
        List<BankStatistics> result = new ArrayList<>();
        List<BankStatistics> tempList;
        Map<String, List<BankStatistics>> temp;
        if (bankStatisticsList != null && bankStatisticsList.size() > 0) {
            //生成最后一条合计
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ExcelUtil excelUtil = new ExcelUtil<BankStatistics>();
            try {
                excelUtil.print("银行报表", BankStatistics.class, "银行报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "银行报表", bankStatisticsList, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //银行报表
    @ApiOperation("获取各个银行所有公司报表明细*")
    @RequestMapping(value = "/getBankComStatistics", method = RequestMethod.GET)
    public MR getBankComStatistics(String dateStart, String dateEnd){
        long total = 0;
        String tenantId = BaseContextHandler.getTenantID();
        BankComStatistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        List<BankComStatistics> bankComstatistics = new ArrayList<>();
        if (StringUtil.isEmpty(dateStart)) {
            return MR.failed("起始时间获取失败");
        }
        if (StringUtil.isEmpty(dateEnd)) {
            return MR.failed("截止时间获取失败");
        }
        try{
            bankComstatistics = hdReportBiz.getBankComStatistics(dateStart,
                    dateEnd);
            if(bankComstatistics!=null&&bankComstatistics.size()>0){
                last = bankComstatistics.get(bankComstatistics.size()-1);
                sumIncome = last.getCurrentIncome();
                sumPay = last.getCurrentPay();
                sumBegin = last.getBeginBalance();
                sumEnd = last.getEndBalance();
                bankComstatistics.remove(bankComstatistics.size()-1);
                total = bankComstatistics.size();
            }
        }catch (Exception e){
            e.printStackTrace();
            return MR.failed("获取失败");
        }
        return new MR(total, bankComstatistics, sumIncome, sumPay,sumBegin,sumEnd);
    }






    //获取八位最大值
    @ApiOperation("获取打印最大值*")
    @RequestMapping(value = "/getPrintMaxNo", method = RequestMethod.GET)
    public R<String> getPrintMaxNo(){
        JSONObject jb = FileUtil.readJsonFromClassPath(FileUtil.RESUME_TEMPLATE);
        if(jb==null){
            return R.failed("获取失败");
        }
        Integer maxNo = (Integer)jb.get("maxNo");
        Map<String,Integer> map = new HashedMap();
        map.put("maxNo",maxNo+1);
        Boolean result = FileUtil.writeJsonFromClassPath(FileUtil.RESUME_TEMPLATE,JSONObject.parseObject(JSONObject.toJSONString(map)));
        String maxNoStr = this.formatNo(maxNo+"");
        System.out.println(maxNoStr+"@@@@@@@@@最大值");
        if(result){
            return R.ok(maxNoStr,"获取成功");
        }else{
            return R.failed("获取失败");
        }
    }


    private String formatNo(String num){
        while(num.length()<7){
            num = "0"+num;
        }
        return num;
    }





    //余额对账单打印
    @ApiOperation("导出各个银行所有公司报表明细*")
    @RequestMapping(value = "/getComPrintList", method = RequestMethod.POST)
    public R<Object> getComPrintList(String ids,String dateStart,String dateEnd){
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        if(StringUtil.isEmpty(dateStart)){
            return R.failed("参数传递失败");
        }
        if(StringUtil.isEmpty(dateEnd)){
            return R.failed("参数传递失败");
        }
        List<Statistics> comPrintList = new ArrayList<>();
        List<String> idStrs = new ArrayList<>();
        for(String id:ids.split(",")){
            idStrs.add(id);
        }
        comPrintList = hdReportBiz.statics(dateStart, dateEnd, TenantContextHolder.getTenantId(),null,idStrs);
        if(comPrintList!=null&&comPrintList.size()>0){
            for(Statistics statistics:comPrintList){
                statistics.setEndTime(dateEnd);
                statistics.setUserName(BaseContextHandler.getName());
                System.out.println(statistics.getTenantId()+"抬头id");
                //Tenant tenant = iUserFeign.getTenantById(statistics.getTenantId()).getData();
                HdBankTenant hdBankTenant = hdBankTenantBiz.getBankNameByTenant(statistics.getTenantId());
                if(hdBankTenant!=null){
                    statistics.setHeadName(hdBankTenant.getTenantName());
                }
                System.out.println(statistics.getHeadName()+"获取抬头名称");
            }
        }
        return R.ok(comPrintList,"获取成功");
    }











    //公司明细报表
    @ApiOperation("新公司明细报表*")
    @RequestMapping(value = "/getComDetailList", method = RequestMethod.GET)
    @ResponseBody
    public R getComDetailList(
            String dateStart,
            String dateEnd,String page,
            String limit,String companyId
    ){
        int total = 0;
        long a = System.currentTimeMillis();
        List<ComDetailStatistics> result = new ArrayList<>();

        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        try{
            RMap<String, List<ComDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(BaseContextHandler.getTenantID()+companyId);
            if(pageInt>1){
                if(resultMap!=null){
                    result = resultMap.get(dateStart+dateEnd);
                    if(result==null){
                        result = hdReportBiz.getComDetailList(dateStart,dateEnd,companyId);
                        resultMap.put(dateStart+dateEnd,result);
                    }
                }else{
                    result = hdReportBiz.getComDetailList(dateStart,dateEnd,companyId);
                    resultMap.put(dateStart+dateEnd,result);
                }
            }else{
                result = hdReportBiz.getComDetailList(dateStart,dateEnd,companyId);
                resultMap.put(dateStart+dateEnd,result);
            }
            System.out.println("数量:"+result.size());
            total = result.size();
            int fromIndex = (pageInt-1) * limitInt;
            int toIndex = fromIndex + limitInt;
            if(toIndex > total){
                toIndex = total;
            }
            if(fromIndex <= total) {
                result = result.subList(fromIndex, toIndex);
            }else{
                result = new ArrayList<>();
            }
//            for(ComDetailStatistics detailStatistics:result){
//                //对方科目
//                String subject = detailStatistics.getSubject();
//                if(StringUtil.isNotEmpty(subject)){
//                    HdCompany hdCompany = bankAccountFeign.getComById(subject);
//                    if(hdCompany!=null){
//
//                        detailStatistics.setSubject(hdCompany.getCompanyName());
//                    }
//                }
//            }
            System.out.println(System.currentTimeMillis()-a);
        }catch (Exception e){
            return R.failed("获取失败");
        }
        IPage iPage = new Page();
        iPage.setRecords(result);
        iPage.setTotal(total);
        return R.ok(iPage);
    }


    @ApiOperation("新导出公司明细报表*")
    @RequestMapping(value = "/exportComDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportComDetailList(
            String dateStart,
            String dateEnd, String companyId
    ){
        List<ComDetailStatistics> result = new ArrayList<>();
        RMap<String, List<ComDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(BaseContextHandler.getTenantID()+companyId);
        if(resultMap!=null){
            result = resultMap.get(dateStart+dateEnd);
            if(result==null){
                result = hdReportBiz.getComDetailList(dateStart,dateEnd,companyId);
                resultMap.put(dateStart+dateEnd,result);
            }
        }else{
            result = hdReportBiz.getComDetailList(dateStart,dateEnd,companyId);
            resultMap.put(dateStart+dateEnd,result);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExcelUtil excelUtil = new ExcelUtil<ComDetailStatistics>();
        try {
            excelUtil.print("公司明细报表", ComDetailStatistics.class, "公司明细报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "公司明细报表", result, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @ApiOperation("新获取银行明细报表*")
    @RequestMapping(value = "/getBankDetailList", method = RequestMethod.GET)
    @ResponseBody
    public R getBankDetailList(
            String dateStart,
            String dateEnd,String page,
            String limit,String bankAccountId
    ){
        int total = 0;
        long a = System.currentTimeMillis();
        List<BankDetailStatistics> result = new ArrayList<>();

        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        try{
            RMap<String, List<BankDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(BaseContextHandler.getTenantID()+bankAccountId);
            if(pageInt>1){
                if(resultMap!=null){
                    result = resultMap.get(dateStart+dateEnd);
                    if(result==null){
                        result = hdReportBiz.getBankDetailList(dateStart,dateEnd,bankAccountId);
                        resultMap.put(dateStart+dateEnd,result);
                    }
                }else{
                    result = hdReportBiz.getBankDetailList(dateStart,dateEnd,bankAccountId);
                    resultMap.put(dateStart+dateEnd,result);
                }
            }else{
                result = hdReportBiz.getBankDetailList(dateStart,dateEnd,bankAccountId);
                resultMap.put(dateStart+dateEnd,result);
            }
            System.out.println("数量:"+result.size());
            total = result.size();
            int fromIndex = (pageInt-1) * limitInt;
            int toIndex = fromIndex + limitInt;
            if(toIndex > total){
                toIndex = total;
            }
            if(fromIndex <= total) {
                result = result.subList(fromIndex, toIndex);
            }else{
                result = new ArrayList<>();
            }
//            for(BankDetailStatistics detailStatistics:result){
//                //对方科目
//                String subject = detailStatistics.getSubject();
//                if(StringUtil.isNotEmpty(subject)){
//                    HdCompany hdCompany = bankAccountFeign.getComById(subject);
//                    if(hdCompany!=null){
//                        detailStatistics.setSubject(hdCompany.getCompanyName());
//                    }
//                }
//            }
            System.out.println(System.currentTimeMillis()-a);
        }catch (Exception e){
            return R.failed("获取失败");
        }
        IPage iPage = new Page();
        iPage.setRecords(result);
        iPage.setTotal(total);
        return R.ok(iPage);
    }

    //新银行明细导出
    @ApiOperation("新导出银行明细报表*")
    @RequestMapping(value = "/exportBankDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportBankDetailList(
            String dateStart,
            String dateEnd, String bankAccountId
    ){
        List<BankDetailStatistics> result = new ArrayList<>();
        RMap<String, List<BankDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(BaseContextHandler.getTenantID()+bankAccountId);
        if(resultMap!=null){
            result = resultMap.get(dateStart+dateEnd);
            if(result==null){
                result = hdReportBiz.getBankDetailList(dateStart,dateEnd,bankAccountId);
                resultMap.put(dateStart+dateEnd,result);
            }
        }else{
            result = hdReportBiz.getBankDetailList(dateStart,dateEnd,bankAccountId);
            resultMap.put(dateStart+dateEnd,result);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExcelUtil excelUtil = new ExcelUtil<BankDetailStatistics>();
        //遍历所有如果外部单位
//        for(BankDetailStatistics detailStatistics:result){
//            //对方科目
//            String subject = detailStatistics.getSubject();
//            if(StringUtil.isNotEmpty(subject)){
//                HdCompany hdCompany = bankAccountFeign.getComById(subject);
//                if(hdCompany!=null){
//                    detailStatistics.setSubject(hdCompany.getCompanyName());
//                }
//            }
//        }
        try {
            excelUtil.print("银行明细报表", BankDetailStatistics.class, "银行明细报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "银行明细报表", result, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








}
