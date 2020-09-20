package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdCompanyBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdMatchCompanyBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdMatchCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdCompanyFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdMatchCompanyFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.StringUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("hdMatchCompany")
@Api(tags = "匹配公司管理模块")
public class HdMatchCompanyController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    HdMatchCompanyBiz hdMatchCompanyBiz;

    @Autowired
    HdCompanyBiz hdCompanyBiz;

    @Autowired
    HdMatchCompanyFeign hdMatchCompanyFeign;

    @Autowired
    HdCompanyFeign hdCompanyFeign;




//    @RequestMapping(
//            value = {"/{id}"},
//            method = {RequestMethod.GET}
//    )
//    @ResponseBody
//    @ApiOperation("查询单个对象")
//    public R<HdMatchCompany> get(@PathVariable String id) {
//        HdMatchCompany o = hdMatchCompanyFeign.selectById(id).getData();
//        if(o!=null){
//            String companyCode = o.getCompanyCode();
//            if(StringUtil.isNotEmpty(companyCode)){
//                HdCompany hdCompany = hdCompanyFeign.selectById(companyCode).getData();
//                if(hdCompany!=null){
//                    o.setCompanyName(hdCompany.getId()+","+hdCompany.getName());
//                }
//            }
//        }
//        return R.ok(o);
//    }






    @ResponseBody
    @ApiOperation("新增单个对象")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R<HdMatchCompany> add(@RequestBody HdMatchCompany entity) {
        int b;
        try{
//            entity.setTenantId(BaseContextHandler.getTenantID());
            entity.setCreateTime(new Date());
            if(StringUtil.isEmpty(entity.getCompanyId())){
                return R.failed("参数获取失败");
            }
            String companyId = entity.getCompanyId();
            HdCompany hdCompany = hdCompanyFeign.selectById(companyId).getData();
            if(hdCompany==null){
                return R.failed("公司获取失败");
            }
            entity.setCompanyCode(hdCompany.getCode());
            entity.setCompanyName(hdCompany.getName());
            b = hdMatchCompanyFeign.insertSelective(entity).getCode();
        }catch (DuplicateKeyException e) {
            e.printStackTrace();
            return R.failed("请检查公司和对方单位是否一一对应");
        } catch (Exception e){
            e.printStackTrace();
            return R.failed("添加失败");
        }

        if(0==b){
            return R.ok(entity,"添加成功");
        }else {
            return R.failed("添加失败,请检查公司和对方单位是否一一对应");
        }


    }




    //公司名匹配查询重写page
    //添加录入人条件6.24
    @ApiOperation("分页获取数据2")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //公司名精准查询,内部抬头外部抬头模糊查询
        long total = 0;
        Object data = "";
        List<HdMatchCompany> hdMatchCompanyList;
        List<HdMatchCompany> allMatchCompanyList;

        try{

            hdMatchCompanyList = hdMatchCompanyFeign.getHdMatchCompanyPage2(params);
            allMatchCompanyList =  hdMatchCompanyFeign.getHdMatchCompanyList2(params);
//            if (hdMatchCompanyList != null && hdMatchCompanyList.size() > 0) {
//                for (HdMatchCompany hdMatchCompany : hdMatchCompanyList) {
//                    String companyId = hdMatchCompany.getCompanyName();
//                    if (StringUtil.isNotEmpty(companyId)) {
//                        String company_name = (hdCompanyFeign.selectById(companyId).getData()).getName();
//                        hdMatchCompany.setCompanyName(company_name);
//                    }
//                }
//            }
            if (allMatchCompanyList != null && allMatchCompanyList.size() > 0) {
                total = allMatchCompanyList.size();
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("数据查询异常");
        }

        IPage iPage = new Page();
        iPage.setTotal(total);
        iPage.setRecords(hdMatchCompanyList);
        return R.ok(iPage);
    }




    /**
     * 单个删除公司名匹配表
     *
     * @return
     */
    @ApiOperation("单个删除公司名匹配表")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public R doDel(@PathVariable String id) {
        String message = null;
        int b = 1;
        try {
                b = hdMatchCompanyFeign.removeById(id).getCode();
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司名匹配表删除失败";
            return R.failed(message);
        }
        if(0==b){
            message = "删除成功";
            return R.ok(message);
        }else{
            message = "公司名匹配表删除失败";
            return R.failed(message);
        }
    }






    /**
     * 批量删除公司名匹配表
     *
     * @return
     */
    @ApiOperation("批量删除公司名匹配表")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                hdMatchCompanyFeign.removeById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司名匹配表删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }




    /**
     * 导出
     */
    //添加录入人条件6.24
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
//    @IgnoreUserToken
//    @IgnoreClientToken
    public void exportXls(String userName,
                          String customer_name, String company) throws ParseException {
        Map<String, Object> params = new HashedMap();
        if (StringUtil.isNotEmpty(customer_name)) {
            params.put("customer_name", customer_name);
        }
        //只有登录人可以导出
        //params.put("create_by",userId);
        List<HdMatchCompany> allMatchCompanyList = hdMatchCompanyFeign.getHdMatchCompanyList2(params);
        if (allMatchCompanyList != null && allMatchCompanyList.size() > 0) {
            for (HdMatchCompany hdMatchCompany : allMatchCompanyList) {
                if (StringUtil.isNotEmpty(hdMatchCompany.getCompanyName())) {
                    HdCompany hdCompany = hdCompanyFeign.selectById(hdMatchCompany.getCompanyId()).getData();
                    String company_name = hdCompany.getName();
                    hdMatchCompany.setCompanyName(company_name);
                }
            }
        }

        ExcelUtil excelUtil = new ExcelUtil<HdMatchCompany>();
        try {
            excelUtil.print("公司匹配信息管理列表", HdMatchCompany.class, "公司匹配信息管理列表", "导出时间:" + jjUtil.getDateStr(), "公司匹配信息管理列表", allMatchCompanyList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //导入不用写,在微服务中
    //添加录入人条件6.24
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {

        //外部抬头，公司必填，内部抬头不必填，公司需匹配不能乱填
        List<HdMatchCompany> hdMatchCompanies;
        ExcelUtil excelUtil = new ExcelUtil<HdMatchCompany>();
        try {
            hdMatchCompanies = excelUtil.importXls(file, HdMatchCompany.class);
            int count = 0;
            if (hdMatchCompanies != null && hdMatchCompanies.size() > 0) {

                //检查外部抬头，公司是否为空，公司能否匹配
                for (HdMatchCompany hdMatchCompany : hdMatchCompanies) {
                    count++;
                    if (StringUtil.isEmpty(hdMatchCompany.getCustomerName())) {
                        return R.failed("第" + count + "条外部抬头为空");
                    }
                    if (StringUtil.isEmpty(hdMatchCompany.getCompanyCode())) {
                        return R.failed("第" + count + "条公司编码为空");
                    }
                    Map<String,Object> map = new HashedMap();
                    map.put("code",hdMatchCompany.getCompanyCode());
                    HdCompany hdCompany = (hdCompanyFeign.getHdCompanyList(map)).get(0);
                    if (hdCompany == null) {
                        return R.failed("第" + count + "条公司无法与系统内部匹配");
                    }
                    hdMatchCompany.setCompanyName(hdCompany.getName());
                    hdMatchCompany.setCompanyId(hdCompany.getId());
                }
                try{
                    hdMatchCompanyFeign.batchSave(hdMatchCompanies);
                }catch (DuplicateKeyException e) {
                    e.printStackTrace();
                    return R.failed("请检查内部抬头和外部抬头，两者不能同时相同");
                } catch (Exception e){
                    e.printStackTrace();
                    return R.failed("添加失败");
                }

            }
        } catch (Exception e) {
            return R.failed("导入失败");
        }

        return R.ok("导入成功");
    }





    //重写编辑，提示不能重复
    @RequestMapping(
            value = {""},
            method = {RequestMethod.PUT}
    )
    @ResponseBody
    @ApiOperation("更新单个对象")
    public R<HdMatchCompany> update(@RequestBody HdMatchCompany entity) {
        int b = 1;
        try{

            if(StringUtil.isEmpty(entity.getCompanyName())){
                return R.failed("参数获取失败");
            }
            String companyId = entity.getCompanyId();
            HdCompany hdCompany = hdCompanyFeign.selectById(companyId).getData();
            if(hdCompany==null){
                return R.failed("公司获取失败");
            }
            entity.setCompanyCode(hdCompany.getCode());
            entity.setCompanyName(hdCompany.getName());
            entity.setCompanyId(hdCompany.getId());
            b = hdMatchCompanyFeign.updateSelectiveById(entity).getCode();
        }catch (DuplicateKeyException e) {
            return R.failed("请检查内部抬头和外部抬头，两者不能同时相同");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("更新失败");
        }

        if(0==b){
            return R.ok(entity,"更新成功");
        }else{
            return R.failed("更新失败");
        }
    }







}
