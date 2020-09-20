package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.ExcelUtil;
import com.github.wxiaoqi.security.common.util.MyBeanUtils;
import com.github.wxiaoqi.security.common.util.StringUtil;
import com.github.wxiaoqi.security.common.util.UUIDUtils;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.security.annotation.Inner;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankTenantBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdTransferBankStatementBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankTenant;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdNzbankAccountDY;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdTransferBankStatementFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("hdTransferBankStatement")
@Api(tags = "内转账单管理模块")
public class HdTransferBankStatementController {

    @Autowired
    HdTransferBankStatementBiz hdTransferBankStatementBiz;

    @Autowired
    HdTransferBankStatementFeign hdTransferBankStatementFeign;

    @Autowired
    HdBankTenantBiz hdBankTenantBiz;

    @Autowired
    HdBankAccountController hdBankAccountController;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;


    /**
     * 银行财务冲账
     */
    @ApiOperation("内转账单冲账*")
    @RequestMapping(value = "/doBatchNzCZ", method = RequestMethod.GET)
    @ResponseBody
    public R doBatchCZ(String ids) {
        String message = null;
        message = "内转账单冲账";
        int count = 0;
        try{
            for (String id : ids.split(",")) {
                HdTransferBankStatement hdNzbankAccount = hdTransferBankStatementFeign.selectById(id).getData();
                HdTransferBankStatement t = new HdTransferBankStatement();
                MyBeanUtils.copyBeanNotNull2Bean(hdNzbankAccount, t);
                t.setId(UUIDUtils.generateUuid());
                t.setMoney(hdNzbankAccount.getMoney().multiply(new BigDecimal(-1)));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                t.setCreateTime(sdf.parse(sdf.format(new Date())));
                t.setRemark("用于" + sdf.format(hdNzbankAccount.getCreateTime()) + ",凭证编号为：" + hdNzbankAccount.getNo() + "的账务冲账！");
                hdTransferBankStatementBiz.insertEntity(t);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
            message = "内转账单冲账失败";
            return R.failed(message);
        }
        message = "您成功冲账" + count + "条记录！";
        return R.ok(message);
    }



    //修改
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.PUT}
    )
    @ResponseBody
    @ApiOperation("更新单个对象*")
    public R update(@RequestBody HdTransferBankStatement entity) {
        entity.setCreateTime(new Date());
        hdTransferBankStatementBiz.updateAccount(entity);
        return R.ok("更新成功");
    }



    //添加
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象*")
    public  R<HdTransferBankStatement> add(@RequestBody HdTransferBankStatement entity){
        //插入内转账单
//        entity.setCrtUserId(BaseContextHandler.getUserID());
        entity.setTenantId(TenantContextHolder.getTenantId());
        entity.setCreateTime(new Date());
        String id = UUIDUtils.generateUuid();
        entity.setId(id);
        hdTransferBankStatementBiz.insertEntity(entity);
        return  R.ok(entity,"保存成功");
//        return new ObjectRestResponse<HdNzbankAccount>(200,"保存成功").data(entity);
    }




    //删除
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE}
    )
    @ResponseBody
    @ApiOperation("删除对象*")
    public R remove(@PathVariable String id){
        if(StringUtil.isEmpty(id)){
            return R.failed("参数传递失败");
        }
        try{
            hdTransferBankStatementBiz.deleteAccount(id);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("删除失败");
        }
        return R.ok("删除成功");
    }



    //批量删除
    @RequestMapping(value = "doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("批量删除对象*")
    public R doBatchDel(String ids){
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try{
            for(String id:ids.split(",")){
                hdTransferBankStatementBiz.deleteAccount(id);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("删除失败");
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }




    //导入
    @ApiOperation("导入*")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        List<HdTransferBankStatement> hdNzbankAccounts;
        ExcelUtil excelUtil = new ExcelUtil<HdTransferBankStatement>();
        String subject;
        try {
            hdNzbankAccounts = excelUtil.importXls(file,HdTransferBankStatement.class);
            int count = 0;
            if(hdNzbankAccounts!=null&&hdNzbankAccounts.size()>0){
                for(HdTransferBankStatement hdNzbankAccount:hdNzbankAccounts){
                    count++;
                    if(StringUtil.isEmpty(hdNzbankAccount.getRemark())){
                        return R.failed("第" + count + "条数据摘要为空！");
                    }
                    if(StringUtil.isEmpty(hdNzbankAccount.getIncomeSubjectName())){
                        return R.failed("第" + count + "条数据借方公司为空");
                    }
                    subject = hdTransferBankStatementBiz.getSubject(hdNzbankAccount.getIncomeSubjectName());
                    if(subject==null){
                        return R.failed("第" + count + "条数据借方公司系统无法匹配！");
                    }
                    if(StringUtil.isEmpty(hdNzbankAccount.getPaySubjectName())){
                        return R.failed("第" + count + "条数据贷方公司为空！");
                    }
                    hdNzbankAccount.setIncomeSubjectId(subject);
                    subject = hdTransferBankStatementBiz.getSubject(hdNzbankAccount.getPaySubjectName());
                    if(subject==null){
                        return R.failed("第" + count + "条数据贷方公司系统无法匹配！");
                    }
                    hdNzbankAccount.setPaySubjectId(subject);
                    //开户行匹配
                    if(StringUtil.isEmpty(hdNzbankAccount.getBankAccountId())){
                        return R.failed("第" + count + "条数据缺少开户行！");
                    }
                    if(hdNzbankAccount.getMoney()==null){
                        return R.failed("第" + count + "条数据请确认交易金额！");
                    }

                    HdBankAccount hdNzDict = hdBankAccountController.getNzDictByCode(hdNzbankAccount.getBankAccountCode());
                    if(hdNzDict==null){
                        return R.failed("第" + count + "条数据开户行系统无法匹配！");
                    }
                    hdNzbankAccount.setBankAccountId(hdNzDict.getId());
                    //插入时添加no
                    hdNzbankAccount.setId(UUIDUtils.generateUuid());
                    hdNzbankAccount.setCreateTime(new Date());
                }
                hdTransferBankStatementBiz.batchSave(hdNzbankAccounts);
            }
            //公司和开户行抬头系统内有
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入出错,请检查数字单元格是否为数值格式");
        }
        return R.ok("导入成功");
    }





    //导出
    @ApiOperation("导出*")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam Map<String,Object> params) throws ParseException {

        List<HdTransferBankStatement> allNzbankAccountList = new ArrayList<>();
        allNzbankAccountList = hdTransferBankStatementFeign.getNzbankAccountListAll(params);

        hdTransferBankStatementBiz.formatAllNo(allNzbankAccountList);
        ExcelUtil excelUtil = new ExcelUtil<HdTransferBankStatement>();
        try {
            excelUtil.print("内转账单记录列表", HdTransferBankStatement.class, "内转账单记录列表", "导出时间:" + jjUtil.getDateStr(), "内转账单记录列表", allNzbankAccountList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //列表查看
    @ApiOperation(value = "分页获取数据2*")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public R page(@RequestParam @ApiParam(value = "参数名称(除了dateStart,dataEnd)") Map<String, Object> params) {
        long total = 0;
        List<HdTransferBankStatement> hdNzbankAccountList = hdTransferBankStatementFeign.getNzbankAccountList(params);
        hdTransferBankStatementBiz.formatAllNo(hdNzbankAccountList);
        Map<String,Object> totalNum = hdTransferBankStatementFeign.getNzbankAccountCount(params);
        if(totalNum!=null){
            total = Integer.valueOf(totalNum.get("total").toString());
        }
        IPage iPage  = new Page();
        iPage.setRecords(hdNzbankAccountList);
        iPage.setTotal(total);
        return R.ok(iPage);
    }





    //批量打印获取
    @ApiOperation(value = "获取打印列表*")
    @RequestMapping(value = "/getPrintList", method = RequestMethod.GET)
    @ResponseBody
    public R<List<HdNzbankAccountDY>> getPrintList(String ids){
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        List<HdNzbankAccountDY> hdNzbankAccountList = new ArrayList<>();
        List<String> result;
        try{
            for(String id:ids.split(",")){
                HdTransferBankStatement hdNzbankAccount = hdTransferBankStatementFeign.selectById(id).getData();
                if(hdNzbankAccount!=null){
                    //根据id获取名称
                    if(StringUtil.isNotEmpty(hdNzbankAccount.getIncomeSubjectId())){
                        result = hdTransferBankStatementBiz.packNameAndCodeById(hdNzbankAccount.getIncomeSubjectId());
                        if(result!=null&&result.size()>0){
                            hdNzbankAccount.setIncomeSubjectId(result.get(0));
                            hdNzbankAccount.setIncomesubjectNo(result.get(1));
                        }
                    }
                    if(StringUtil.isNotEmpty(hdNzbankAccount.getPaySubjectId())){
                        result = hdTransferBankStatementBiz.packNameAndCodeById(hdNzbankAccount.getPaySubjectId());
                        if(result!=null&&result.size()>0){
                            hdNzbankAccount.setPaySubjectId(result.get(0));
                            hdNzbankAccount.setPaysubjectNo(result.get(1));
                        }
                    }
//                if(StringUtil.isNotEmpty(hdNzbankAccount.getBankAccount())){
//                    hdNzbankAccount.setBankno(baseBiz.packDictIdAndName(hdNzbankAccount.getBankno()));
//                }
                    hdTransferBankStatementBiz.formatNo(hdNzbankAccount,6);
                    HdNzbankAccountDY hdNzbankAccountDY = new HdNzbankAccountDY();

                    try {
                        MyBeanUtils.copyBeanNotNull2Bean(hdNzbankAccount, hdNzbankAccountDY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //补充添加wb账户和银行编码
                    if(StringUtil.isNotEmpty(hdNzbankAccount.getBankAccountId())){
                        hdNzbankAccountDY.setBankWbno(hdTransferBankStatementBiz.packNzno(hdNzbankAccount.getBankAccountId()));
                        hdNzbankAccountDY.setBankno(hdTransferBankStatementBiz.packDictIdAndName(hdNzbankAccount.getBankAccountId()));
                    }

                    if(hdNzbankAccountDY.getMoney()!=null){
                        String money = hdNzbankAccountDY.getMoney().toString();
                        List<Character> c = new ArrayList<>();
                        c.add('￥');
                        for(int i=0;i<money.length();i++){
                            c.add(money.charAt(i));
                            System.out.println(money.charAt(i));
                        }
                        hdNzbankAccountDY.setMoneyChar(c);
                    }
//                    if(StringUtil.isNotEmpty(hdNzbankAccountDY.getCrtUserId())){
//                        String userId = hdNzbankAccountDY.getCrtUserId();
////                        User user = iUserFeign.getById(userId);
////                        if(user!=null){
////                            hdNzbankAccountDY.setCrtUserId(user.getName());
////                        }else{
////                            hdNzbankAccountDY.setCrtUserId(BaseContextHandler.getName());
////                        }
//                    }else{
//                        hdNzbankAccountDY.setCrtUserId(BaseContextHandler.getName());
//                    }
                    //获取租户名称
                    //Tenant tenant = iUserFeign.getTenantById(hdNzbankAccountDY.getTenantId()).getData();
                    hdNzbankAccountDY.setTenantId(TenantContextHolder.getTenantId().toString());
                    HdBankTenant hdBankTenant = hdBankTenantBiz.getBankNameByTenant(hdNzbankAccountDY.getTenantId());
                    if(hdBankTenant!=null){
                        hdNzbankAccountDY.setHeadName(hdBankTenant.getTenantName());
                    }
                    hdNzbankAccountList.add(hdNzbankAccountDY);
                }
            }
        }catch (Exception e){
            return R.failed("获取失败");
        }
        return R.ok(hdNzbankAccountList,"获取成功");
//        return new ObjectRestResponse<>(200,"获取成功").data(hdNzbankAccountList);
    }





    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询单个对象")
    public R<HdTransferBankStatement> get(@PathVariable String id) {
        R<HdTransferBankStatement> entityObjectRestResponse = new R();
        HdTransferBankStatement o = hdTransferBankStatementFeign.selectById(id).getData();
        //根据id获取名称
        if(StringUtil.isNotEmpty(o.getIncomeSubjectName())){
            o.setIncomeSubjectId(hdTransferBankStatementBiz.packComIdAndName(o.getIncomeSubjectName()));
        }
        if(StringUtil.isNotEmpty(o.getPaySubjectName())){
            o.setPaySubjectId(hdTransferBankStatementBiz.packComIdAndName(o.getPaySubjectName()));
        }
//        if(StringUtil.isNotEmpty(o.getBankno())){
//            o.setBanknoId(baseBiz.packDictIdAndName(o.getBankno()));
//        }
        if(StringUtil.isNotEmpty(o.getBankAccountId())){
            o.setBanknoId(hdTransferBankStatementBiz.packDictIdAndName(o.getBankAccountId()));
        }
        entityObjectRestResponse.setData(o);
        return entityObjectRestResponse;
    }





}
