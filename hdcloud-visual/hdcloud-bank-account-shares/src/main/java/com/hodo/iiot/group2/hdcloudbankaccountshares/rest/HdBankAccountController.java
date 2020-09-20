package com.hodo.iiot.group2.hdcloudbankaccountshares.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankAccountBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.biz.HdBankStatementGfBiz;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankAccountFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankTenantFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.ExcelUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.StringUtil;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("hdBankAccount")
@Api(tags = "银行管理模块")
public class HdBankAccountController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    private HdBankAccountBiz hdBankAccountBiz;

    @Autowired
    HdBankAccountFeign hdBankAccountFeign;

    @Autowired
    HdBankTenantFeign hdBankTenantFeign;

    @Autowired
    private HdBankStatementGfBiz hdBankStatementGfBiz;


    //重寫插入，判斷是否内转外转重复
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public R add(@RequestBody HdBankAccount entity) {
        int b;
        try {
            entity.setCreateTime(new Date());
            b = hdBankAccountFeign.insertSelective(entity).getCode();
            if (1 == 1) {
            }
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return R.failed("请确认账号编号无重复");
        }
        if(b==0){
            return R.ok("录入成功");
        }else {
            return R.failed("请确认账号编号无重复");
        }
    }


    //移除单个对象
    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.DELETE}
    )
    @ResponseBody
    @ApiOperation("移除单个对象")
    public R remove(@PathVariable String id) {
        List<HdBankStatement> hdBankStatements = hdBankStatementGfBiz.getAccountsByBank(id);
        if(hdBankStatements!=null&&hdBankStatements.size()>0){
            return R.failed(500,"该账户下有账单信息,删除会导致统计错误！");
        }
        hdBankAccountFeign.deleteById(id);
        return R.ok("删除成功");
    }


    /**
     * 批量删除银行账户
     *
     * @return
     */
    @ApiOperation("批量删除公司名匹配表")
    @RequestMapping(value = "/doBatchDel", method = RequestMethod.POST)
    @ResponseBody
    public R doBatchDel(@RequestBody  String map) {
        JSONObject jo = JSONObject.parseObject(map,JSONObject.class);
        String ids = jo.getString("ids");
        if (StringUtil.isEmpty(ids)) {
            return R.failed(1003, "参数传递失败");
        }
        String message = null;
        int count = 0;
        try {
            for (String id : ids.split(",")) {
                List<HdBankStatement> hdBankStatements = hdBankStatementGfBiz.getAccountsByBank(id);
                if(hdBankStatements!=null&&hdBankStatements.size()>0){
                    return R.failed(500,"该账户下有账单信息,删除会导致统计错误！");
                }
                hdBankAccountFeign.deleteById(id);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "银行账户删除失败";
            return R.failed(500, message);
        }
        message = "您成功删除" + count + "条";
        return R.ok(message);
    }



    //重写查询加入银行名称模糊查询

    @ApiOperation("分页获取数据2")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public R list(@RequestParam Map<String,Object> params){
        try{
            String pageNumber = jjUtil.handleParams(params, "page");
            String pageSize = jjUtil.handleParams(params, "limit");
            if("".equals(pageNumber)){ params.put("page",pageNumber);}
            if("".equals(pageSize)){ params.put("limit",pageSize);}
        }catch (Exception e){
            return R.failed("数据获取异常");
        }

        return hdBankAccountFeign.getHdBankAccountPage(params);
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
                          String bank,String nzid,String bankType,String wbzh,String bankCode) throws ParseException {

        Map<String, Object> map = new HashedMap();
        map.put("bankCodeLike",bankCode);
        map.put("bankTypeLike",bankType);
        map.put("bankNameLike",bank);
        map.put("internalBankAccountIdLike",nzid);
        map.put("externalBankAccountIdLike",wbzh);
        List<HdBankAccount> allBankAccountEx = hdBankAccountFeign.getHdBankAccountList(map);
        ExcelUtil excelUtil = new ExcelUtil<HdBankAccount>();
        if(allBankAccountEx!=null&&allBankAccountEx.size()>0){
            for(HdBankAccount hdNzDict:allBankAccountEx){
                HdBankAccount hdBank = hdBankAccountFeign.selectById(hdNzDict.getBankName()).getData();
                if(hdBank!=null){
                    hdNzDict.setBankName(hdBank.getBankName());
                    hdNzDict.setBankCode(hdBank.getBankCode());
                }
                if ("0".equals(hdNzDict.getBankType())) {
                    hdNzDict.setBankType("一般户");
                } else if ("1".equals(hdNzDict.getBankType())) {
                    hdNzDict.setBankType("专户");
                } else if ("2".equals(hdNzDict.getBankType())) {
                    hdNzDict.setBankType("理财户");
                } else if ("3".equals(hdNzDict.getBankType())){
                    hdNzDict.setBankType("基本户");
                } else if("4".equals(hdNzDict.getBankType())){
                    hdNzDict.setBankType("贷款户");
                }else{
                    hdNzDict.setBankType("其他");
                }
//                hdNzDict.setCreateBy(BaseContextHandler.getName());
            }
        }
        try {
            excelUtil.print("银行账户管理列表", HdBankAccount.class, "银行账户管理列表", "导出时间:" + jjUtil.getDateStr(), "银行账户管理列表", allBankAccountEx, request, response);
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
        //内转账号，外部账号，银行名称必填
        List<HdBankAccount> hdNzDicts;
        ExcelUtil excelUtil = new ExcelUtil<HdBankAccount>();
        try {
            hdNzDicts = excelUtil.importXls(file, HdBankAccount.class);
            int count = 0;
            if (hdNzDicts != null && hdNzDicts.size() > 0) {
                for (HdBankAccount hdNzDict : hdNzDicts) {
                    count++;
//                    if (StringUtil.isEmpty(hdNzDict.getNzid())) {
//                        return new ObjectRestResponse<>(1004, "第" + count + "条数据内转账号为空");
//                    }
                    if (StringUtil.isEmpty(hdNzDict.getExternalBankAccountId())) {
                        return R.failed("第" + count + "条数据外转账号为空");
                    }
                    if (StringUtil.isEmpty(hdNzDict.getBankCode())) {
                        return R.failed("第" + count + "条数据银行编号为空");
                    }
                    if (StringUtil.isEmpty(hdNzDict.getBankName())) {
                        return R.failed("第" + count + "条数据银行名称为空");
                    }
                    //testComName.add(hdNzDict.getBank());
                    //也没有说不能出现一个编号两个名称
                    //根据编号查名称,控制不了excel出现两个code一个名称
                    String bankCode = hdNzDict.getBankCode();
                    List<String> bankNameStrs = (List<String>) hdBankAccountFeign.searchBankByCode("bank_name","bank_code",bankCode);
                    if(bankNameStrs!=null&&bankNameStrs.size()>0){
                        hdNzDict.setBankName(bankNameStrs.get(0));
                    }
                    //7.4
//                    String hdBankId = hdBankBiz.getBankId(hdNzDict.getBankCode());
//                    if(hdBankId==null){
//                        return new ObjectRestResponse<>(1004, "第" + count + "条系统无法匹配银行名称");
//                    }
//                    hdNzDict.setBank(hdBankId);
                    //6.25
                    //根据bankType获取信息
                    if(hdNzDict.getBankType()!=null&&StringUtil.isNotEmpty(hdNzDict.getBankType())) {
                        if ("一般户".equals(hdNzDict.getBankType())) {
                            hdNzDict.setBankType("0");
                        } else if ("专户".equals(hdNzDict.getBankType())) {
                            hdNzDict.setBankType("1");
                        } else if ("理财户".equals(hdNzDict.getBankType())) {
                            hdNzDict.setBankType("2");
                        } else if ("基本户".equals(hdNzDict.getBankType())){
                            hdNzDict.setBankType("3");
                        } else if("贷款户".equals(hdNzDict.getBankType())){
                            hdNzDict.setBankType("4");
                        } else {
                            return R.failed("第" + count + "条银行类型错误,只能为(一般户,专户,理财户,基本户,贷款户)");
                        }
                    }
                    //hdNzDict.setCreateBy(BaseContextHandler.getUserID());
//                    if(StringUtil.isEmpty(hdNzDict.getCreateBy())){
//                        return new ObjectRestResponse<>(1004, "第" + count + "条数据负责人为空");
//                    }
//                    User user = iUserFeign.getByUsername(hdNzDict.getCreateBy());
//                    if(user==null){
//                        return new ObjectRestResponse<>(1004, "第" + count + "条数据负责人系统无法识别");
//                    }
//                    hdNzDict.setCreateBy(BaseContextHandler.getUserID());
//                    hdNzDict.setCreateTime(new Date());
                }
                hdBankAccountFeign.batchSave(hdNzDicts);
            }

        } catch (DuplicateKeyException e) {
            return R.failed("请确认账号编号无重复");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("导入出错");
        }
        return R.ok("导入成功");
    }



    //添加录入人条件6.24
    //根据id获取账号匹配实体类
    @ApiOperation("获取账号匹配实体类")
    @RequestMapping(value = "/getBankAccountById", method = RequestMethod.GET)
    public HdBankAccount getNzDictById(String id){
        if(StringUtil.isEmpty(id)){
            return null;
        }
        return hdBankAccountFeign.selectById(id).getData();
    }




    @ApiOperation("获取账号匹配实体类")
    @RequestMapping(value = "/getNzDictByName", method = RequestMethod.GET)
    public R<List<HdBankAccount>> getNzDictByName(@RequestParam("name") String name){
        if(StringUtil.isEmpty(name)){
            return null;
        }

        Map<String, Object> map = new HashedMap();
        map.put("externalBankName",name);
        List<HdBankAccount> hdBankAccounts = hdBankAccountFeign.getHdBankAccountList(map);
//        hdNzDict.setCreateBy(TenantContextHolder.getUserID());
//        hdNzDict.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(hdBankAccounts);
    }



    @ApiOperation("获取账号匹配实体类")
    @RequestMapping(value = "/getNzDictByCode", method = RequestMethod.GET)
    public HdBankAccount getNzDictByCode(@RequestParam("code") String code){
        if(StringUtil.isEmpty(code)){
            return null;
        }
        Map<String, Object> map = new HashedMap();
        map.put("externalBankAccountId",code);
        //hdNzDict.setCreateBy(BaseContextHandler.getUserID());
//        hdNzDict.setTenantId(BaseContextHandler.getTenantID());

        HdBankAccount hdBankAccount = hdBankAccountFeign.getHdBankAccountList(map).get(0);

        if(hdBankAccount!=null){
            return hdBankAccount;
        }
        return null;
    }



    /**
     * 增加获取内转账单开户行列表
     */
    @ApiOperation("获取开户行接口*")
    @RequestMapping(value = "/getDictList", method = RequestMethod.GET)
    public R<Object> getDictList() {
        List<HdBankAccount> hdNzDictList = new ArrayList<>();
        Map<String, Object> map = new HashedMap();
        hdNzDictList = hdBankAccountFeign.getHdBankAccountList(map);
        if(hdNzDictList!=null&&hdNzDictList.size()>0){
            for(HdBankAccount hdNzDict:hdNzDictList){
                hdNzDict.setValue(hdNzDict.getBankName());
            }
        }
        return R.ok(hdNzDictList);
    }



    /**
     * 根据不同的抬头获取银行账户
     * @return
     */
    @ApiOperation("根据抬头获取开户行接口*")
    @RequestMapping(value = "/getDictListByHead", method = RequestMethod.GET)
    public R<Object> getDictList(@RequestParam  String tenantName) {
        //根据租户名称去中间表获取id

        Map<String,Object> mapname = new HashedMap();
        mapname.put("tenantName",tenantName);
        HdBankTenant hdBankTenant = hdBankTenantFeign.getHdTenantList(mapname).get(0);
        String tenantId = "";
        if(hdBankTenant!=null){
            tenantId = hdBankTenant.getTenantId().toString();
        }
//        String tenantId = (String) hdBankAccountFeign.getTenantIdByName(tenantName).getData();

        List<HdBankAccount> hdNzDictList = new ArrayList<>();
        Map<String, Object> map = new HashedMap();
        map.put("tenantId",tenantId);
        hdNzDictList = hdBankAccountFeign.getHdBankAccountList(map);
        if(hdNzDictList!=null&&hdNzDictList.size()>0){
            for(HdBankAccount hdNzDict:hdNzDictList){
                hdNzDict.setValue(hdNzDict.getBankName());
            }
        }
        return R.ok(hdNzDictList);
    }



    //根据银行id判断是否有关联账户，内部调用
    @RequestMapping(value = "/checkBankId", method = RequestMethod.GET)
    public Boolean checkBankId(String bankId){
        return hdBankAccountBiz.checkBankId(bankId);
    }



    @RequestMapping(
            value = {"/{id}"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询单个对象")
    public R<HdBankAccount> get(@PathVariable String id) {
        R<HdBankAccount> entityObjectRestResponse = new R();
        HdBankAccount o = hdBankAccountFeign.selectById(id).getData();
        if(o!=null){
//            String bankId = o.getBank();
//            HdBank hdBank = hdBankBiz.selectById(bankId);
//            if(hdBank!=null){
//                o.setBank(hdBank.getId()+","+hdBank.getName());
//            }
//            User user = new User();
            String userId = o.getCreateBy();
            try{
                if(StringUtil.isNotEmpty(userId)){
//                    user = iUserFeign.getById(userId);
                }
            }catch (Exception e){
                R.failed("数据获取异常");
                return entityObjectRestResponse;
            }
//            userId = jjUtil.handleUserId(userId,user);
            o.setCreateBy(userId);

        }
        entityObjectRestResponse.setData(o);
        return entityObjectRestResponse;
    }



    @RequestMapping(
            value = {""},
            method = {RequestMethod.PUT}
    )
    @ResponseBody
    @ApiOperation("更新单个对象")
    public R<HdBankAccount> update(@RequestBody HdBankAccount entity) {
        int b;
        try{
            b = hdBankAccountFeign.updateSelectiveById(entity).getCode();
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return R.failed("请确认账号编号无重复");
        }
        if(0==b){
            return R.ok(entity,"更新成功");
        }else {
            return R.failed("请确认账号编号无重复");
        }

    }




    //根据code查询是否已存在
    @RequestMapping(
            value = {"/searchBankByCode"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询银行名称根据编号")
    public R<Object> searchBankByCode(String bankCode){
        List<NzEntity> bankNames = new ArrayList<>();
        List<String> bankNameStrs = new ArrayList<>();
        try{
            bankNameStrs = hdBankAccountFeign.searchBankByCode("bank_name","bank_code",bankCode);
            if(bankNameStrs!=null&&bankNameStrs.size()>0){
                for(String bankNameStr : bankNameStrs){
                    NzEntity nzEntity = new NzEntity();
                    nzEntity.setValue(bankNameStr);
                    bankNames.add(nzEntity);
                }
                return R.ok(bankNames,"获取成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("数据异常");
        }
        return  R.ok(bankNames,"空字符串");
    }




    @RequestMapping(
            value = {"/searchBankByName"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("查询银行编号根据名称")
    public R<String> searchBankByName(String bankName){
        String bankCode = "";
        try{
            bankCode = hdBankAccountFeign.searchBankByName("bank_code","bank_name",bankName).get(0);
            if(StringUtil.isEmpty(bankCode)){
                return  R.ok("","空字符串");
            }
        }catch (Exception e){
            e.printStackTrace();
            return  R.failed("数据异常");
        }
        return  R.ok(bankCode,"获取成功");
    }



    //根据外部账户查询账户id
    @RequestMapping(
            value = {"/getNzDictByWbzh"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    @ApiOperation("根据外部账上查询id")
    public String getNzDictByWbzh(String externalBankAccountId){
        Map<String, Object> map = new HashedMap();
        map.put("externalBankAccountId",externalBankAccountId);
        List<HdBankAccount> list = hdBankAccountFeign.getHdBankAccountList(map);
        String id = list.get(0).getId();
        return id;
    }






}
