package com.hodo.iiot.group2.hdcloud.bank.account.currency.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.Base.impl.CommonServiceImpl;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyBankDetailStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyBankStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyComDetailStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBillService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteReportService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.service.HdReportService;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019-11-15.
 */
@Service
public class HdReportServiceImpl implements HdReportService {
    @Autowired
    private RemoteReportService remoteReportService;

    //银行总计
    public List<TyBankStatistics> getDataByBank(String startTime, String endTime, String bankName,String flag){
        List<TyBankStatistics> bankStatisticsList = remoteReportService.getBankList(startTime,endTime,bankName,flag,TenantContextHolder.getTenantId().toString().toString());
        bankStatisticsList = Commom(bankStatisticsList,flag);
        return bankStatisticsList;
    }



    //公司总计
    public List<TyBankStatistics> getDataByCompany(String startTime,String endTime,String companyName,String flag){
        List<TyBankStatistics> bankStatisticsList = remoteReportService.getCompanyList(startTime,endTime,companyName,flag,TenantContextHolder.getTenantId().toString());
        bankStatisticsList = Commom(bankStatisticsList,flag);
        return bankStatisticsList;
    }


    //成员单位总计
    public List<TyBankStatistics> getDataByMemberUnit(String startTime, String endTime, String memberUnitName, String companyName,String flag){
        List<TyBankStatistics> bankStatisticsList = remoteReportService.getMemberUnitList(startTime,endTime,companyName,memberUnitName,flag,TenantContextHolder.getTenantId().toString());
        bankStatisticsList = Commom(bankStatisticsList,flag);
        return bankStatisticsList;
    }


    //银行单位总计
    public List<TyBankStatistics> getDataByBankUnit(String startTime,String endTime,String bankName,String companyName,String memberUnitName,String flag){
        List<TyBankStatistics> bankStatisticsList = remoteReportService.getBankMemberUnitList(startTime,endTime,bankName,companyName,memberUnitName,flag,TenantContextHolder.getTenantId().toString());
        bankStatisticsList = Commom(bankStatisticsList,flag);
        return bankStatisticsList;
    }


    //银行公司总计
    public List<TyBankStatistics> getDataByBankCompany(String startTime,String endTime,String bankName,String companyName,String flag){
        List<TyBankStatistics> bankStatisticsList = remoteReportService.getBankCompanyList(startTime,endTime,bankName,companyName,flag,TenantContextHolder.getTenantId().toString());
        bankStatisticsList = Commom(bankStatisticsList,flag);
        return bankStatisticsList;
    }



    //计算期末和合计方法抽取
    private List<TyBankStatistics> Commom(List<TyBankStatistics> bankStatisticsList,String flag) {
        TyBankStatistics last = new TyBankStatistics();
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        BigDecimal sumBegin = BigDecimal.ZERO;
        BigDecimal sumEnd = BigDecimal.ZERO;
        BigDecimal endBalance = BigDecimal.ZERO;
        BigDecimal sumNzIncome = BigDecimal.ZERO;
        BigDecimal sumNzPay = BigDecimal.ZERO;
        List<TyBankStatistics> result = new ArrayList<>();
        //计算期末和合计
        if (bankStatisticsList != null && bankStatisticsList.size() > 0) {
            for (TyBankStatistics bankStatistics : bankStatisticsList) {
                BigDecimal beginBalance = bankStatistics.getBeginBalance();
                BigDecimal income = bankStatistics.getCurrentIncome();
                BigDecimal pay = bankStatistics.getCurrentPay();
                BigDecimal nzIncome = bankStatistics.getNzIncome();
                BigDecimal nzPay = bankStatistics.getNzPay();
                sumIncome = sumIncome.add(income).subtract(nzIncome);
                sumPay = sumPay.add(pay).subtract(nzPay);
                sumNzIncome = sumNzIncome.add(nzIncome);
                sumNzPay = sumNzPay.add(nzPay);
                sumBegin = sumBegin.add(beginBalance);
                endBalance = income.subtract(pay).add(beginBalance);
                bankStatistics.setCurrentIncome(income.subtract(nzIncome));
                bankStatistics.setCurrentPay(pay.subtract(nzPay));
                sumEnd = sumEnd.add(endBalance);
                bankStatistics.setEndBalance(endBalance);
            }
            last.setBeginBalance(sumBegin);
            last.setCurrentIncome(sumIncome);
            last.setCurrentPay(sumPay);
            last.setNzIncome(sumNzIncome);
            last.setNzPay(sumNzPay);
            last.setEndBalance(sumEnd);
            last.setBankCode("合计:");

            List<TyBankStatistics> newbankStatisticsList = new ArrayList<>();
            if("1".equals(flag)&&bankStatisticsList.size()>0){
                for(int i=0;i<bankStatisticsList.size();i++){
                    if(Double.valueOf(bankStatisticsList.get(i).getEndBalance().toString())!=0.00){
                        newbankStatisticsList.add(bankStatisticsList.get(i));
                    }
                }
                bankStatisticsList = newbankStatisticsList;
            }
            bankStatisticsList.add(last);

            result = bankStatisticsList;

        }


        return result;
    }






