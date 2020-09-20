package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;

import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteReport", value = ServiceNameConstants.REPORT_DATA)
public interface HdReportFeign {


    @RequestMapping(value = "/report/gf/selectStatics", method = RequestMethod.GET)
    List<Statistics> selectStatics(@RequestParam("startTime") String startTime,
                                   @RequestParam("endTime") String endTime,
                                   @RequestParam("tenantId") Integer tenantId,
                                   @RequestParam("userId") String userId,
                                   @RequestParam("ids")List<String> ids);


    @RequestMapping(value = "/report/gf/getComBankStatistics", method = RequestMethod.GET)
    List<ComBankStatistics> getComBankStatistics(@RequestParam("dateStart") String dateStart,
                                                 @RequestParam("dateEnd") String dateEnd,
                                                 @RequestParam("tenantId") Integer tenantId);


    @RequestMapping(value = "/report/gf/getBankStatisticsList", method = RequestMethod.GET)
    List<BankStatistics> getBankStatisticsList(@RequestParam("dateStart")String dateStart,
                                               @RequestParam("dateEnd")String dateEnd,
                                               @RequestParam("userId")String userId,
                                               @RequestParam("tenantId") Integer tenantId);

    @RequestMapping(value = "/report/gf/getBankComStatistics", method = RequestMethod.GET)
    List<BankComStatistics> getBankComStatistics(@RequestParam("dateStart")String dateStart,
                                                 @RequestParam("dateEnd")String dateEnd,
                                                 @RequestParam("tenantId") Integer tenantId);


    @RequestMapping(value = "/report/gf/getComDetailList", method = RequestMethod.GET)
    List<ComDetailStatistics> getComDetailList(@RequestParam("dateStart")String dateStart,
                                               @RequestParam("dateEnd")String dateEnd, @RequestParam("tenantId")Integer tenantId,
                                               @RequestParam("companyId")String companyId);


    @RequestMapping(value = "/report/gf/getComReportBalance", method = RequestMethod.GET)
    String getComReportBalance(@RequestParam("dateStart")String dateStart,@RequestParam("tenantId") Integer tenantId,
                               @RequestParam("companyId")String companyId);


    @RequestMapping(value = "/report/gf/getBankDetailList", method = RequestMethod.GET)
    List<BankDetailStatistics> getBankDetailList(@RequestParam("dateStart")String dateStart,
                                                @RequestParam("dateEnd")String dateEnd,@RequestParam("tenantId")Integer tenantId,
                                                @RequestParam("accountId")String accountId);


    @RequestMapping(value = "/report/gf/getBankReportBalance", method = RequestMethod.GET)
    String getBankReportBalance(@RequestParam("dateStart")String dateStart,@RequestParam("tenantId") Integer tenantId,
                               @RequestParam("companyId")String companyId);


}
