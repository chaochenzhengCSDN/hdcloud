package com.hodo.iiot.group2.hdcloud.bank.account.currency.report;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.HdReportEntity;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyBankDetailStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyBankStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyComDetailStatistics;

import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdReportService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.ExcelUtil;

import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.RedissonUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.StringUtil;
import com.sun.prism.impl.BaseContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("TyhdReport")
//@CheckClientToken
//@CheckUserToken
@Api(value = "TyhdReport", tags = "報表管理")
public class TyReportController {
    //为了不加tenant临时使用此biz

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private HdReportService hdReportService;

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    @ApiOperation(value = "*获取统计报表")
    @RequestMapping(value = "/getBankReport", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportType", value = "0:银行总计，1:公司总计，2:成员单位总计, 3:银行单位总计, 4:银行公司总计", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "多少条", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", paramType = "query"),
            @ApiImplicitParam(name = "bankAccountId", value = "银行名", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "公司名", paramType = "query"),
            @ApiImplicitParam(name = "memberCompanyId", value = "成员单位名", paramType = "query"),
            @ApiImplicitParam(name = "dateStart", value = "开始时间（必填）", paramType = "query"),
            @ApiImplicitParam(name = "dateEnd", value = "结束时间（必填）", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "0:不去除空数据，1:去除空数据", paramType = "query"),
    })
    public R<Object> getCountReport(@RequestParam("reportType") String reportType,
                                    @RequestParam("dateStart") String dateStart, @RequestParam("dateEnd") String dateEnd,
                                    @RequestParam("bankAccountId") String bankAccountId, @RequestParam("companyId") String companyId,
                                    @RequestParam("memberCompanyId") String memberCompanyId, @RequestParam("page") String page,
                                    @RequestParam("limit") String limit, @RequestParam("flag") String flag){
        //获取总数
        int total = 0;
        List<TyBankStatistics> bankStatisticsList = new ArrayList<>();
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal sumNzIncome = BigDecimal.ZERO;
        BigDecimal sumNzPay = BigDecimal.ZERO;
        //用于计算总和
        TyBankStatistics last;
        String timeFlag = dateStart.replace("-","") + dateEnd.replace("-","");
        //自定义返回值类型
        if (StringUtil.isEmpty(dateStart)) {
            //return R.failed(total, new ArrayList<TyBankStatistics>(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            return R.ok(new ArrayList<>());
        }
        if (StringUtil.isEmpty(dateEnd)) {
            return R.ok(new ArrayList<>());
        }
        if (StringUtil.isEmpty(page)) { page = "1"; }
        if (StringUtil.isEmpty(limit)) { limit = "10"; }

        try {
        switch (reportType){
            case "0" :
                //银行总计
                bankStatisticsList = hdReportService.getDataByBank(dateStart,dateEnd,bankAccountId,flag);
                break;
            case "1" :
                //公司总计
                bankStatisticsList = hdReportService.getDataByCompany(dateStart,dateEnd,companyId,flag);
                break;
            case "2" :
                //成员单位总计
                bankStatisticsList = hdReportService.getDataByMemberUnit(dateStart,dateEnd,memberCompanyId,companyId,flag);
                break;
            case "3" :
                //银行单位总计
                bankStatisticsList = hdReportService.getDataByBankUnit(dateStart,dateEnd,bankAccountId,companyId,memberCompanyId,flag);
                break;
            case "4" :
                //银行公司总计
                bankStatisticsList = hdReportService.getDataByBankCompany(dateStart,dateEnd,bankAccountId,companyId,flag);
                break;
            default : //可选
                //不操作
        }

        if (bankStatisticsList != null && bankStatisticsList.size() > 0) {
            last = bankStatisticsList.get(bankStatisticsList.size()-1);
            sumIncome = last.getCurrentIncome();
            sumPay = last.getCurrentPay();
            sumBegin = last.getBeginBalance();
            sumEnd = last.getEndBalance();
            sumNzIncome = last.getNzIncome();
            sumNzPay = last.getNzPay();
            bankStatisticsList.remove(bankStatisticsList.size()-1);
            total = bankStatisticsList.size();
            TyBankStatistics sum = new TyBankStatistics();
            sum.setBankCode("合计");
            sum.setBeginBalance(sumBegin);
            sum.setCurrentIncome(sumIncome);
            sum.setCurrentPay(sumPay);
            sum.setEndBalance(sumEnd);
            sum.setNzIncome(sumNzIncome);
            sum.setNzPay(sumNzPay);
            bankStatisticsList.add(sum);

//            resultMap.put(timeFlag,bankStatisticsList);

            int pageNo = Integer.parseInt(page);
            int pageSize = Integer.parseInt(limit);
            pageNo = (pageNo - 1) * pageSize;
            if (pageNo + pageSize > total) {
                if(pageNo>total){
                    return R.failed("传递页数错误，查询时请先选择第一页");
                }
                bankStatisticsList = bankStatisticsList.subList(pageNo, total);
            } else {
                bankStatisticsList = bankStatisticsList.subList(pageNo, pageNo + pageSize);
            }


         }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("获取失败");
        }
        HdReportEntity<Object> result = new HdReportEntity<>();
        result.setData(bankStatisticsList);
        result.setSumBegin(sumBegin);
        result.setSumEnd(sumEnd);
        result.setSumIncome(sumIncome);
        result.setSumPay(sumPay);
        result.setSumNzIncome(sumNzIncome);
        result.setSumNzPay(sumNzPay);
        //封装返回值
        return R.ok(result);
    }

    @ApiOperation(value = "*导出统计报表")
    @RequestMapping(value = "/getReportExcel", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportType", value = "0:导出银行总计，1:导出公司总计，2:导出成员单位总计, 3:导出银行单位总计, 4:导出银行公司总计", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "多少条", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", paramType = "query"),
            @ApiImplicitParam(name = "bankAccountId", value = "银行名", paramType = "query"),
            @ApiImplicitParam(name = "companyId", value = "公司名", paramType = "query"),
            @ApiImplicitParam(name = "memberCompanyId", value = "成员单位名", paramType = "query"),
            @ApiImplicitParam(name = "dateStart", value = "开始时间（必填）", paramType = "query"),
            @ApiImplicitParam(name = "dateEnd", value = "结束时间（必填）", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "0:不去除空数据，1:去除空数据", paramType = "query"),
    })
    public void getReportExcel(@RequestParam("reportType") String reportType,@RequestParam("dateStart") String dateStart,
                               @RequestParam("dateEnd") String dateEnd,@RequestParam("bankAccountId") String bankAccountId,
                               @RequestParam("companyId") String companyId, @RequestParam("memberCompanyId") String memberCompanyId,
                               @RequestParam("page") String page,@RequestParam("limit") String limit,
                               @RequestParam("flag") String flag) {

        //获取总数
        int total = 0;
        //用于计算总和
        TyBankStatistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal sumNzIncome = BigDecimal.ZERO;
        BigDecimal sumNzPay = BigDecimal.ZERO;
        List<TyBankStatistics> bankStatisticsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExcelUtil excelUtil = new ExcelUtil<TyBankStatistics>();
        String timeFlag = dateStart.replace("-", "") + dateEnd.replace("-", "");
//        RMap<String, List<TyBankStatistics>> resultMap = null;
//        RedissonClient redissonClient = redissonUtil.getRedissonClient();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        try {
            switch (reportType) {
                case "0":
                    //银行总计
                    bankStatisticsList = hdReportService.getDataByBank(dateStart, dateEnd, bankAccountId,flag);
                    break;
                case "1":
                    //公司总计
                    bankStatisticsList = hdReportService.getDataByCompany(dateStart, dateEnd, companyId,flag);
                    break;
                case "2":
                    //成员单位总计
                    bankStatisticsList = hdReportService.getDataByMemberUnit(dateStart, dateEnd, memberCompanyId, companyId,flag);
                    break;
                case "3":
                    //银行单位总计
                    bankStatisticsList = hdReportService.getDataByBankUnit(dateStart, dateEnd, bankAccountId, companyId, memberCompanyId,flag);
                    break;
                case "4":
                    //银行公司总计
                    bankStatisticsList = hdReportService.getDataByBankCompany(dateStart, dateEnd, bankAccountId, companyId,flag);
                    break;
                default: //可选
                    //不操作
            }
            if (bankStatisticsList != null && bankStatisticsList.size() > 0) {
                last = bankStatisticsList.get(bankStatisticsList.size()-1);
                sumIncome = last.getCurrentIncome();
                sumPay = last.getCurrentPay();
                sumBegin = last.getBeginBalance();
                sumEnd = last.getEndBalance();
                sumNzIncome = last.getNzIncome();
                sumNzPay = last.getNzPay();
                bankStatisticsList.remove(bankStatisticsList.size()-1);
                total = bankStatisticsList.size();
                TyBankStatistics sum = new TyBankStatistics();
                sum.setBankCode("合计");
                sum.setBeginBalance(sumBegin);
                sum.setCurrentIncome(sumIncome);
                sum.setCurrentPay(sumPay);
                sum.setEndBalance(sumEnd);
                sum.setNzIncome(sumNzIncome);
                sum.setNzPay(sumNzPay);
                bankStatisticsList.add(sum);
            }
            if ("0".equals(reportType)) {
                excelUtil.print("银行报表", TyBankStatistics.class, "银行报表", "导出时间:" + sdf.format(new Date()) + " 查询时间:" + dateStart + "至" + dateEnd, "银行报表", bankStatisticsList, request, response);
            }
            if ("1".equals(reportType)) {
                excelUtil.print("公司报表", TyBankStatistics.class, "公司报表", "导出时间:" + sdf.format(new Date()) + " 查询时间:" + dateStart + "至" + dateEnd, "公司报表", bankStatisticsList, request, response);
            }
            if ("2".equals(reportType)) {
                excelUtil.print("成员单位报表", TyBankStatistics.class, "成员单位报表", "导出时间:" + sdf.format(new Date()) + " 查询时间:" + dateStart + "至" + dateEnd, "成员单位报表", bankStatisticsList, request, response);
            }
            if ("3".equals(reportType)) {
                excelUtil.print("银行单位报表", TyBankStatistics.class, "银行单位报表", "导出时间:" + sdf.format(new Date()) + " 查询时间:" + dateStart + "至" + dateEnd, "银行单位报表", bankStatisticsList, request, response);
            }
            if ("4".equals(reportType)) {
                excelUtil.print("银行公司报表", TyBankStatistics.class, "银行公司报表", "导出时间:" + sdf.format(new Date()) + " 查询时间:" + dateStart + "至" + dateEnd, "银行公司报表", bankStatisticsList, request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------新建优化---------------------------------------//
    @ApiOperation("新获取银行明细报表*")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "多少条", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", paramType = "query"),
            @ApiImplicitParam(name = "bankAccountId", value = "银行Id", paramType = "query"),
            @ApiImplicitParam(name = "dateStart", value = "开始时间（必填）", paramType = "query"),
            @ApiImplicitParam(name = "dateEnd", value = "结束时间（必填）", paramType = "query")
    })
    @RequestMapping(value = "/getBankDetailList", method = RequestMethod.GET)
    @ResponseBody
    public R getBankDetailList(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,@RequestParam("page") String page,
            @RequestParam("limit") String limit,@RequestParam("bankAccountId") String bankAccountId
    ){
        if(StringUtil.isEmpty(dateStart)){
            return R.ok(new ArrayList<>());
        }
        if(StringUtil.isEmpty(dateEnd)){
            return R.ok(new ArrayList<>());
        }
        if(StringUtil.isEmpty(bankAccountId)){
            return R.ok(new ArrayList<>());
        }
        int total = 0;
        long a = System.currentTimeMillis();
        List<TyBankDetailStatistics> result = new ArrayList<>();

        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        try{
            //获取租户信息
            Integer tenantId = TenantContextHolder.getTenantId();
            RMap<String, List<TyBankDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(tenantId+bankAccountId);
            if(pageInt>1){
                if(resultMap!=null){
                    result = resultMap.get(dateStart+dateEnd);
                    if(result==null){
                        result = hdReportService.getBankDetailList(dateStart,dateEnd,bankAccountId);
                        resultMap.put(dateStart+dateEnd,result);
                    }
                }else{
                    result = hdReportService.getBankDetailList(dateStart,dateEnd,bankAccountId);
                    resultMap.put(dateStart+dateEnd,result);
                }
            }else{
                result = hdReportService.getBankDetailList(dateStart,dateEnd,bankAccountId);
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
            System.out.println(System.currentTimeMillis()-a);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("获取失败");
        }
        HdReportEntity<Object> resultR = new HdReportEntity<>();
        resultR.setTotal(total);
        resultR.setData(result);
        return R.ok(resultR);

    }


    //新银行明细导出
    @ApiOperation("新导出银行明细报表*")
    @RequestMapping(value = "/exportBankDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportBankDetailList(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd, @RequestParam("bankAccountId") String bankAccountId
    ){
        Integer tenantId = TenantContextHolder.getTenantId();
        List<TyBankDetailStatistics> result = new ArrayList<>();
        RMap<String, List<TyBankDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(tenantId+bankAccountId);
        if(resultMap!=null){
            result = resultMap.get(dateStart+dateEnd);
            if(result==null){
                result = hdReportService.getBankDetailList(dateStart,dateEnd,bankAccountId);
                resultMap.put(dateStart+dateEnd,result);
            }
        }else{
            result = hdReportService.getBankDetailList(dateStart,dateEnd,bankAccountId);
            resultMap.put(dateStart+dateEnd,result);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExcelUtil excelUtil = new ExcelUtil<TyBankDetailStatistics>();
        try {
            excelUtil.print("银行明细报表", TyBankDetailStatistics.class, "银行明细报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "银行明细报表", result, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //公司明细报表
    @ApiOperation("新公司明细报表*")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "多少条", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", paramType = "query"),
            @ApiImplicitParam(name = "memberCompanyId", value = "公司Id", paramType = "query"),
            @ApiImplicitParam(name = "dateStart", value = "开始时间（必填）", paramType = "query"),
            @ApiImplicitParam(name = "dateEnd", value = "结束时间（必填）", paramType = "query")
    })
    @RequestMapping(value = "/getComDetailList", method = RequestMethod.GET)
    @ResponseBody
    public R getComDetailList(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd,@RequestParam("page") String page,
            @RequestParam("limit") String limit,@RequestParam("memberCompanyId") String memberCompanyId
    ){
        if(StringUtil.isEmpty(dateStart)){
            return R.ok(new ArrayList<>());
        }
        if(StringUtil.isEmpty(dateEnd)){
            return R.ok(new ArrayList<>());
        }
        if(StringUtil.isEmpty(memberCompanyId)){
            return R.ok(new ArrayList<>());
        }
        int total = 0;
        long a = System.currentTimeMillis();
        List<TyComDetailStatistics> result = new ArrayList<>();
        Integer tenantId = TenantContextHolder.getTenantId();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        try{
            RMap<String, List<TyComDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(tenantId+memberCompanyId);
            if(pageInt>1){
                if(resultMap!=null){
                    result = resultMap.get(dateStart+dateEnd);
                    if(result==null){
                        result = hdReportService.getComDetailList(dateStart,dateEnd,memberCompanyId);
                        resultMap.put(dateStart+dateEnd,result);
                    }
                }else{
                    result = hdReportService.getComDetailList(dateStart,dateEnd,memberCompanyId);
                    resultMap.put(dateStart+dateEnd,result);
                }
            }else{
                result = hdReportService.getComDetailList(dateStart,dateEnd,memberCompanyId);
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
            System.out.println(System.currentTimeMillis()-a);
        }catch (Exception e){
            return R.failed("获取失败");
        }
        HdReportEntity<Object> resultR = new HdReportEntity<>();
        resultR.setTotal(total);
        resultR.setData(result);
        return R.ok(resultR);
    }



    @ApiOperation("新导出公司明细报表*")
    @RequestMapping(value = "/exportComDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportComDetailList(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd, @RequestParam("memberCompanyId") String memberCompanyId
    ){
        List<TyComDetailStatistics> result = new ArrayList<>();
        RMap<String, List<TyComDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(TenantContextHolder.getTenantId()+memberCompanyId);
        if(resultMap!=null){
            result = resultMap.get(dateStart+dateEnd);
            if(result==null){
                result = hdReportService.getComDetailList(dateStart,dateEnd,memberCompanyId);
                resultMap.put(dateStart+dateEnd,result);
            }
        }else{
            result = hdReportService.getComDetailList(dateStart,dateEnd,memberCompanyId);
            resultMap.put(dateStart+dateEnd,result);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExcelUtil excelUtil = new ExcelUtil<TyComDetailStatistics>();
        try {
            excelUtil.print("公司明细报表", TyComDetailStatistics.class, "公司明细报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "公司明细报表", result, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
