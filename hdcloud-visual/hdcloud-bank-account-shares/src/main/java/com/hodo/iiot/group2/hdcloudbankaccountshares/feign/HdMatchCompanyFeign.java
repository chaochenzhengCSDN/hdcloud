package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdMatchCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteMatchCompany", value = ServiceNameConstants.BASE_DATA)
public interface HdMatchCompanyFeign {

    @RequestMapping(value = "/hdmatchcompany/gf/{id}", method = RequestMethod.GET)
    R<HdMatchCompany> selectById(@PathVariable("id") String id);


    @RequestMapping(value = "/hdmatchcompany/gf/", method = RequestMethod.POST)
    R insertSelective(@RequestBody HdMatchCompany hdMatchCompany);


    @RequestMapping(value = "/hdmatchcompany/gf/", method = RequestMethod.GET)
    R getAllCompanyNames();


    @RequestMapping(value = "/hdmatchcompany/gf/page", method = RequestMethod.GET)
    R getHdMatchCompanyPage(@RequestParam Map<String, Object> map);

    @GetMapping(value = "/hdmatchcompany/gf/page2")
    List<HdMatchCompany> getHdMatchCompanyPage2(@RequestParam Map<String, Object> map);

    @RequestMapping(value = "/hdmatchcompany/gf/all2", method = RequestMethod.GET)
    List<HdMatchCompany> getHdMatchCompanyList(@RequestParam Map<String, Object> map);

    @RequestMapping(value = "/hdmatchcompany/gf/all2", method = RequestMethod.GET)
    List<HdMatchCompany> getHdMatchCompanyList2(@RequestParam Map<String, Object> map);



    @RequestMapping(value = "/hdmatchcompany/gf/", method = RequestMethod.GET)
    R getCompany(@RequestBody HdMatchCompany hdMatchCompany);


    @RequestMapping(value = "/hdmatchcompany/gf/", method = RequestMethod.PUT)
    R updateSelectiveById(@RequestBody HdMatchCompany hdMatchCompany);


    @RequestMapping(value = "/hdmatchcompany/gf/addList", method = RequestMethod.POST)
    Boolean batchSave(@RequestBody List<HdMatchCompany> list);


    @RequestMapping(value = "/hdmatchcompany/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);

    //待实现
    @RequestMapping(value = "/hdmatchcompany/gf/getHdMatchCompanyList", method = RequestMethod.GET)
    List<HdMatchCompany> getHdMatchCompanyTotal(@RequestParam("companyName") String companyName,@RequestParam("page")String page,@RequestParam("limit")String limit,
                                               @RequestParam("params")Map<String,Object> params);

    //待实现
    @RequestMapping(value = "/hdmatchcompany/gf/getAllMatchCompany", method = RequestMethod.GET)
    List<HdMatchCompany> getAllMatchCompany(@RequestParam("companyName") String companyName,@RequestParam("params")Map<String,Object> params);


    //待实现
    @RequestMapping(value = "/hdmatchcompany/gf/getMatchCompanyByTerm", method = RequestMethod.GET)
    List<HdMatchCompany> getMatchCompanyByTerm(@RequestParam("customerName") String customerName);



}




