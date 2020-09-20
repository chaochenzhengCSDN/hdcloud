package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.impl;

import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;

import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.Statistics;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.feign.RemoteReportService;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.service.HdReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019-12-07.
 */
@Service
public class HdReportSercieImpl implements HdReportService {
    @Autowired
    RemoteReportService remoteReportService;

    public List<Statistics> statics(String startTime, String endTime, Integer tenantId, String userId, List<String> ids) {
        //最后一笔收入和支出
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        Statistics last = new Statistics();
        //收入-支出+期初
        List<Statistics> statisticsList = remoteReportService.selectStatics(startTime,
                endTime, tenantId,userId,null);
        if (statisticsList != null && statisticsList.size() > 0) {
            BigDecimal endBalance = BigDecimal.ZERO;
            for (Statistics statistics : statisticsList) {
                BigDecimal beginBalance = statistics.getBeginBalance();
                BigDecimal income = statistics.getCurrentIncome();
                BigDecimal pay = statistics.getCurrentPay();
                sumIncome = sumIncome.add(income);
                sumPay = sumPay.add(pay);
                sumBegin = sumBegin.add(beginBalance);
                endBalance = income.subtract(pay).add(beginBalance);
                sumEnd = sumEnd.add(endBalance);
                statistics.setEndBalance(endBalance);
            }
            last.setBeginBalance(sumBegin);
            last.setCurrentIncome(sumIncome);
            last.setCurrentPay(sumPay);
            last.setEndBalance(sumEnd);
            last.setCompanyName("合计:");
            statisticsList.add(last);
        }
        return statisticsList;
    }

}