    //重新计算银行明细
    public List<TyBankDetailStatistics> getBankDetailList(String startTime,
                                                          String endTime,String accountId){
        //查询账单明细
        List<TyBankDetailStatistics> bankDetailList = remoteReportService.getBankDetailList(startTime,endTime,
                accountId,TenantContextHolder.getTenantId().toString());
        List<TyBankDetailStatistics> result = new ArrayList<>();
        //加入期初
        String balance = remoteReportService.getBankReportBalance(startTime,accountId,TenantContextHolder.getTenantId().toString());
        //封装期末
        BigDecimal beginBalance = BigDecimal.ZERO;
        if(StringUtil.isNotEmpty(balance)){
            beginBalance = new BigDecimal(balance);
        }
        String tempDate = startTime;
        //封装期初
        TyBankDetailStatistics bankDetailStatisticsDay = null;
        TyBankDetailStatistics bankDetailStatisticsMonth = null;
        TyBankDetailStatistics bankDetailStatisticsYear = null;
        BigDecimal totalIncomeDay = BigDecimal.ZERO;
        BigDecimal totalPayDay = BigDecimal.ZERO;
        BigDecimal totalIncomeMonth = BigDecimal.ZERO;
        BigDecimal totalPayMonth = BigDecimal.ZERO;
        BigDecimal totalIncomeYear = BigDecimal.ZERO;
        BigDecimal totalPayYear = BigDecimal.ZERO;
        if(bankDetailList!=null&&bankDetailList.size()>0){
            //加入期初
            addBeginBalance(result,beginBalance,startTime);
            for(TyBankDetailStatistics bankDetailStatistics:bankDetailList){
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
                        bankDetailStatisticsDay = new TyBankDetailStatistics();
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
                        bankDetailStatisticsMonth = new TyBankDetailStatistics();
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
                        bankDetailStatisticsYear = new TyBankDetailStatistics();
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
            bankDetailStatisticsDay = new TyBankDetailStatistics();
            bankDetailStatisticsDay.setAccountDate(tempDate);
            bankDetailStatisticsDay.setIncome(totalIncomeDay);
            bankDetailStatisticsDay.setPay(totalPayDay);
            bankDetailStatisticsDay.setBalance(result.get(result.size()-1).getBalance());
            bankDetailStatisticsDay.setRemark("本日合计");
            result.add(bankDetailStatisticsDay);
            bankDetailStatisticsMonth = new TyBankDetailStatistics();
            bankDetailStatisticsMonth.setRemark("本月合计");
            bankDetailStatisticsMonth.setAccountDate(tempDate.substring(0,7));
            bankDetailStatisticsMonth.setIncome(totalIncomeMonth);
            bankDetailStatisticsMonth.setPay(totalPayMonth);
            bankDetailStatisticsMonth.setBalance(result.get(result.size()-1).getBalance());
            result.add(bankDetailStatisticsMonth);
            //加入本年
            bankDetailStatisticsYear = new TyBankDetailStatistics();
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
        TyBankDetailStatistics first = new TyBankDetailStatistics();
        first.setBalance(begin);
        first.setAccountDate(startTime);
        first.setRemark("期初余额");
        first.setPay(BigDecimal.ZERO);
        first.setIncome(BigDecimal.ZERO);
        first.setCode("");
        first.setName("");
        first.setSubject("");
        result.add(first);
    }





    public List<TyComDetailStatistics> getComDetailList(
            String startTime,
            String endTime,String companyId
    ){
        //查询账单明细
        List<TyComDetailStatistics> comDetailList = remoteReportService.getComDetailList(startTime,endTime,
                companyId,TenantContextHolder.getTenantId().toString());
        List<TyComDetailStatistics> result = new ArrayList<>();
        //加入期初
        String balance = remoteReportService.getComReportBalance(startTime,companyId,TenantContextHolder.getTenantId().toString());

        BigDecimal beginBalance = BigDecimal.ZERO;
        if(StringUtil.isNotEmpty(balance)){
            beginBalance = new BigDecimal(balance);
        }
        String tempDate = startTime;
        TyComDetailStatistics comDetailStatisticsDay = null;
        TyComDetailStatistics comDetailStatisticsMonth = null;
        TyComDetailStatistics comDetailStatisticsYear = null;
        BigDecimal totalIncomeDay = BigDecimal.ZERO;
        BigDecimal totalPayDay = BigDecimal.ZERO;
        BigDecimal totalIncomeMonth = BigDecimal.ZERO;
        BigDecimal totalPayMonth = BigDecimal.ZERO;
        BigDecimal totalIncomeYear = BigDecimal.ZERO;
        BigDecimal totalPayYear = BigDecimal.ZERO;
        if(comDetailList!=null&&comDetailList.size()>0){
            //加入期初
            addComBeginBalance(result,beginBalance,startTime);
            for(TyComDetailStatistics comDetailStatistics:comDetailList){
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
                        comDetailStatisticsDay = new TyComDetailStatistics();
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
                        comDetailStatisticsMonth = new TyComDetailStatistics();
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
                        comDetailStatisticsYear = new TyComDetailStatistics();
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
            comDetailStatisticsDay = new TyComDetailStatistics();
            comDetailStatisticsDay.setAccountDate(tempDate);
            comDetailStatisticsDay.setIncome(totalIncomeDay);
            comDetailStatisticsDay.setPay(totalPayDay);
            comDetailStatisticsDay.setBalance(result.get(result.size()-1).getBalance());
            comDetailStatisticsDay.setRemark("本日合计");
            result.add(comDetailStatisticsDay);
            comDetailStatisticsMonth = new TyComDetailStatistics();
            comDetailStatisticsMonth.setRemark("本月合计");
            comDetailStatisticsMonth.setAccountDate(tempDate.substring(0,7));
            comDetailStatisticsMonth.setIncome(totalIncomeMonth);
            comDetailStatisticsMonth.setPay(totalPayMonth);
            comDetailStatisticsMonth.setBalance(result.get(result.size()-1).getBalance());
            result.add(comDetailStatisticsMonth);
            //加入本年
            comDetailStatisticsYear = new TyComDetailStatistics();
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
        TyComDetailStatistics first = new TyComDetailStatistics();
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
}
