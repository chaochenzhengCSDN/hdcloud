package com.hodo.iiot.group2.hdcloud.cash.account.cambodia.feign;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.constant.ServiceNameConstants;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashRemark;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashSubject;
import com.hodo.iiot.group2.hdcloud.cash.account.cambodia.entity.HdCashUnit;
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
    /**************************************摘要**************************************/
    //批量添加
    @RequestMapping(value = "/hdcashremark/addList", method = RequestMethod.POST)
    public R addCashRemarkList(@RequestBody List<HdCashRemark> cashRemarkList);
    //查询所有
    @GetMapping(value = "/hdcashremark/all")
    public List<HdCashRemark> getAllCashRemark(@RequestParam() Map<String,Object> params);
    //分页查询
    @GetMapping(value = "/hdcashremark/page")
    public R pageCashRemark(@RequestParam() Map<String,Object> params);
    //添加
    @PostMapping("/hdcashremark")
    public R addCashRemark(@RequestBody HdCashRemark hdCashRemark);
    //删除
    @DeleteMapping("/hdcashremark/{id}")
    public R removeCashRemarkById(@PathVariable("id") Serializable id);
    //查询
    @GetMapping("/hdcashremark/{id}")
    public R getCashRemarkById(@PathVariable("id") Serializable id);
    //更新
    @PutMapping("/hdcashremark")
    public R updateCashRemarkById(@RequestBody HdCashRemark hdCashRemark);
    //批量删除
    @PostMapping("/hdcashremark/removeByIds")
    public R removeCashRemarkByIds(@RequestBody List<String> ids);
    /**************************************公司**************************************/
    //公司模块
    //查询id
    @RequestMapping(value = "/hdcashunit/{id}", method = RequestMethod.GET)
    public R<HdCashUnit> getCompanyById(@PathVariable("id") Serializable id);
    //编辑
    @PutMapping(value = "/hdcashunit")
    public R updateCompanyById(@RequestBody HdCashUnit entity);
    //增加
    @RequestMapping(value = "/hdcashunit", method = RequestMethod.POST)
    public R addCompany(@RequestBody HdCashUnit entity);
    //删除
    @DeleteMapping("/hdcashunit/{id}")
    public R removeCompanyById(@PathVariable("id") Serializable id);
    //批量添加公司
    @RequestMapping(value = "/hdcashunit/addList", method = RequestMethod.POST)
    public R addCompanyList(@RequestBody List<HdCashUnit> companyList);
    @RequestMapping(value = "/hdcashunit/all", method = RequestMethod.GET)
    public List<HdCashUnit> getAllCompany(@RequestParam() Map<String, Object> params);
    @RequestMapping(value = "/hdcashunit/page", method = RequestMethod.GET)
    R pageCompany(@RequestParam() Map<String, Object> params);
    @PostMapping(value = "/hdcashunit/removeByIds")
    public R removeCompanyByIds(@RequestBody List<String> ids);
    @GetMapping(value = "/hdcashunit/selectOne")
    public HdCashUnit getCashUnitByName(@RequestParam Map<String,Object> params);
    /**************************************科目**************************************/
    //批量添加
    @RequestMapping(value = "/hdcashsubject/addList", method = RequestMethod.POST)
    public R addCashSubjectList(@RequestBody List<HdCashSubject> cashSubjectList);
    //查询所有
    @GetMapping(value = "/hdcashsubject/all")
    public List<HdCashSubject> getAllCashSubject(@RequestParam Map<String,Object> params);
    //分页查询
    @GetMapping(value = "/hdcashsubject/page")
    public R pageCashSubject(@RequestParam Map<String,Object> params);
    //添加
    @PostMapping(value = "/hdcashsubject")
    public R addCashSubject(@RequestBody HdCashSubject hdCashSubject);
    //删除
    @DeleteMapping(value = "/hdcashsubject/{id}")
    public R removeCashSubjectById(@PathVariable("id") Serializable id);
    //查询
    @GetMapping(value = "/hdcashsubject/{id}")
    public R getCashSubjectById(@PathVariable("id") Serializable id);
    //更新
    @PutMapping(value = "/hdcashsubject")
    public R updateCashSubjectById(@RequestBody HdCashSubject hdCashSubject);
    //批量删除
    @PostMapping(value = "/hdcashsubject/removeByIds")
    public R removeCashSubjectByIds(@RequestBody List<String> ids);
    /****************************************************************************/
  




}
