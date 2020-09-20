package com.hodo.iiot.group2.hdcloud.bank.account.currency.feign;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.entity.*;
import io.swagger.models.auth.In;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019-11-05.
 */
@FeignClient(contextId = "remoteBaseService", value = ServiceNameConstants.BASE_DATA)
public interface RemoteBaseService {
    /**************************************账单**************************************/
    //根據租戶獲取
    @RequestMapping(value = "/hdbankaccount/all", method = RequestMethod.GET)
    List<HdBankAccount> selectListAllByTenant(@RequestParam() Map<String,Object> params);
    @RequestMapping(value = "/hdbankaccount/page", method = RequestMethod.GET)
    R pageBankAccount(@RequestParam() Map<String,Object> params);
    //获取条件账户
    @RequestMapping(value = "/hdbankaccount/selectOne", method = RequestMethod.GET)
    HdBankAccount getAccountByTerm(@RequestParam("internalBankAccountId")String nzId, @RequestParam("externalBankAccountId")String wbzh);
    //获取所有账户不加条件
    @RequestMapping(value = "/hdbankaccount/all2", method = RequestMethod.GET)
    List<HdBankAccount> selectListAll(@RequestParam() Map<String,Object> params);

    //批量保存银行账户
    @RequestMapping(value = "/hdbankaccount/addList", method = RequestMethod.POST)
    R addBankAccountList(@RequestBody List<HdBankAccount> hdBankAccountList);
    @RequestMapping(value = "/hdbankaccount/searchBankByCode", method = RequestMethod.GET)
    List<String> searchBankByCode(@RequestParam("bankCode") String bankCode);
    //查询id
    @GetMapping("/hdbankaccount/{id}")
    public R<HdBankAccount> getBankAccountById(@PathVariable("id") Serializable id);
    //编辑
    @PutMapping("/hdbankaccount")
    public R updateBankAccountById(@RequestBody HdBankAccount entity);
    //增加
    @PostMapping("/hdbankaccount")
    public R saveBankAccount(@RequestBody HdBankAccount entity);
    //删除
    @DeleteMapping("/hdbankaccount/{id}")
    public R removeBankAccountById(@PathVariable("id") Serializable id);
    @GetMapping("/hdbankaccount/getAccountsByComId")
    public List<HashMap> getAccountsByComId(@RequestParam("compId") Serializable compId);

    @GetMapping("/hdbankaccount/getAccountsByMemberComId")
    public List<HashMap> getAccountsByMemberComId(@RequestParam("memberCompId") String compId);
    /**************************************成员单位**************************************/
    //成员单位
    @RequestMapping(value = "/hdcompanymember/page", method = RequestMethod.GET)
    R pageMemberCompany(@RequestParam Map<String,Object> params);
    @RequestMapping(value = "/hdcompanymember/selectOne", method = RequestMethod.GET)
    HdCompanyMember getMembercomByCode(@RequestParam("code")String code);
    //批量保存
    @RequestMapping(value = "/hdcompanymember/addList", method = RequestMethod.POST)
    public R addCompanyMemberList(@RequestBody List<HdCompanyMember> hdCompanyMemberList);
    @RequestMapping(value = "/hdcompanymember/all", method = RequestMethod.GET)
    public List<HdCompanyMember> getAllMemberCompany(@RequestParam() Map<String,Object> params);
    @PostMapping(value = "/hdcompanymember")
    public R addMemberCompany(@RequestBody HdCompanyMember entity);
    @RequestMapping(value = "/hdcompanymember/{id}", method = RequestMethod.GET)
    public R<HdCompanyMember> getMemberCompanyById(@PathVariable("id") Serializable id);
    @PutMapping(value = "/hdcompanymember")
    public R updateMemberCompanyById(@RequestBody HdCompanyMember entity);
    @DeleteMapping("/hdcompanymember/{id}")
    public R removeMemberCompanyById(@PathVariable("id") Serializable id);
    @PostMapping(value = "/hdcompanymember/removeByIds")
    public R removeCompanyMemberByIds(@RequestBody List<String> ids);
    /**************************************公司**************************************/
    //公司模块
    //查询id
    @RequestMapping(value = "/hdcompany/{id}", method = RequestMethod.GET)
    public R<HdCompany> getCompanyById(@PathVariable("id") Serializable id);
    //编辑
    @PutMapping(value = "/hdcompany")
    public R updateCompanyById(@RequestBody HdCompany entity);
    //增加
    @RequestMapping(value = "/hdcompany", method = RequestMethod.POST)
    public R addCompany(@RequestBody HdCompany entity);
    //删除
    @DeleteMapping("/hdcompany/{id}")
    public R removeCompanyById(@PathVariable("id") Serializable id);
    //批量添加公司
    @RequestMapping(value = "/hdcompany/addList", method = RequestMethod.POST)
    public R addCompanyList(@RequestBody List<HdCompany> companyList);
    @RequestMapping(value = "/hdcompany/all", method = RequestMethod.GET)
    public List<HdCompany> getAllCompany(@RequestParam() Map<String,Object> params);
    @RequestMapping(value = "/hdcompany/page", method = RequestMethod.GET)
    R pageCompany(@RequestParam() Map<String,Object> params);
    @PostMapping(value = "/hdcompany/removeByIds")
    public R removeCompanyByIds(@RequestBody List<String> ids);

