package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.util.StringUtil;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdReportFeign;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author Mr.AG
 * @email 463540703@qq.com
 * @version 2019-06-22 15:22:40
 */
@Service
public class HdReportBiz {

    @Autowired
    HdReportFeign hdReportFeign;


    public List<Statistics> statics(String startTime, String endTime, Integer tenantId, String userId) {
        //最后一笔收入和支出
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        Statistics last = new Statistics();
        //收入-支出+期初
        List<Statistics> statisticsList = hdReportFeign.selectStatics(startTime, endTime,tenantId,null,null);
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



    public List<Statistics> statics(String startTime, String endTime, Integer tenantId, String userId, List<String> ids) {
        List<Statistics> statisticsList = hdReportFeign.selectStatics(startTime,
                endTime, tenantId,userId,ids);
        if(statisticsList!=null&&statisticsList.size()>0){
            BigDecimal endBalance = BigDecimal.ZERO;
            for(Statistics statistics:statisticsList){
                BigDecimal beginBalance = statistics.getBeginBalance();
                BigDecimal income = statistics.getCurrentIncome();
                BigDecimal pay = statistics.getCurrentPay();

                endBalance = income.subtract(pay).add(beginBalance);
                statistics.setEndBalance(endBalance);
            }
        }
        return statisticsList;
    }




    public List<ComBankStatistics> getComBankStatistics(String dateStart, String dateEnd){
        List<ComBankStatistics> comBankStatisticsList = hdReportFeign.getComBankStatistics(dateStart,dateEnd, TenantContextHolder.getTenantId());
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal endBalance = BigDecimal.ZERO;
        ComBankStatistics last = new ComBankStatistics();
        //计算期末和合计
        if(comBankStatisticsList!=null&&comBankStatisticsList.size()>0){
            for(ComBankStatistics comBankStatistics:comBankStatisticsList){
                BigDecimal beginBalance = comBankStatistics.getBeginBalance();
                BigDecimal income = comBankStatistics.getCurrentIncome();
                BigDecimal pay = comBankStatistics.getCurrentPay();
                sumIncome = sumIncome.add(income);
                sumPay = sumPay.add(pay);
                sumBegin = sumBegin.add(beginBalance);
                endBalance = income.subtract(pay).add(beginBalance);
                sumEnd = sumEnd.add(endBalance);
                comBankStatistics.setEndBalance(endBalance);
            }
            last.setBeginBalance(sumBegin);
            last.setCurrentIncome(sumIncome);
            last.setCurrentPay(sumPay);
            last.setEndBalance(sumEnd);
            last.setCompanyCode("合计:");
            comBankStatisticsList.add(last);
        }
        return comBankStatisticsList;
    }



    //获取银行报表
    public List<BankStatistics> getBankStatistics(String dateStart, String dateEnd, String userId,
                                                  Integer tenantId){
        List<BankStatistics> bankStatisticsList = hdReportFeign.getBankStatisticsList(dateStart,dateEnd,userId,tenantId);
        BankStatistics last = new BankStatistics();
        BigDecimal sumPassedCredit = BigDecimal.ZERO;
        BigDecimal sumUsedCredit = BigDecimal.ZERO;
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal endBalance = BigDecimal.ZERO;
        //计算期末和合计
        if(bankStatisticsList!=null&&bankStatisticsList.size()>0){
            for(BankStatistics bankStatistics:bankStatisticsList){
                BigDecimal beginBalance = bankStatistics.getBeginBalance();
                BigDecimal income = bankStatistics.getCurrentIncome();
                BigDecimal pay = bankStatistics.getCurrentPay();
                //授信金额因为是字符串先判空
                if(StringUtil.isNotEmpty(bankStatistics.getPassedCredit())){
                    sumPassedCredit = sumPassedCredit.add(bankStatistics.getPassedCredit());
                }
                if(StringUtil.isNotEmpty(bankStatistics.getUsedCredit())){
                    sumUsedCredit = sumUsedCredit.add(bankStatistics.getUsedCredit());
                }
                sumIncome = sumIncome.add(income);
                sumPay = sumPay.add(pay);
                sumBegin = sumBegin.add(beginBalance);
                endBalance = income.subtract(pay).add(beginBalance);
                sumEnd = sumEnd.add(endBalance);
                bankStatistics.setEndBalance(endBalance);
            }
            last.setBeginBalance(sumBegin);
            last.setCurrentIncome(sumIncome);
            last.setCurrentPay(sumPay);
            last.setEndBalance(sumEnd);
            last.setPassedCredit(sumPassedCredit);
            last.setUsedCredit(sumUsedCredit);
            last.setBankCode("合计:");
            bankStatisticsList.add(last);
        }
        return bankStatisticsList;

    }




    public List<BankComStatistics> getBankComStatistics(String dateStart, String dateEnd){
        List<BankComStatistics> bankComStatisticsList = hdReportFeign.getBankComStatistics(dateStart,dateEnd,TenantContextHolder.getTenantId());
        BankComStatistics last = new BankComStatistics();
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal endBalance = BigDecimal.ZERO;
        //计算期末和合计
        if(bankComStatisticsList!=null&&bankComStatisticsList.size()>0){
            for(BankComStatistics bankComStatistics:bankComStatisticsList){
                BigDecimal beginBalance = bankComStatistics.getBeginBalance();
                BigDecimal income = bankComStatistics.getCurrentIncome();
                BigDecimal pay = bankComStatistics.getCurrentPay();
                sumIncome = sumIncome.add(income);
                sumPay = sumPay.add(pay);
                sumBegin = sumBegin.add(beginBalance);
                endBalance = income.subtract(pay).add(beginBalance);
                sumEnd = sumEnd.add(endBalance);
                bankComStatistics.setEndBalance(endBalance);
            }
            last.setBeginBalance(sumBegin);
            last.setCurrentIncome(sumIncome);
            last.setCurrentPay(sumPay);
            last.setEndBalance(sumEnd);
            last.setBankCode("合计:");
            bankComStatisticsList.add(last);
        }
        return bankComStatisticsList;

    }




    public List<ComDetailStatistics> getComDetailList(
            String startTime,
            String endTime,String companyId
    ){
        //查询账单明细
        List<ComDetailStatistics> comDetailList = hdReportFeign.getComDetailList(startTime,endTime,TenantContextHolder.getTenantId(), companyId);
        List<ComDetailStatistics> result = new ArrayList<>();
        //加入期初
        String balance = hdReportFeign.getComReportBalance(startTime,TenantContextHolder.getTenantId(),companyId);

        BigDecimal beginBalance = BigDecimal.ZERO;
        if(StringUtil.isNotEmpty(balance)){
            beginBalance = new BigDecimal(balance);
        }
        String tempDate = startTime;
        ComDetailStatistics comDetailStatisticsDay = null;
        ComDetailStatistics comDetailStatisticsMonth = null;
        ComDetailStatistics comDetailStatisticsYear = null;
        BigDecimal totalIncomeDay = BigDecimal.ZERO;
        BigDecimal totalPayDay = BigDecimal.ZERO;
        BigDecimal totalIncomeMonth = BigDecimal.ZERO;
        BigDecimal totalPayMonth = BigDecimal.ZERO;
        BigDecimal totalIncomeYear = BigDecimal.ZERO;
        BigDecimal totalPayYear = BigDecimal.ZERO;
        if(comDetailList!=null&&comDetailList.size()>0){
            //加入期初
            addComBeginBalance(result,beginBalance,startTime);
            for(ComDetailStatistics comDetailStatistics:comDetailList){
                //计算期末
                beginBalance = beginBalance.add(comDetailStatistics.getIncome()).subtract(
                        comDetailStatistics.getPay()
                );
                comDetailStatistics.setBalance(beginBalance);
                result.add(comDetailStatistics);

                //计算本日合计
                if(tempDate.substring(0,10).equals(comDetailStatistics.getAccountDate().substring(0,10))){
                    totalIncomeDay = totalIncomeDay.add(comDetailStatistics.getIncome());
                    totalPayDay = totalPayDay.add(comDetailStatistics.getPay());
                }else{
                    if(totalIncomeDay.compareTo(BigDecimal.ZERO)==0&&
                            totalPayDay.compareTo(BigDecimal.ZERO)==0){
                        totalIncomeDay = totalIncomeDay.add(comDetailStatistics.getIncome());
                        totalPayDay = totalPayDay.add(comDetailStatistics.getPay());
                    }else{
                        //新增本日合计同时
                        comDetailStatisticsDay = new ComDetailStatistics();
                        comDetailStatisticsDay.setAccountDate(tempDate);
                        comDetailStatisticsDay.setIncome(totalIncomeDay);
                        comDetailStatisticsDay.setPay(totalPayDay);
                        comDetailStatisticsDay.setRemark("本日合计");
                        if(result.size()>1){
                            comDetailStatisticsDay.setBalance(result.get(result.size()-2).getBalance());
                        }else{
                            comDetailStatisticsDay.setBalance(result.get(0).getBalance());
                        }

                        result.add(result.size()-1,comDetailStatisticsDay);
                        totalIncomeDay = comDetailStatistics.getIncome();
                        totalPayDay = comDetailStatistics.getPay();
                    }
                }

                //计算本月合计
                if(tempDate.substring(0,7).equals(comDetailStatistics.getAccountDate().substring(0,7))){
                    totalIncomeMonth = totalIncomeMonth.add(comDetailStatistics.getIncome());
                    totalPayMonth = totalPayMonth.add(comDetailStatistics.getPay());
                }else{
                    if(totalIncomeMonth.compareTo(BigDecimal.ZERO)==0&&
                            totalPayMonth.compareTo(BigDecimal.ZERO)==0){
                        totalIncomeMonth = totalIncomeMonth.add(comDetailStatistics.getIncome());
                        totalPayMonth = totalPayMonth.add(comDetailStatistics.getPay());
                    }else{
                        //新增本日合计同时
                        comDetailStatisticsMonth = new ComDetailStatistics();
                        comDetailStatisticsMonth.setRemark("本月合计");
                        comDetailStatisticsMonth.setAccountDate(tempDate.substring(0,7));
                        comDetailStatisticsMonth.setIncome(totalIncomeMonth);
                        comDetailStatisticsMonth.setPay(totalPayMonth);
                        if(result.size()>1){
                            comDetailStatisticsMonth.setBalance(result.get(result.size()-2).getBalance());
                        }else{
                            comDetailStatisticsMonth.setBalance(result.get(0).getBalance());
                        }
                        result.add(result.size()-1,comDetailStatisticsMonth);
                        totalIncomeMonth = comDetailStatistics.getIncome();
                        totalPayMonth = comDetailStatistics.getPay();
                    }
                }
                //计算本年合计
                if(tempDate.substring(0,4).equals(comDetailStatistics.getAccountDate().substring(0,4))){
                    totalIncomeYear = totalIncomeYear.add(comDetailStatistics.getIncome());
                    totalPayYear = totalPayYear.add(comDetailStatistics.getPay());
                }else{
                    if(totalIncomeYear.compareTo(BigDecimal.ZERO)==0&&
                            totalPayYear.compareTo(BigDecimal.ZERO)==0){
                        totalIncomeYear = totalIncomeYear.add(comDetailStatistics.getIncome());
                        totalPayYear = totalPayYear.add(comDetailStatistics.getPay());
                    }else{
                        //新增本年合计同时
                        comDetailStatisticsYear = new ComDetailStatistics();
                        comDetailStatisticsYear.setRemark("本年累计");
                        comDetailStatisticsYear.setAccountDate(tempDate.substring(0,4));
                        comDetailStatisticsYear.setIncome(totalIncomeMonth);
                        comDetailStatisticsYear.setPay(totalPayMonth);
                        if(result.size()>1){
                            comDetailStatisticsYear.setBalance(result.get(result.size()-2).getBalance());
                        }else{
                            comDetailStatisticsYear.setBalance(result.get(0).getBalance());
                        }

                        result.add(result.size()-1,comDetailStatisticsYear);
                        totalIncomeYear = comDetailStatistics.getIncome();
                        totalPayYear = comDetailStatistics.getPay();
                    }
                }
                tempDate = comDetailStatistics.getAccountDate();
            }
            comDetailStatisticsDay = new ComDetailStatistics();
            comDetailStatisticsDay.setAccountDate(tempDate);
            comDetailStatisticsDay.setIncome(totalIncomeDay);
            comDetailStatisticsDay.setPay(totalPayDay);
            comDetailStatisticsDay.setBalance(result.get(result.size()-1).getBalance());
            comDetailStatisticsDay.setRemark("本日合计");
            result.add(comDetailStatisticsDay);
            comDetailStatisticsMonth = new ComDetailStatistics();
            comDetailStatisticsMonth.setRemark("本月合计");
            comDetailStatisticsMonth.setAccountDate(tempDate.substring(0,7));
            comDetailStatisticsMonth.setIncome(totalIncomeMonth);
            comDetailStatisticsMonth.setPay(totalPayMonth);
            comDetailStatisticsMonth.setBalance(result.get(result.size()-1).getBalance());
            result.add(comDetailStatisticsMonth);
            //加入本年
            comDetailStatisticsYear = new ComDetailStatistics();
            comDetailStatisticsYear.setRemark("本年累计");
            comDetailStatisticsYear.setAccountDate(tempDate.substring(0,4));
            comDetailStatisticsYear.setIncome(totalIncomeYear);
            comDetailStatisticsYear.setPay(totalPayYear);
            comDetailStatisticsYear.setBalance(result.get(result.size()-1).getBalance());
            result.add(comDetailStatisticsYear);
        }
        return result;
    }


    private void addComBeginBalance(List result, BigDecimal begin, String startTime){
        ComDetailStatistics first = new ComDetailStatistics();
        first.setBalance(begin);
        first.setSynAccountDate("");
        first.setAccountDate(startTime);
        first.setRemark("期初余额");
        first.setPay(BigDecimal.ZERO);
        first.setIncome(BigDecimal.ZERO);
        first.setBankAccount("");
        first.setBankCode("");
        first.setBankName("");
        first.setSubject("");
        result.add(first);
    }






    //重新计算银行明细
    public List<BankDetailStatistics> getBankDetailList(String startTime,
                                                        String endTime,String accountId){
        //查询账单明细
        List<BankDetailStatistics> bankDetailList = hdReportFeign.getBankDetailList(startTime,endTime,TenantContextHolder.getTenantId(),
                accountId);
        List<BankDetailStatistics> result = new ArrayList<>();
        //加入期初
        String balance = hdReportFeign.getBankReportBalance(startTime,TenantContextHolder.getTenantId(),accountId);
        //封装期末
        BigDecimal beginBalance = BigDecimal.ZERO;
        if(StringUtil.isNotEmpty(balance)){
            beginBalance = new BigDecimal(balance);
        }
        String tempDate = startTime;
        //封装期初
        BankDetailStatistics bankDetailStatisticsDay = null;
        BankDetailStatistics bankDetailStatisticsMonth = null;
        BankDetailStatistics bankDetailStatisticsYear = null;
        BigDecimal totalIncomeDay = BigDecimal.ZERO;
        BigDecimal totalPayDay = BigDecimal.ZERO;
        BigDecimal totalIncomeMonth = BigDecimal.ZERO;
        BigDecimal totalPayMonth = BigDecimal.ZERO;
        BigDecimal totalIncomeYear = BigDecimal.ZERO;
        BigDecimal totalPayYear = BigDecimal.ZERO;
        if(bankDetailList!=null&&bankDetailList.size()>0){
            //加入期初
            addBeginBalance(result,beginBalance,startTime);
            for(BankDetailStatistics bankDetailStatistics:bankDetailList){
                beginBalance = beginBalance.add(bankDetailStatistics.getIncome()).subtract(
                        bankDetailStatistics.getPay()
                );
                bankDetailStatistics.setBalance(beginBalance);
                result.add(bankDetailStatistics);
                //计算本日合计
                if(tempDate.substring(0,10).equals(bankDetailStatistics.getAccountDate().substring(0,10))){
                    totalIncomeDay = totalIncomeDay.add(bankDetailStatistics.getIncome());
                    totalPayDay = totalPayDay.add(bankDetailStatistics.getPay());
                }else{
                    if(totalIncomeDay.compareTo(BigDecimal.ZERO)==0&&
                            totalPayDay.compareTo(BigDecimal.ZERO)==0){
                        totalIncomeDay = totalIncomeDay.add(bankDetailStatistics.getIncome());
                        totalPayDay = totalPayDay.add(bankDetailStatistics.getPay());
                    }else{
                        //新增本日合计同时
                        bankDetailStatisticsDay = new BankDetailStatistics();
                        bankDetailStatisticsDay.setAccountDate(tempDate);
                        bankDetailStatisticsDay.setIncome(totalIncomeDay);
                        bankDetailStatisticsDay.setPay(totalPayDay);
                        bankDetailStatisticsDay.setRemark("本日合计");
                        if(result.size()>1){
                            bankDetailStatisticsDay.setBalance(result.get(result.size()-2).getBalance());
                        }else{
                            bankDetailStatisticsDay.setBalance(result.get(0).getBalance());
                        }
                        result.add(result.size()-1,bankDetailStatisticsDay);
                        totalIncomeDay = bankDetailStatistics.getIncome();
                        totalPayDay = bankDetailStatistics.getPay();
                    }
                }

                //计算本月合计
                if(tempDate.substring(0,7).equals(bankDetailStatistics.getAccountDate().substring(0,7))){
                    totalIncomeMonth = totalIncomeMonth.add(bankDetailStatistics.getIncome());
                    totalPayMonth = totalPayMonth.add(bankDetailStatistics.getPay());
                }else{
                    if(totalIncomeMonth.compareTo(BigDecimal.ZERO)==0&&
                            totalPayMonth.compareTo(BigDecimal.ZERO)==0){
                        totalIncomeMonth = totalIncomeMonth.add(bankDetailStatistics.getIncome());
                        totalPayMonth = totalPayMonth.add(bankDetailStatistics.getPay());
                    }else{
                        //新增本日合计同时
                        bankDetailStatisticsMonth = new BankDetailStatistics();
                        bankDetailStatisticsMonth.setRemark("本月合计");
                        bankDetailStatisticsMonth.setAccountDate(tempDate.substring(0,7));
                        bankDetailStatisticsMonth.setIncome(totalIncomeMonth);
                        bankDetailStatisticsMonth.setPay(totalPayMonth);
                        if(result.size()>1){
                            bankDetailStatisticsMonth.setBalance(result.get(result.size()-2).getBalance());
                        }else{
                            bankDetailStatisticsMonth.setBalance(result.get(0).getBalance());
                        }

                        result.add(result.size()-1,bankDetailStatisticsMonth);
                        totalIncomeMonth = bankDetailStatistics.getIncome();
                        totalPayMonth = bankDetailStatistics.getPay();
                    }
                }
                //计算本年合计
                if(tempDate.substring(0,4).equals(bankDetailStatistics.getAccountDate().substring(0,4))){
                    totalIncomeYear = totalIncomeYear.add(bankDetailStatistics.getIncome());
                    totalPayYear = totalPayYear.add(bankDetailStatistics.getPay());
                }else{
                    if(totalIncomeYear.compareTo(BigDecimal.ZERO)==0&&
                            totalPayYear.compareTo(BigDecimal.ZERO)==0){
                        totalIncomeYear = totalIncomeYear.add(bankDetailStatistics.getIncome());
                        totalPayYear = totalPayYear.add(bankDetailStatistics.getPay());
                    }else{
                        //新增本年合计同时
                        bankDetailStatisticsYear = new BankDetailStatistics();
                        bankDetailStatisticsYear.setRemark("本年累计");
                        bankDetailStatisticsYear.setAccountDate(tempDate.substring(0,4));
                        bankDetailStatisticsYear.setIncome(totalIncomeMonth);
                        bankDetailStatisticsYear.setPay(totalPayMonth);
                        if(result.size()>1){
                            bankDetailStatisticsYear.setBalance(result.get(result.size()-2).getBalance());
                        }else{
                            bankDetailStatisticsYear.setBalance(result.get(0).getBalance());
                        }

                        result.add(result.size()-1,bankDetailStatisticsYear);
                        totalIncomeYear = bankDetailStatistics.getIncome();
                        totalPayYear = bankDetailStatistics.getPay();
                    }
                }
                tempDate = bankDetailStatistics.getAccountDate();
            }
            bankDetailStatisticsDay = new BankDetailStatistics();
            bankDetailStatisticsDay.setAccountDate(tempDate);
            bankDetailStatisticsDay.setIncome(totalIncomeDay);
            bankDetailStatisticsDay.setPay(totalPayDay);
            bankDetailStatisticsDay.setBalance(result.get(result.size()-1).getBalance());
            bankDetailStatisticsDay.setRemark("本日合计");
            result.add(bankDetailStatisticsDay);
            bankDetailStatisticsMonth = new BankDetailStatistics();
            bankDetailStatisticsMonth.setRemark("本月合计");
            bankDetailStatisticsMonth.setAccountDate(tempDate.substring(0,7));
            bankDetailStatisticsMonth.setIncome(totalIncomeMonth);
            bankDetailStatisticsMonth.setPay(totalPayMonth);
            bankDetailStatisticsMonth.setBalance(result.get(result.size()-1).getBalance());
            result.add(bankDetailStatisticsMonth);
            //加入本年
            bankDetailStatisticsYear = new BankDetailStatistics();
            bankDetailStatisticsYear.setRemark("本年累计");
            bankDetailStatisticsYear.setAccountDate(tempDate.substring(0,4));
            bankDetailStatisticsYear.setIncome(totalIncomeYear);
            bankDetailStatisticsYear.setPay(totalPayYear);
            bankDetailStatisticsYear.setBalance(result.get(result.size()-1).getBalance());
            result.add(bankDetailStatisticsYear);
        }
        return result;
    }


    private void addBeginBalance(List result, BigDecimal begin, String startTime){
        BankDetailStatistics first = new BankDetailStatistics();
        first.setBalance(begin);
        first.setAccountDate(startTime);
        first.setRemark("期初余额");
        first.setPay(BigDecimal.ZERO);
        first.setIncome(BigDecimal.ZERO);
        first.setCompanyCode("");
        first.setCompanyName("");
        first.setSubject("");
        result.add(first);
    }






}