package com.hodo.iiot.group2.hdcloud.bank.account.currency.service;

import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyBankDetailStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyBankStatistics;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.TyComDetailStatistics;

import java.util.List;

/**
 * Created by Administrator on 2019-11-15.
 */
public interface HdReportService {
    //银行统计
    List<TyBankStatistics> getDataByBank(String startTime,String endTime,
                                         String bankName,String flag);
    List<TyBankStatistics> getDataByCompany(String startTime,String endTime,
                                            String companyName,String flag);
    List<TyBankStatistics> getDataByMemberUnit(String startTime,String endTime,
                                               String companyName,String memberUnitName,
                                               String flag);
    List<TyBankStatistics> getDataByBankUnit(String startTime,String endTime,String bankName,
                                             String companyName,String memberUnitName,
                                             String flag);

    List<TyBankStatistics> getDataByBankCompany(String startTime,String endTime,String bankName,
                                                       String companyName,String flag);


    public List<TyBankDetailStatistics> getBankDetailList(String startTime,
                                                          String endTime, String accountId);
    public List<TyComDetailStatistics> getComDetailList(
            String startTime,
            String endTime,String companyId
    );


}
