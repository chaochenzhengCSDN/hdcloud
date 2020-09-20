package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.report;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.Statistics;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.HdReportService;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.RedissonUtil;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
