package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;


import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankTenantBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankTenantFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hdBankTenant")
@Api(tags = "租户管理模块")
public class HdBankTenantController {

    @Autowired
    HdBankTenantFeign hdBankTenantFeign;

    @Autowired
    HdBankTenantBiz hdBankTenantBiz;


    //添加抬头和租户的关系
    @RequestMapping(
            value = {""},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    @ApiOperation("新增单个对象")
    public R add(@RequestBody HdBankTenant entity) {
        try {
            hdBankTenantFeign.insertSelective(entity);
            return R.ok(entity,"抬头绑定成功");
//            return (new ObjectRestResponse(200,"抬头绑定成功")).data(entity);
        }catch (DuplicateKeyException e){
            return R.ok(entity,"抬头已绑定成功");
//            return (new ObjectRestResponse(200,"抬头已绑定成功")).data(entity);
        }catch (Exception e){
            return  R.failed("抬头绑定失败");
        }
    }




    //根据租户id获取抬头
    @RequestMapping(
            value = {"getTenantNameById"},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    @ApiOperation("根据租户id获取银行名称")
    public R<String> getBankNameByTenant(@RequestParam("tenantId") String tenantId) {
        String result = "";
        try {
            HdBankTenant hdBankTenant = hdBankTenantBiz.getBankNameByTenant(tenantId);

            if(hdBankTenant!=null){
                result = hdBankTenant.getTenantName();
            }
            return R.ok(result,"抬头绑定成功");
//            return (new ObjectRestResponse(200,"抬头绑定成功")).data(result);
        }catch (Exception e){
            return  R.failed("抬头绑定失败");
        }
    }





}
