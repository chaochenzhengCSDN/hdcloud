package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankRecordBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankRecordFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdCompanyFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.StringUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("hdBankRecord")
@Api(tags = "处理记录管理模块")
public class HdBankRecordController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    HdBankRecordBiz hdBankRecordBiz;

    @Autowired
    HdBankRecordFeign hdBankRecordFeign;

    @Autowired
    HdCompanyFeign hdCompanyFeign;






    /**
     * 批量删除银行操作记录
     *
     * @return
     */
    @ApiOperation("批量删除银行操作记录")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public R doDel(@PathVariable("id") String id) {
        if (StringUtil.isEmpty(id)) {
            return R.failed("参数传递失败");
        }
        int b = 1;
        String message = null;
        int count = 0;
        try {
            b = hdBankRecordFeign.removeById(id).getCode();
            count++;
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行操作记录删除失败";
            return R.failed(message);
        }
        if(b==0){
            message = "删除成功";
            return R.ok(message);
        }else{
            message = "银行操作记录删除失败";
            return R.failed(message);
        }
    }




    /**
     * 批量删除银行操作记录
     *
     * @return
     */
    @ApiOperation("批量删除银行操作记录")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }

        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                hdBankRecordFeign.removeById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行操作记录删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }




    //重写page
    //添加录入人条件6.24
    @ApiOperation("分页获取数据2")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //公司精准查询，有时间查询，其他模糊查询
        //增加序号，银行精准查询
        //操作类型模糊查询

        long total = 0;
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        List<HdBankRecord> hdBankRecordList = hdBankRecordFeign.getHdBankRecordList2(params);
        List<HdBankRecord> allBankRecordList = hdBankRecordFeign.getAllBankRecord(params);
//        List<HdBankRecord> allMatchCompanyList = baseBiz.getAllBankRecord(companyName,startTime,endTime,params);
        if (hdBankRecordList != null && hdBankRecordList.size() > 0) {
            for (HdBankRecord hdBankRecord : hdBankRecordList) {
                hdBankRecordBiz.formatNo(hdBankRecord);
                String companyId = hdBankRecord.getCompanyId();
                if (StringUtil.isNotEmpty(companyId)) {
                    String company_name = (hdCompanyFeign.selectById(companyId).getData()).getName();
                    hdBankRecord.setCompanyId(company_name);
                }
                //获取创建人id
                String userId = hdBankRecord.getCreateBy();
                if(StringUtil.isNotEmpty(userId)){
//                    User user = iUserFeign.getById(userId);
//                    if(user!=null){
//                        hdBankRecord.setCreateBy(user.getName());
//                    }
                }

            }
        }
        if (allBankRecordList != null && allBankRecordList.size() > 0) {
            total = allBankRecordList.size();
        }
        IPage iPage = new Page();
        iPage.setRecords(hdBankRecordList);
        iPage.setTotal(total);

        return R.ok(iPage);
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
    public void exportXls(@RequestParam Map<String,Object> params) throws ParseException {

        List<HdBankRecord> allBankRecordList = hdBankRecordFeign.getAllBankRecord(params);
        //公司名称和收支方向
        if (allBankRecordList != null && allBankRecordList.size() > 0) {
            for (HdBankRecord hdBankRecord : allBankRecordList) {
                hdBankRecordBiz.formatNo(hdBankRecord);
                if (hdBankRecord.getFlag() != null) {
                    if ("0".equals(hdBankRecord.getFlag())) {
                        hdBankRecord.setFlag("收入");
                    } else {
                        hdBankRecord.setFlag("支出");
                    }
                }
                if (hdBankRecord.getCompanyId() != null) {
                    String company_name = (hdCompanyFeign.selectById(hdBankRecord.getCompanyId()).getData()).getName();
                    if (StringUtil.isNotEmpty(company_name)) {
                        hdBankRecord.setCompanyId(company_name);
                    }
                }
                //获取创建人id
                String userId = hdBankRecord.getCreateBy();
//                if(StringUtil.isNotEmpty(userId)){
//                    User user = iUserFeign.getById(userId);
//                    if(user!=null){
//                        hdBankRecord.setCreateBy(user.getName());
//                    }
//                }
            }
        }

        ExcelUtil excelUtil = new ExcelUtil<HdBankRecord>();
        try {
            excelUtil.print("银行处理记录列表", HdBankRecord.class, "银行处理记录列表", "导出时间:" + jjUtil.getDateStr(), "银行处理记录列表", allBankRecordList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }









}