    /**************************************银行匹配**************************************/
    //银行匹配
    //增加
    @PostMapping("/hdmatchcompanymember")
    public R addMatchCompanyMember(@RequestBody HdMatchCompanyMember hdMatchCompanyMember);
    //刪除
    @DeleteMapping("/hdmatchcompanymember/{id}")
    public R removeMatchCompanyMemberById(@PathVariable("id") Serializable id);
    //更新
    @PutMapping("/hdmatchcompanymember")
    public R updateMatchCompanyMemberById(@RequestBody HdMatchCompanyMember entity);
    @RequestMapping(value = "/hdmatchcompanymember/page2", method = RequestMethod.GET)
    List<HdMatchCompanyMember> pageMatchCompanyMember(@RequestParam() Map<String,Object> params);
    //获取其他总计
    @RequestMapping(value = "/hdmatchcompanymember/other", method = RequestMethod.GET)
    public Map<String,Object> getMatchCompanyMemberOtherVal(@RequestParam() Map<String,Object> params);
    //根据外部抬头获取匹配公司信息

    @RequestMapping(value = "/hdmatchcompanymember/all", method = RequestMethod.GET)
    public List<HdMatchCompanyMember> getMatchCompanyByTerm(@RequestParam("customerName") String customerName);
    @RequestMapping(value = "/hdmatchcompanymember/{id}", method = RequestMethod.GET)
    R<HdMatchCompanyMember> getMemberMatchCompanyByComId(@PathVariable("id") Serializable id);
    @RequestMapping(value = "/hdmatchcompanymember/addList", method = RequestMethod.POST)
    public R addMatchCompanyMemberList(@RequestBody List<HdMatchCompanyMember> hdMatchCompanyMemberList);
    @RequestMapping(value = "/hdmatchcompanymember/all2", method = RequestMethod.GET)
    public List<HdMatchCompanyMember> getAllMatchCompanyMember(@RequestParam() Map<String,Object> params);
    @RequestMapping(value = "/hdmatchcompanymember/all", method = RequestMethod.GET)
    public List<HdMatchCompanyMember> getAllSingleMatchCompanyMember(@RequestParam() Map<String,Object> params);

    /**************************************其他**************************************/
    //查询九恒星数据
    @RequestMapping(value = "/syncData/syncData",method = RequestMethod.POST)
    List<List<Object>> syncData(@RequestBody Map<String, Object> params);

    //根據租戶獲取抬頭
    @RequestMapping(value = "/hdbanktenant/selectOne",method = RequestMethod.GET)
    HdBankTenant getTenantByTerm(@RequestParam() Map<String,Object> params);

    //根据租户名中间表获取租户id

    //获取所有租户
    @RequestMapping(value = "/hdbanktenant/all",method = RequestMethod.GET)
    List<HdBankTenant> selectAllTenants();
    //保存进中间表
    @PostMapping(value = "/hdbanktenant")
    R saveBankTenant(@RequestBody HdBankTenant hdBankTenant);




}
