package com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.ty;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.HdCompanyMemberController;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdCompanyMember;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/hdcompanymember/ty")
public class HdCompanyMemberTy extends HdCompanyMemberController{


    @Override
    @GetMapping("/page")
    public R getHdCompanyMemberPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdCompanyMemberPage(map);
    }

    @Override
    @GetMapping("/all")
    public List<HdCompanyMember> getHdCompanyMemberList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdCompanyMemberList(map);
    }

    @Override
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getById(id);
    }

    @Override
    @GetMapping("/selectOne")
    public HdCompanyMember selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.selectOne(map);
    }

    @Override
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdcompanymember_add')")
    public R save(@RequestBody HdCompanyMember hdCompanyMember) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.save(hdCompanyMember);
    }

    @Override
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdcompanymember_edit')")
    public R updateById(@RequestBody HdCompanyMember hdCompanyMember) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.updateById(hdCompanyMember);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdcompanymember_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeById(id);
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdcompanymember_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeById(map);
    }

    @Override
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdcompanymember_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeByIds(list);
    }

    @Override
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdcompanymember_import')")
    public R addList(@RequestBody List<HdCompanyMember> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.addList(list);
    }
}
