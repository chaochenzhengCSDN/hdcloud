package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.util.ExcelUtil;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankStatementGfBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdCompanyBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.User;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdCompanyFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.StringUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("hdCompany")
@Api(tags = "公司管理模块")
public class HdCompanyController {


    @Autowired
    private HdCompanyFeign hdCompanyFeign;

    @Autowired
    private HdBankStatementGfBiz hdBankStatementGfBiz;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    HdCompanyBiz hdCompanyBiz;


    //为了加入用户名和id
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询单个对象")

    public R<HdCompany> get(@PathVariable("id") String id) {
        R<HdCompany> entityObjectRestResponse = new R<HdCompany>();
        System.out.println(hdCompanyFeign.selectById(id).getData());
        HdCompany o = hdCompanyFeign.selectById(id).getData();
        if(o!=null){
            User user = new User();
            String userId = o.getCreateBy();
            if(StringUtil.isNotEmpty(userId)){
                try{
//                    user = iUserFeign.getById(userId);
                }catch (Exception e){
                    e.printStackTrace();
                    entityObjectRestResponse.setCode(500);
                    entityObjectRestResponse.setMsg("数据获取异常");
                    return entityObjectRestResponse;
                }
            }
            userId = jjUtil.handleUserId(userId,user);
            o.setCreateBy(userId);
        }
        return R.ok(o);
    }



    //page展示//添加录入人条件6.24

    //获取所有公司名称
    //添加录入人条件6.24
    @ApiOperation("获取所有公司名称")
    @RequestMapping(value = "/getAllCompanyNames", method = RequestMethod.GET)
    @ResponseBody
    public R<List<HdCompany>> getAllCompanyNames() {
        List<HdCompany> hdCompanyList = hdCompanyBiz.getAllCompanyNames();
        return  R.ok(hdCompanyList);
    }


    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE}
    )
    @ResponseBody
    @ApiOperation("移除单个对象")
    public R remove(@PathVariable String id) {
        //验证是否有绑定数据
        //账单表如果有则无法删除,根据公司id查询账单记录
        List<HdBankStatement> hdBankStatements =  hdBankStatementGfBiz.getAccountsByCom(id);
        if(hdBankStatements!=null&&hdBankStatements.size()>0){
            return R.failed("该公司下有账单信息,删除会导致统计错误！");
        }
        hdCompanyFeign.removeById(id);
        return R.ok("删除成功");
    }




    /**
     * 批量删除公司操作记录
     *
     * @return
     */
    @ApiOperation("批量删除银行操作记录")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return  R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                List<HdBankStatement> hdBankStatements =  hdBankStatementGfBiz.getAccountsByCom(id);
                if(hdBankStatements!=null&&hdBankStatements.size()>0){
                    return R.failed("某公司下有账单信息,删除会导致统计错误！");
                }
                //调用feign删除
                hdCompanyFeign.removeById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "公司删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return  R.ok(message);
    }



    //重写公司录入,公司名不可重复
    //重寫插入，判斷是否内转外转重复
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public R add(@RequestBody HdCompany entity) {
        int b = 1;
        try {
            entity.setCreateTime(new Date());
            b = hdCompanyFeign.insertSelective(entity).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(0==b){
            return R.ok("录入成功");
        }else {
            return R.failed("请检查公司名或编码是否重复");
        }
    }





    /**
     * 导出
     */
    //添加录入人条件6.24
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(String userName,
                          String company,String code) throws ParseException {
        //只有登录人可以导出
        List<HdCompany> allCompanyEx = hdCompanyBiz.getAllCompanyEx(company,code);
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        try {
            excelUtil.print("银行公司管理列表", HdCompany.class, "银行公司管理列表", "导出时间:" + jjUtil.getDateStr(), "公司管理列表", allCompanyEx, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //添加录入人条件6.24
    @ApiOperation("导入")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        List<HdCompany> companies;
        int b = 1;
        ExcelUtil excelUtil = new ExcelUtil<HdCompany>();
        int count = 0;
        try {
            companies = excelUtil.importXls(file, HdCompany.class);
            if (companies != null && companies.size() > 0) {
                for(HdCompany hdCompany:companies){
                    count++;
                    if(StringUtil.isEmpty(hdCompany.getCode())){
                        return  R.failed("第" + count + "条公司编号为空");
                    }
                }
                b = hdCompanyBiz.batchSave(companies);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("导入失败");
        }
        //检查是否有同名，是否无名称
        if(b == 0){
            return R.ok("导入成功");
        }else {
            return R.failed("导入失败");
        }
    }



    //6.27
    //根据名称获取id
    @RequestMapping(value = "/getComByName", method = RequestMethod.GET)
    public HdCompany getComByName(@RequestParam("name") String name){
        Map<String, Object> params = new HashedMap();
        params.put("name",name);
        return hdCompanyFeign.getHdCompanyList(params).get(0);
    }



    @RequestMapping(value = "/getComById", method = RequestMethod.GET)
    public HdCompany getComById(@RequestParam("id") String id){
        return hdCompanyFeign.selectById(id).getData();
    }



    @RequestMapping(
            value = {""},
            method = {RequestMethod.PUT}
    )
    @ResponseBody
    @ApiOperation("更新单个对象")
    public R update(@RequestBody  HdCompany entity) {
        int b = 1;
        try{
            b = hdCompanyFeign.updateSelectiveById(entity).getCode();
        }catch (Exception e) {
        }
        if(0==b){
            return R.ok("更新成功");
        }else {
            return R.failed("请检查公司名或编码是否重复");
        }
    }




    @ApiOperation("分页获取数据*")
    @RequestMapping(
            value = {"/page"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        try{
            String pageNumber = jjUtil.handleParams(params, "page");
            String pageSize = jjUtil.handleParams(params, "limit");
            if("".equals(pageNumber)){ params.put("page",pageNumber);}
            if("".equals(pageSize)){ params.put("limit",pageSize);}
        }catch (Exception e){
            return R.failed("数据获取异常");
        }

        return hdCompanyFeign.getHdCompanyPage(params);
    }







}
