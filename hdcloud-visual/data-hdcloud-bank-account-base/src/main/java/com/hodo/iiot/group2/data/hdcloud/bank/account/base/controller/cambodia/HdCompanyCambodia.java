package com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.cambodia;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.controller.HdCompanyController;
import com.hodo.iiot.group2.data.hdcloud.bank.account.base.entity.HdCompany;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hdcompany/cambodia")
public class HdCompanyCambodia extends HdCompanyController {


    @Override
    @GetMapping("/page")
    public R getHdCompanyPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.getHdCompanyPage(map);
    }

    @Override
    @GetMapping("/all")
    public List<HdCompany> getHdCompanyList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.getHdCompanyList(map);
    }

    @Override
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.getById(id);
    }

    @Override
    @GetMapping("/selectOne")
    public HdCompany selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.selectOne(map);
    }

    @Override
    @PostMapping()
    @PreAuthorize("@pms.hasPermission('hdcompany_add')")
    public R save(@RequestBody HdCompany hdCompany) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.save(hdCompany);
    }

    @Override
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdcompany_edit')")
    public R updateById(@RequestBody HdCompany hdCompany) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.updateById(hdCompany);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdcompany_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.removeById(id);
    }

    @Override
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdcompany_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.removeByIds(list);
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdcompany_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.removeById(map);
    }

    @Override
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdcompany_import')")
    public R addList(@RequestBody List<HdCompany> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return super.addList(list);
    }
}
