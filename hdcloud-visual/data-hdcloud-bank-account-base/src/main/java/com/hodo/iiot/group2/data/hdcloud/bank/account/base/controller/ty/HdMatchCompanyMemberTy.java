package com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.ty;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.HdMatchCompanyMemberController;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdMatchCompanyMember;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hdmatchcompanymember/ty")
public class HdMatchCompanyMemberTy extends HdMatchCompanyMemberController {


    @Override
    @GetMapping("/page")
    public R getHdMatchCompanyMemberPage(@RequestParam Map<String, Object> map) throws ParseException {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdMatchCompanyMemberPage(map);
    }

    @Override
    @GetMapping("/all")
    public List<HdMatchCompanyMember> getHdMatchCompanyMemberList(@RequestParam Map<String, Object> map) throws ParseException {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdMatchCompanyMemberList(map);
    }

    @Override
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getById(id);
    }

    @Override
    @GetMapping("/selectOne")
    public HdMatchCompanyMember selectOne(@RequestParam Map<String, Object> map) throws ParseException {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.selectOne(map);
    }

    @Override
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdmatchcompanymember_add')")
    public R save(@RequestBody HdMatchCompanyMember hdMatchCompanyMember) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.save(hdMatchCompanyMember);
    }

    @Override
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdmatchcompanymember_edit')")
    public R updateById(@RequestBody HdMatchCompanyMember hdMatchCompanyMember) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.updateById(hdMatchCompanyMember);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdmatchcompanymember_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeById(id);
    }

    @Override
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdmatchcompanymember_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeByIds(list);
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdmatchcompanymember_delete')")
    public R removeById(@RequestBody Map<String, Object> map) throws ParseException {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeById(map);
    }

    @Override
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdmatchcompanymember_import')")
    public R addList(@RequestBody List<HdMatchCompanyMember> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.addList(list);
    }

    @Override
    @GetMapping(value = "/page2")
    public List getHdTymatchCompanyList(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdTymatchCompanyList(params);
    }

    @Override
    @GetMapping(value = "/all2")
    public List getAllTymatchCompany(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getAllTymatchCompany(params);
    }

    @Override
    @GetMapping(value = "/other")
    public Map<String, Object> other(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.other(params);
    }

    @Override
    @GetMapping(value = "/allEx")
    public List getAllCompanyEx(String company_id, String customer_name, String bank_id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getAllCompanyEx(company_id, customer_name, bank_id);
    }
}
