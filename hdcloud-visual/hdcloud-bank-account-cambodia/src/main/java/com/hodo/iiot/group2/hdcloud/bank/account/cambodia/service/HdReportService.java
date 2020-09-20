package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.service;

import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019-12-07.
 */
public interface HdReportService {
    public List<BankDetailStatistics> getBankDetailList(String startTime,
                                                        String endTime, String accountId);

    public List<ComDetailStatistics> getComDetailList(
            String startTime,
            String endTime,String companyId
    );

    public List<BankComStatistics> getBankComStatistics(String dateStart, String dateEnd);

    public List<ComBankStatistics> getComBankStatistics(String dateStart, String dateEnd);
    //银行报表
    List<BankStatistics> getBankStatistics(@Param("startTime")String dateStart,
                                               @Param("endTime")String dateEnd,
                                               @Param("userId")String userId,
                                               @Param("tenantId") Integer tenantId);
    //公司报表
    List<Statistics> statics(@Param("startTime") String startTime,
                                   @Param("endTime") String endTime,
                                   @Param("tenantId") Integer tenantId,
                                   @Param("userId") String userId,
                                   @Param("ids")List<String> ids);
}
