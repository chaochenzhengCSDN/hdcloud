package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.util.*;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.common.MR;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("hdbankstatement")
@Api(tags = "账单管理模块")
public class HdBankStatementGfController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    HdBankStatementGfBiz hdBankStatementGfBiz;

    @Autowired
    HdBankStatementGfFeign hdBankStatementGfFeign;

    @Autowired
    HdBankPendingBiz hdBankPendingBiz;

    @Autowired
    HdBankPendingFeign hdBankPendingFeign;

    @Autowired
    HdBankRecordBiz hdBankRecordBiz;

    @Autowired
    HdBankRecordFeign hdBankRecordFeign;

    @Autowired
    HdCompanyBiz hdCompanyBiz;

    @Autowired
    HdCompanyFeign hdCompanyFeign;

    @Autowired
    HdBankAccountBiz hdBankAccountBiz;

    @Autowired
    HdBankAccountFeign hdBankAccountFeign;





    /**
     * 手工帐
     * @param entity
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public R add(@RequestBody HdBankStatement entity) {
        int b = 1;
        //检查是否有公司，否则无法新增
        if(StringUtil.isEmpty(entity.getCompanyId())){
            return R.failed("公司未填写");
        }
        entity.setSourceType("4");
        entity.setAccountType("3");
        if(entity.getSynaccountDate()==null){
            entity.setSynaccountDate(new Date());
        }
        entity.setAccountDate(new Date());
        b = hdBankStatementGfBiz.insertEntity(entity);
        if(0==b){
            return R.ok("添加成功");
        }else{
            return R.failed("添加失败");
        }
    }




    //编辑记账日期为当天的手工帐
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.PUT}
    )
    @ResponseBody
    @ApiOperation("更新单个对象")
    public R update(@RequestBody HdBankStatement entity) {
        if(!jjUtil.sdf.format(entity.getAccountDate()).equals(jjUtil.getDateStr())){
            return R.failed("请选择当天记录操作");
        }
        if(!"3".equals(entity.getAccountType())){
            return R.failed("请选择手工帐操作");
        }
        hdBankStatementGfFeign.updateSelectiveById(entity);
        return R.ok("更新成功");
    }




    @RequestMapping(value = "/addNzAccounts", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增內部賬單对象")
    public R addNzAccount(@RequestParam("bankAccounts") String entitys) {
        if(entitys!=null){
            List<HdBankStatement> hdBankAccounts = new ArrayList<>();
            hdBankAccounts =  JSONObject.parseArray(entitys, HdBankStatement.class);
            if(hdBankAccounts!=null&&hdBankAccounts.size()>0){
                for(HdBankStatement hdBankAccount:hdBankAccounts){
                    hdBankAccount.setSynaccountDate(hdBankAccount.getAccountDate());
                }
                hdBankStatementGfBiz.batchSave(hdBankAccounts);
            }
        }
        return R.ok("添加成功");
    }




    //重写添加公司名称字段
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public R<HdBankStatement> get(@PathVariable String id) {
        R<HdBankStatement> entityObjectRestResponse = new R<>();
        HdBankStatement entity = hdBankStatementGfFeign.selectById(id).getData();
        //获取公司id,添加名称
        String companyId = entity.getCompanyId();
        if (StringUtil.isNotEmpty(companyId)) {
            String companyName = hdCompanyFeign.selectById(companyId).getData().getName();
            if (StringUtil.isNotEmpty(companyName)) {
                entity.setCompanyName(companyName);
            }
        }
        String accountId = entity.getBankAccountId();
        if(StringUtil.isNotEmpty(accountId)){
            //根据银行账户id获取外部账户
            HdBankAccount hdNzDict = hdBankAccountFeign.selectById(accountId).getData();
            if(hdNzDict!=null){
                entity.setBankAccountId(hdNzDict.getExternalBankAccountId());
                entity.setBankName(hdNzDict.getBankName());
            }
        }
        entityObjectRestResponse.setData(entity);
        return entityObjectRestResponse;
    }





    //重写分页用sql
    //添加录入人条件6.24
    @ApiOperation(value = "分页获取数据2")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public MR page(@RequestParam @ApiParam(value = "参数名称(company,page,limit,remark," +
            "dateStart,dataEnd,synDateStart,synDateEnd,accountId,accountType)") Map<String, Object> params) {
        long total = 0;
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        //2.1版本新增金额区间查询
        //增加到账日期，所属银行，账单类型
        params.put("page",page);
        params.put("limit",limit);

        List<HdBankStatement> hdBankAccountList = hdBankStatementGfFeign.getBankAccountListBySql(params);
        //加入银行账户8.6
        hdBankAccountList = hdBankStatementGfBiz.addBankAccount(hdBankAccountList);
        Map<String,Object> allOtherVal =  hdBankStatementGfFeign.getOtherVal(params);
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        if(allOtherVal!=null&&allOtherVal.size()>0){
            if(allOtherVal.get("income")!=null){
                sumIncome = new BigDecimal(allOtherVal.get("income").toString());
            }
            if(allOtherVal.get("pay")!=null){
                sumPay = new BigDecimal(allOtherVal.get("pay").toString());
            }
            if(allOtherVal.get("total")!=null){
                total = Integer.valueOf(allOtherVal.get("total").toString());
            }
        }
        return new MR(total, hdBankAccountList, sumIncome, sumPay);
    }




    /**
     * 银行财务冲账
     */
    @ApiOperation("银行财务冲账")
    @RequestMapping(value = "/doBatchCZ", method = RequestMethod.GET)
    @ResponseBody
    public R doBatchCZ(String ids) {
        String message = null;
        message = "银行账务信息冲账成功";
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                HdBankStatement hdBankAccount = hdBankStatementGfFeign.selectById(id).getData();
                HdBankStatement t = new HdBankStatement();
                MyBeanUtils.copyBeanNotNull2Bean(hdBankAccount, t);
                t.setId(UUIDUtils.generateUuid());
                t.setIncome(hdBankAccount.getIncome().multiply(new BigDecimal(-1)));
                t.setPay(hdBankAccount.getPay().multiply(new BigDecimal(-1)));
                t.setAccountDate(new Date());
                t.setSynaccountDate(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                t.setAccountDate(sdf.parse(sdf.format(new Date())));
                t.setRemark("用于" + sdf.format(hdBankAccount.getAccountDate()) + ",凭证编号为：" + hdBankAccount.getNo() + "的账务冲账！");
                //更新一条原有的，保存一条新冲账，保存一条待处理
                hdBankStatementGfBiz.insertEntity(t);
                count++;

            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行账务信息冲账失败";
            return R.ok(message);
        }
        message = "您成功冲账" + count + "条记录！";
        return R.ok(message);
    }




    //查询所有的摘要
    //添加录入人条件6.24
    @ApiOperation("查询所有的摘要")
    @RequestMapping(value = "/doSearch", method = RequestMethod.GET)
    @ResponseBody
    public R<String> doSearch() {
        String str = hdBankStatementGfBiz.Search();
        return R.ok(str);
    }



    /**
     * 获取系统当前时间
     *
     * @return
     */
    @ApiOperation("获取系统当前时间")
    @RequestMapping(value = "/doDate", method = RequestMethod.GET)
    @ResponseBody
    public R<String> doDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前时间
        String day = sdf.format(new Date());
        return R.ok(day);
    }




    /**
     * 批量删除银行账务信息表
     *
     * @return
     */
    @ApiOperation("批量删除银行账务信息表")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R<String> doBatchDel(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                hdBankStatementGfFeign.removeById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行账务信息表删除失败";
            return R.failed(message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }





    /**
     * 导出excel
     *
     * @throws ParseException
     */
    //添加录入人条件6.24
    @ApiOperation("导出")
    @RequestMapping(value = "/exportXls", method = RequestMethod.GET)
    @ResponseBody
    public void exportXls(@RequestParam Map<String, Object> params) throws ParseException {
        String tenantId = String.valueOf(TenantContextHolder.getTenantId());
        List<String> accountTypeList = null;
        List<HdBankStatement> allBankAccountList = hdBankStatementGfFeign.getAllBankAccountBySql(params);
        //加入银行账户8.6
        allBankAccountList = hdBankStatementGfBiz.addBankAccount(allBankAccountList);
        ExcelUtil excelUtil = new ExcelUtil<HdBankStatement>();
        try {
            excelUtil.print("银行账单记录列表", HdBankStatement.class, "银行账单记录列表","导出时间:" + jjUtil.getDateStr(), "银行账单记录列表", allBankAccountList, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    /**
     * 修改摘要
     */
    @ApiOperation("修改摘要")
    @RequestMapping(value = "/updateRemark", method = RequestMethod.POST)
    @ResponseBody
    public R updateRemark(@RequestBody String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String id = jo.getString("id");
        String remark = jo.getString("remark");

        if (StringUtil.isEmpty(id)) {
            return R.failed("参数传递失败");
        }
        if (StringUtil.isEmpty(remark)) {
            return R.failed("参数传递失败");
        }
        String message = null;
        message = "修改备注成功";
        try {
            HdBankStatement hdBankAccount = hdBankStatementGfFeign.selectById(id).getData();
            hdBankAccount.setRemark(remark);
            hdBankAccount.setCreateBy(BaseContextHandler.getUserID());
            hdBankStatementGfFeign.updateSelectiveById(hdBankAccount);
        } catch (Exception e) {
            e.printStackTrace();
            message = "修改备注失败";
            return R.failed(message);
        }

        return R.ok(message);
    }






    //添加录入人条件6.24
    /**
     * 同步功能
     */
    @RequestMapping(value = "/synData", method = RequestMethod.GET)
    @ResponseBody
    public R<Map<String, Object>> synData(String query_begin, String query_end, String query_to) {
        Map<String, Object> result = new HashedMap();
        int synCount = 0;
        Integer due = 0;
        Integer saveCount = 0;
        try{
            Integer saveCountBefore = hdBankStatementGfFeign.getSaveCount(query_begin, query_end,TenantContextHolder.getTenantId());
            Date accountDay = null;
            if (StringUtil.isNotEmpty(query_to)) {
                try {
                    System.out.println("同步至:"+query_to);
                    accountDay = DateUtils.date_sdf.parse(query_to);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return R.failed("同步至某日日期格式错误");
                }
            }
            List<HdBankStatement> accountList = new ArrayList<>();
            List<HdBankPending> pendingList = new ArrayList<>();
            //查询本地所有的sheetid,查询所有的rid
            List<String> sheetIdInBankAccount = hdBankStatementGfFeign.getSheetIdsInBankAccount() != null ? hdBankStatementGfFeign.getSheetIdsInBankAccount() :
                    new ArrayList<>();
            List<String> sheetIdInBankPend = hdBankStatementGfFeign.getSheetIdsInBankPend() != null ? hdBankStatementGfFeign.getSheetIdsInBankPend() :
                    new ArrayList<>();
            List<String> rIdInBankAccount = hdBankStatementGfFeign.getRIdsInBankAccount() != null ? hdBankStatementGfFeign.getRIdsInBankAccount() :
                    new ArrayList<>();
            List<String> rIdInBankPend = hdBankStatementGfFeign.getRIdsInBankPend() != null ? hdBankStatementGfFeign.getRIdsInBankPend() :
                    new ArrayList<>();
            hdBankStatementGfBiz.synNZData(accountList, pendingList, sheetIdInBankAccount,
                    sheetIdInBankPend, query_begin, query_end);
            //一笔生成两笔需要相加除以2
            due = (accountList.size() + pendingList.size()) / 2;
            System.out.println(accountList.size() + "------互转" + pendingList.size());
            hdBankStatementGfBiz.synNZIncome(accountList, pendingList, sheetIdInBankAccount,
                    sheetIdInBankPend, query_begin, query_end);
            System.out.println(accountList.size() + "------内转外" + pendingList.size());
            hdBankStatementGfBiz.synNZPay(accountList, pendingList, sheetIdInBankAccount,
                    sheetIdInBankPend, query_begin, query_end);
            System.out.println(accountList.size() + "------外转内" + pendingList.size());

            hdBankStatementGfBiz.synWZ(accountList, pendingList, rIdInBankAccount,
                    rIdInBankPend, query_begin, query_end);
            System.out.println(accountList.size() + "------外转" + pendingList.size());

            //System.out.println(accountList.size()+"------4"+pendingList.size());
            //int saveCount=0;
            System.out.println("賬單保存開始");
            if (accountList != null && accountList.size() > 0) {
                synCount = accountList.size();
                //重新封裝賬單加序號
                //按照時間排個序
                Collections.sort(accountList, new Comparator<HdBankStatement>() {
                    @Override
                    public int compare(HdBankStatement o1, HdBankStatement o2) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date dt1 = o1.getAccountDate();
                            Date dt2 = o2.getAccountDate();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                hdBankStatementGfBiz.handBankAccountList(accountList, accountDay);
                hdBankStatementGfBiz.batchSave(accountList);

//            for(HdBankAccount hdBankAccount:accountList){
//                try{
//                    if(accountDay!=null){
//                        hdBankAccount.setAccountDate(accountDay);
//                    }
//                    baseBiz.insertEntity(hdBankAccount);
//                    saveCount++;
//                }catch (Exception e){
//                    System.out.println("錯誤信息"+e.getMessage());
//                    e.printStackTrace();
//                    continue;
//                }
//            }
            }
            //System.out.println(synCount+"-----------"+saveCount+"待處理保存開始:");
            if (pendingList != null && pendingList.size() > 0) {
                synCount += pendingList.size();
                for (HdBankPending hdBankPending : pendingList) {
//                try{
                    //添加用户
                    String userId = BaseContextHandler.getUserID();
                    hdBankPending.setCreateBy(userId);
                    if (accountDay != null) {
                        hdBankPending.setAccountDate(accountDay);
                    }
                    EntityUtils.setCreatAndUpdatInfo(hdBankPending);
                    //hdBankPendingBiz.insertSelective(hdBankPending);
                    //saveCount++;
//                }catch (Exception e){
//                    System.out.println("待處理錯誤信息"+e.getMessage());
//                    e.printStackTrace();
//                    continue;
//                }
                }
                hdBankPendingBiz.batchSave(pendingList);
            }
//        return new ObjectRestResponse<>(200,"总获取数量"+num+
//                "同步成功数量"+successNum+"用时:"+(end-start)+"毫秒");
            //同步后
            Integer saveCountAfter = hdBankStatementGfBiz.getSaveCount(query_begin, query_end);
            System.out.println(saveCountAfter + "@@@@@@@@@@" + synCount);
            //实际同步数量
            saveCount = saveCountAfter - due - saveCountBefore;
            if (synCount == 0) {
                saveCount = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed(result,"同步失败");
        }
        result.put("synCount", synCount - due);
        result.put("save", saveCount);
        return R.ok(result,"同步成功");
//        return new ObjectRestResponse<>(200, "同步成功").data(result);
    }





    /*********************************标准版本二次修改*********************************/
    //修改账单日期
    @ApiOperation("新修改账单日期")
    @RequestMapping(value = "/changeAccountDate", method = RequestMethod.POST)
    @ResponseBody
    public R changeAccountDate(@ApiParam("请求参数") String ids,String accountDate){
        if(StringUtil.isEmpty(ids)){
            return R.failed("参数传递失败");
        }
        if(StringUtil.isEmpty(accountDate)){
            return R.failed("参数传递失败");
        }
        List<HdBankRecord> hdBankRecordList = new ArrayList<>();
        List<HdBankStatement> hdBankAccountList = new ArrayList<>();
        Date accountDay = null;
        String remark = "";
        int count = 0;
        if (StringUtil.isNotEmpty(accountDate)) {
            try {
                accountDay = DateUtils.date_sdf.parse(accountDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return R.failed("同步至某日日期格式错误");
            }
        }
        String[] idStrs = ids.split(",");
        if(idStrs.length>0){
            for(String id : idStrs){
                count++;
                HdBankStatement hdBankAccount = hdBankStatementGfFeign.selectById(id).getData();
                //修改记账日期，同时修改摘要，修改编号，记录操作日志
                if(hdBankAccount==null){
                    return R.failed("第"+count+"条数据获取异常");
                }
                //比较时间
                if(accountDay.before(hdBankAccount.getAccountDate())){
                    return R.failed("第"+count+"条数据记账日期修改不得早于账单日期");
                }
                remark = hdBankAccount.getRemark();
                if(StringUtil.isNotEmpty(remark)&&remark.contains("原九恒星账单日期:")){
                }else{
                    remark = remark+";原九恒星账单日期:"+jjUtil.formatDate(hdBankAccount.getAccountDate());
                }
                if(StringUtil.isEmpty(remark)){
                    remark = remark+"原九恒星账单日期:"+jjUtil.formatDate(hdBankAccount.getAccountDate());
                }
                hdBankAccount.setAccountDate(accountDay);
                hdBankAccount.setCreateBy(BaseContextHandler.getUserID());
                hdBankAccount.setRemark(remark);
                //获取修改后日期的最大序号
                hdBankStatementGfBiz.getNoMaxFreeTime(hdBankAccount);

                HdBankRecord hdbankRecord = hdBankRecordBiz.getHdBankRecord(hdBankAccount,"账单修改九恒星日期");
                hdBankRecordList.add(hdbankRecord);
                hdBankAccountList.add(hdBankAccount);
            }
            hdBankStatementGfBiz.batchUpdate(hdBankAccountList);
            hdBankRecordBiz.batchSave(hdBankRecordList);
        }
        return R.ok("修改成功");
//        return new ObjectRestResponse<>(200,"修改成功");
    }




    //账单还原，只能单个还原，无法判断，银行和摘要无法匹配，如果是九恒星同步的可以还原吗，还原回去要一模一样吗
    //不用把摘要存起来，还原分账和账单一分为二的区别，分账的话相加肯定大于0，不是分账相加等于0
    @ApiOperation("新账单还原")
    @RequestMapping(value = "/returnAccount", method = RequestMethod.GET)
    @ResponseBody
    public R<String> returnAccount(@ApiParam("请求参数") String ids){
        List<HdBankStatement> fenAccounts = new ArrayList<>();
        List<HdBankStatement> huaAccounts = new ArrayList<>();
        String msg = "账单还原处理成功";
        if(StringUtil.isEmpty(ids)){
            return R.failed("获取参数失败");
        }
        try{
            for(String id : ids.split(",")){
                HdBankStatement hdBankAccount = hdBankStatementGfFeign.selectById(id).getData();
                if(hdBankAccount!=null){
                    if("1".equals(hdBankAccount.getSourceType())){
                        fenAccounts.add(hdBankAccount);
                    }
                    if("0".equals(hdBankAccount.getSourceType())||
                            "2".equals(hdBankAccount.getSourceType())){
                        huaAccounts.add(hdBankAccount);
                    }
                }
            }
            hdBankStatementGfBiz.handFenAccounts(fenAccounts);
            hdBankStatementGfBiz.handHuaAccounts(huaAccounts);
        }catch (Exception e){
            e.printStackTrace();
            msg = "有账单处理失败";
            return R.failed(msg);
        }
        return R.ok(msg);
    }




    @RequestMapping(value = "/deleteBySourceId", method = RequestMethod.POST)
    @ResponseBody
    public void deleteBySourceId(@RequestParam("sourceId") String sourceId){
        if(StringUtil.isNotEmpty(sourceId)){
            Map<String,Object> map = new HashedMap();
            map.put("sourceId",sourceId);
            hdBankStatementGfFeign.delete(map);
        }
    }




    /***********************同步excel**********************/

    /****************************新增导入功能*******************************/
    //导入
    @ApiOperation("导入*")
    @RequestMapping(value = "/importXls", method = RequestMethod.POST)
    @ResponseBody
    public R importXls(
            @RequestParam(value = "file", required = true) MultipartFile file
    ){
        List<HdBankStatement> hdBankAccountsEx;
        ExcelUtil excelUtil = new ExcelUtil<HdBankStatement>();
        try{
            hdBankAccountsEx = excelUtil.importXls(file, HdBankStatement.class);
            int count = 0;
            if(hdBankAccountsEx!=null&&hdBankAccountsEx.size()>0){
                //获取最大编号
                int maxNo = hdBankStatementGfBiz.getNoMaxCurrentTime();
                for(HdBankStatement hdBankAccountEx:hdBankAccountsEx){
                    count++;
                    //公司名必填
                    if(StringUtil.isEmpty(hdBankAccountEx.getCompanyName())){
                        return R.failed("第"+count+"条账单公司名称为空");
                    }
                    //对方单位名称必填
                    if(StringUtil.isEmpty(hdBankAccountEx.getSubjects())){
                        return R.failed("第"+count+"条账单对方单位名称为空");
                    }
                    //摘要必填
                    if(StringUtil.isEmpty(hdBankAccountEx.getRemark())){
                        return R.failed("第"+count+"条账单摘要为空");
                    }
                    //收入和支出不能同时为空
                    if(hdBankAccountEx.getIncome()==null){
                        hdBankAccountEx.setIncome(BigDecimal.ZERO);
                    }
                    if(hdBankAccountEx.getPay()==null){
                        hdBankAccountEx.setPay(BigDecimal.ZERO);
                    }
                    if(hdBankAccountEx.getIncome().compareTo(BigDecimal.ZERO)==0&&
                            hdBankAccountEx.getPay().compareTo(BigDecimal.ZERO)==0){
                        return R.failed("第"+count+"条账单收入或支出必须为有效值");
                    }
                    //银行账号必填
                    if(StringUtil.isEmpty(hdBankAccountEx.getBankAccountId())){
                        return R.failed("第"+count+"条账单银行账户为空");
                    }
                    //如果到账日期为空当前日期，记账日期为当前日期
                    if(hdBankAccountEx.getSynaccountDate()==null){
                        hdBankAccountEx.setSynaccountDate(new Date());
                    }
                    hdBankAccountEx.setAccountDate(new Date());
                    //凭证编号+1
                    hdBankAccountEx.setNo(new BigDecimal(maxNo));
                    hdBankAccountEx.setBy2(String.valueOf(maxNo));
                    //银行账号获取id同时获取银行名称
                    String accountId = hdBankAccountBiz.getNzDictByWbzh(hdBankAccountEx.getBankAccountId());
                    if(StringUtil.isEmpty(accountId)){
                        return R.failed("第"+count+"条账单银行账户无法匹配");
                    }
                    hdBankAccountEx.setBankAccountId(accountId);
                    HdBankAccount hdNzDict = hdBankAccountFeign.selectById(accountId).getData();
                    if(hdNzDict!=null){
                        hdBankAccountEx.setBankName(hdNzDict.getBankName());
                    }
                    //公司名称转id
                    Map<String,Object> map = new HashedMap();
                    map.put("name",hdBankAccountEx.getCompanyId());
                    HdCompany hdCompany = hdCompanyFeign.getHdCompanyList(map).get(0);
                    if(hdCompany==null){
                        return R.failed("第"+count+"条账单公司系统无法匹配");
                    }
                    hdBankAccountEx.setCompanyId(hdCompany.getId());
                    //账单类型为手工帐
                    hdBankAccountEx.setAccountType("3");
                    hdBankAccountEx.setSourceType("4");
//                    hdBankAccountEx.setCreateBy(BaseContextHandler.getUserID());
                    hdBankAccountEx.setTenantId(TenantContextHolder.getTenantId());
                    maxNo++;
                }
                hdBankStatementGfBiz.batchSave(hdBankAccountsEx);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("导入失败,请检查导入格式是否正确");
        }
        return R.ok("导入成功");
    }





}
