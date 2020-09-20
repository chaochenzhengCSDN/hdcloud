package com.hodo.iiot.group2.hdcloudbankaccountshares.feign;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//feign调用服务
@FeignClient(contextId = "remoteCompany", value = ServiceNameConstants.BASE_DATA)
public interface HdCompanyFeign {

    @RequestMapping(value = "/hdcompany/gf/{id}", method = RequestMethod.GET)
    R<HdCompany> selectById(@PathVariable("id") String id);


    @PostMapping(value = "/hdcompany/gf")
    R insertSelective(@RequestBody HdCompany hdCompany);


//    @RequestMapping(value = "/hdcompany", method = RequestMethod.GET)
//    MR getAllCompanyNames();


    @RequestMapping(value = "/hdcompany/gf/page", method = RequestMethod.GET)
    R getHdCompanyPage(@RequestParam Map<String,Object> map);


    @GetMapping(value = "/hdcompany/gf/all")
    List<HdCompany>  getHdCompanyList(@RequestParam Map<String,Object> map);



    @RequestMapping(value = "/hdcompany/gf", method = RequestMethod.GET)
    R getCompany(@RequestBody HdCompany hdCompany);


    @RequestMapping(value = "/hdcompany/gf", method = RequestMethod.PUT)
    R updateSelectiveById(@RequestBody HdCompany hdCompany);


    @RequestMapping(value = "/hdcompany/gf/addList", method = RequestMethod.POST)
    R addList(@RequestBody List<HdCompany> list);


    @RequestMapping(value = "/hdcompany/gf/{id}", method = RequestMethod.DELETE)
    R removeById(@PathVariable("id") String id);






}




