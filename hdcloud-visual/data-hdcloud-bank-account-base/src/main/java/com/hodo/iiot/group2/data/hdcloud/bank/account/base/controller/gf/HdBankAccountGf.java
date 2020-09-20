package com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.gf;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.HdBankAccountController;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdBankAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hdbankaccount/gf")
public class HdBankAccountGf extends HdBankAccountController {
    

    @Override
    @GetMapping("/page")
    public R getHdBankAccountPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.getHdBankAccountPage(map);
    }

    @Override
    @GetMapping("/all")
    public List<HdBankAccount> getHdBankAccountList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.getHdBankAccountList(map);
    }

    @Override
    @GetMapping("/all2")
    public List<HdBankAccount> getHdBankAccountList2(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.getHdBankAccountList2(map);
    }

    @Override
    @GetMapping("/selectOne")
    public HdBankAccount selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.selectOne(map);
    }

    @Override
    @GetMapping("/selectStringList")
    public List<String> selectDistinctString(String str, String param, String val) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.selectDistinctString(str, param, val);
    }

    @Override
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.getById(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdbankaccount_add')")
    public R save(@RequestBody HdBankAccount hdBankAccount) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.save(hdBankAccount);
    }

    @Override
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdbankaccount_edit')")
    public R updateById(@RequestBody HdBankAccount hdBankAccount) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.updateById(hdBankAccount);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdbankaccount_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.removeById(id);
    }

    @Override
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdbankaccount_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.removeByIds(list);
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdbankaccount_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.removeById(map);
    }

    @Override
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdbankaccount_import')")
    public R addList(@RequestBody List<HdBankAccount> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return super.addList(list);
    }
}
