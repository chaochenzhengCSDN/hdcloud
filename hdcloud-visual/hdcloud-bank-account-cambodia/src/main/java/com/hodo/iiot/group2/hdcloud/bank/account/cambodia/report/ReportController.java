package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.report;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.*;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service.HdReportService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.RedissonUtil;

import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.map.HashedMap;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hodo.iiot.group2.hdcloud.bank.account.cambodia.util.RedissonUtil.redissonUtil;

@RestController
@RequestMapping("hdReport")
@Api(value = "hdReport", tags = "報表管理")
public class ReportController {
    //为了不加tenant临时使用此biz

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private HdReportService hdReportService;

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    //银行明细
    @ApiOperation("新获取银行明细报表*")
    @RequestMapping(value = "/getBankDetailList", method = RequestMethod.GET)
    @ResponseBody
    public R getBankDetailList(
            String dateStart,
            String dateEnd,String page,
            String limit,String accountId
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
            RMap<String, List<BankDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(TenantContextHolder.getTenantId() +accountId);
            if(pageInt>1){
                if(resultMap!=null){
                    result = resultMap.get(dateStart+dateEnd);
                    if(result==null){
                        result = hdReportService.getBankDetailList(dateStart,dateEnd,accountId);
                        resultMap.put(dateStart+dateEnd,result);
                    }
                }else{
                    result = hdReportService.getBankDetailList(dateStart,dateEnd,accountId);
                    resultMap.put(dateStart+dateEnd,result);
                }
            }else{
                result = hdReportService.getBankDetailList(dateStart,dateEnd,accountId);
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
        Map<String,Object> resultR = new HashMap<>();
        resultR.put("records",result);
        resultR.put("total",total);
        return R.ok(resultR);
    }

    //银行明细导出
    @ApiOperation("新导出银行明细报表*")
    @RequestMapping(value = "/exportBankDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportBankDetailList(
            String dateStart,
            String dateEnd, String accountId
    ){
        List<BankDetailStatistics> result = new ArrayList<>();
        RMap<String, List<BankDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(TenantContextHolder.getTenantId()+accountId);
        if(resultMap!=null){
            result = resultMap.get(dateStart+dateEnd);
            if(result==null){
                result = hdReportService.getBankDetailList(dateStart,dateEnd,accountId);
                resultMap.put(dateStart+dateEnd,result);
            }
        }else{
            result = hdReportService.getBankDetailList(dateStart,dateEnd,accountId);
            resultMap.put(dateStart+dateEnd,result);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ExcelUtil excelUtil = new ExcelUtil<BankDetailStatistics>();
        try {
            excelUtil.print("银行明细报表", BankDetailStatistics.class, "银行明细报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "银行明细报表", result, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //公司明细
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
            RMap<String, List<ComDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(TenantContextHolder.getTenantId()+companyId);
            if(pageInt>1){
                if(resultMap!=null){
                    result = resultMap.get(dateStart+dateEnd);
                    if(result==null){
                        result = hdReportService.getComDetailList(dateStart,dateEnd,companyId);
                        resultMap.put(dateStart+dateEnd,result);
                    }
                }else{
                    result = hdReportService.getComDetailList(dateStart,dateEnd,companyId);
                    resultMap.put(dateStart+dateEnd,result);
                }
            }else{
                result = hdReportService.getComDetailList(dateStart,dateEnd,companyId);
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
        Map<String,Object> resultR = new HashedMap<>();
        resultR.put("records",result);
        resultR.put("total",total);
        return R.ok(resultR);
    }

    //公司明细导出
    @ApiOperation("新导出公司明细报表*")
    @RequestMapping(value = "/exportComDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportComDetailList(
            String dateStart,
            String dateEnd, String companyId
    ){
        List<ComDetailStatistics> result = new ArrayList<>();
        RMap<String, List<ComDetailStatistics>> resultMap =redissonUtil.getRedissonClient().getMap(TenantContextHolder.getTenantId()+companyId);
        if(resultMap!=null){
            result = resultMap.get(dateStart+dateEnd);
            if(result==null){
                result = hdReportService.getComDetailList(dateStart,dateEnd,companyId);
                resultMap.put(dateStart+dateEnd,result);
            }
        }else{
            result = hdReportService.getComDetailList(dateStart,dateEnd,companyId);
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

    //银行报表
    @ApiOperation("获取各个银行所有公司报表明细*")
    @RequestMapping(value = "/getBankComStatistics", method = RequestMethod.GET)
    public R getBankComStatistics(String dateStart, String dateEnd){
        long total = 0;
        Integer tenantId = TenantContextHolder.getTenantId();
        BankComStatistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        List<BankComStatistics> bankComstatistics = new ArrayList<>();
        if (StringUtil.isEmpty(dateStart)) {
            return R.failed("起始时间获取失败");
        }
        if (StringUtil.isEmpty(dateEnd)) {
            return R.failed("截止时间获取失败");
        }
        try{
            bankComstatistics = hdReportService.getBankComStatistics(dateStart,
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
            return R.failed("获取失败");
        }
        Map<String,Object> resultR = new HashedMap<>();
        resultR.put("records",bankComstatistics);
        resultR.put("total",total);
        resultR.put("sumIncome",sumIncome);
        resultR.put("sumPay",sumPay);
        resultR.put("sumBegin",sumBegin);
        resultR.put("sumEnd",sumEnd);
        return R.ok(resultR);
    }

    //
    //导出
    @ApiOperation("导出各个银行所有公司报表明细*")
    @RequestMapping(value = "/exportBankComStatistics", method = RequestMethod.GET)
    public void exportBankComStatistics(String dateStart, String dateEnd){
        long total = 0;
        Integer tenantId = TenantContextHolder.getTenantId();
        List<BankComStatistics> bankComstatistics = new ArrayList<>();
        try{
            bankComstatistics = hdReportService.getBankComStatistics(dateStart,
                    dateEnd);
            ExcelUtil excelUtil = new ExcelUtil<BankComStatistics>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            excelUtil.print("账户公司报表明细", BankComStatistics.class, "账户公司报表明细", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "账户公司报表明细", bankComstatistics, request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //公司报表
    //获取各个公司所有银行的
    @ApiOperation("获取各个公司所有银行报表明细*")
    @RequestMapping(value = "/getComBankStatistics", method = RequestMethod.GET)
    public R getComBankStatistics(String dateStart, String dateEnd){
        long total = 0;
        Integer tenantId = TenantContextHolder.getTenantId();
        ComBankStatistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        List<ComBankStatistics> comBankstatistics = new ArrayList<>();
        if (StringUtil.isEmpty(dateStart)) {
            return R.failed("起始时间获取失败");
        }
        if (StringUtil.isEmpty(dateEnd)) {
            return R.failed("截止时间获取失败");
        }
        try{
            comBankstatistics = hdReportService.getComBankStatistics(dateStart,
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
            return R.failed("获取失败");
        }
        Map<String,Object> resultR = new HashedMap<>();
        resultR.put("records",comBankstatistics);
        resultR.put("total",total);
        resultR.put("sumIncome",sumIncome);
        resultR.put("sumPay",sumPay);
        resultR.put("sumBegin",sumBegin);
        resultR.put("sumEnd",sumEnd);
        return R.ok(resultR);
    }


    //导出
    @ApiOperation("导出各个公司所有银行报表明细*")
    @RequestMapping(value = "/exportComBankStatistics", method = RequestMethod.GET)
    public void exportComBankStatistics(String dateStart, String dateEnd){
        long total = 0;
        Integer tenantId = TenantContextHolder.getTenantId();
        List<ComBankStatistics> comBankstatistics = new ArrayList<>();
        try{
            comBankstatistics = hdReportService.getComBankStatistics(dateStart,
                    dateEnd);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ExcelUtil excelUtil = new ExcelUtil<ComBankStatistics>();
            excelUtil.print("公司账户报表明细", ComBankStatistics.class, "公司账户报表明细", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "公司账户报表明细", comBankstatistics, request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ApiOperation("获取银行报表*")
    @RequestMapping(value = "/getBankStatistcs", method = RequestMethod.GET)
    @ResponseBody
    public R getBankStatistcs(String dateStart,
                                                                                              String dateEnd,String page,String limit) {
        int total = 0;
        BankStatistics last;
        Map<String,Object> resultR = new HashedMap<>();
        if (StringUtil.isEmpty(dateStart)) {
            resultR.put("records",new ArrayList<>());
            resultR.put("sumIncome",0);
            resultR.put("sumPay",0);
            resultR.put("total",total);
            return R.ok(resultR);
        }
        if (StringUtil.isEmpty(dateEnd)) {
            resultR.put("records",new ArrayList<>());
            resultR.put("sumIncome",0);
            resultR.put("sumPay",0);
            resultR.put("total",total);
            return R.ok(resultR);
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
            bankStatisticsList = hdReportService.getBankStatistics(
                    dateStart,dateEnd,null,TenantContextHolder.getTenantId()
            );
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
            return R.failed("获取失败");
        }
        resultR.put("records",new ArrayList<>());
        resultR.put("sumIncome",0);
        resultR.put("sumPay",0);
        resultR.put("total",total);
        return R.ok(resultR);
    }

    //导出银行报表
    @ApiOperation("导出银行报表*")
    @RequestMapping(value = "/exportBankXls", method = RequestMethod.GET)
    public void exportBankXls(String dateStart,String dateEnd,String userName) {
        List<BankStatistics> bankStatisticsList = hdReportService.getBankStatistics(
                dateStart,dateEnd,null,TenantContextHolder.getTenantId()
        );
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

    @ApiOperation("获取公司报表*")
    @RequestMapping(value = "/getComStatisticsList", method = RequestMethod.GET)
    @ResponseBody
    public R getComStatisticsList(String dateStart,
                                                                                              String dateEnd) {
        long total = 0;
        Integer tenantId = TenantContextHolder.getTenantId();
        Statistics last;
        Map<String,Object> resultR = new HashedMap<>();
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        List<Statistics> statisticsList = new ArrayList<>();
        try{
            if (StringUtil.isEmpty(dateStart)) {
                resultR.put("records",new ArrayList<>());
                resultR.put("sumIncome",0);
                resultR.put("sumPay",0);
                resultR.put("total",total);
                return R.ok(resultR);
            }
            if (StringUtil.isEmpty(dateEnd)) {
                resultR.put("records",new ArrayList<>());
                resultR.put("sumIncome",0);
                resultR.put("sumPay",0);
                resultR.put("total",total);
                return R.ok(resultR);
            }
            statisticsList = hdReportService.statics(dateStart, dateEnd, tenantId,null,null);
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
            return R.failed("获取失败");
        }
        resultR.put("total",total);
        resultR.put("records",statisticsList);
        resultR.put("sumIncome",sumIncome);
        resultR.put("sumPay",sumPay);
        resultR.put("sumBegin",sumBegin);
        resultR.put("sumEnd",sumEnd);
        return R.ok(resultR);
    }

    @ApiOperation("导出公司报表*")
    @RequestMapping(value = "/exportStatisticsXls", method = RequestMethod.GET)
    public void exportStatisticsXls(String dateStart,
                                    String dateEnd,String userName){
        Integer tenantId = TenantContextHolder.getTenantId();
        Statistics last;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        List<Statistics> statisticsList = hdReportService.statics(dateStart, dateEnd, tenantId,null,null);
        ExcelUtil excelUtil = new ExcelUtil<Statistics>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            excelUtil.print("公司账单报表", Statistics.class, "公司账单报表", "导出时间:"+sdf.format(new Date()) +" 查询时间:"+dateStart+"至"+dateEnd, "公司账单报表", statisticsList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
