package com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.ty;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.HdMatchCompanyController;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdMatchCompany;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hdmatchcompany/ty")
public class HdMatchCompanyTy extends HdMatchCompanyController {

    @Override
    @GetMapping("/page")
    public R getHdMatchCompanyPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdMatchCompanyPage(map);
    }

    @Override
    @GetMapping("/all")
    public List<HdMatchCompany> getHdMatchCompanyList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getHdMatchCompanyList(map);
    }

    @Override
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.getById(id);
    }

    @Override
    @GetMapping("/selectOne")
    public HdMatchCompany selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.selectOne(map);
    }

    @Override
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdmatchcompany_add')")
    public R save(@RequestBody HdMatchCompany hdMatchCompany) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.save(hdMatchCompany);
    }

    @Override
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdmatchcompany_edit')")
    public R updateById(@RequestBody HdMatchCompany hdMatchCompany) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.updateById(hdMatchCompany);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdmatchcompany_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeById(id);
    }

    @Override
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdmatchcompany_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeByIds(list);
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdmatchcompany_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_TY);
        return super.removeById(map);
    }

    @Override
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdmatchcompany_import')")
    public R addList(@RequestBody List<HdMatchCompany> list) {
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
}
