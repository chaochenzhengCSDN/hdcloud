package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller.gf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankAllocation;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.gf.HdBankAllocationGfServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankallocation/gf")
@Api(value = "hdbankallocation", tags = "bank_account管理")
public class HdBankAllocationGfController {

    private final HdBankAllocationGfServiceImpl hdBankAllocationGfService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankAllocationPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return R.failed("没有租户id");
        }

        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAllocation> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("number");
        return R.ok(hdBankAllocationGfService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @GetMapping("/all")
    public List<HdBankAllocation> getHdBankAllocationList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList();
        }

        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAllocation> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("number");
        return hdBankAllocationGfService.list(queryWrapper);
    }


    /**
     * 通过id查询bank_account
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdBankAllocationGfService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @ApiOperation(value = "查询一条", notes = "查询一条")
    @GetMapping("/selectOne")
    public HdBankAllocation selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankAllocation> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdBankAllocationGfService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增bank_account
     *
     * @param hdBankAllocation bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdbankallocation_add')")
    public R save(@RequestBody HdBankAllocation hdBankAllocation) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdBankAllocation.setCreateBy(createBy);
            hdBankAllocation.setTenantId(tenantId);
            hdBankAllocation.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        return R.ok(hdBankAllocationGfService.save(hdBankAllocation));
    }

    /**
     * 修改bank_account
     *
     * @param hdBankAllocation bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdbankallocation_edit')")
    public R updateById(@RequestBody HdBankAllocation hdBankAllocation) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        boolean b = hdBankAllocationGfService.updateById(hdBankAllocation);
        if (b) {
            return R.ok(b);
        } else {
            return R.failed(b);
        }
    }

    /**
     * 通过id删除bank_account
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除bank_account", notes = "通过id删除bank_account")
    @SysLog("通过id删除bank_account")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdbankallocation_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdBankAllocationGfService.removeById(id));
    }

    @ApiOperation(value = "通过ids删除", notes = "通过ids删除")
    @SysLog("通过ids删除")
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdbankallocation_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        try {
            return R.ok(hdBankAllocationGfService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("删除失败");
        }

    }

    /**
     * 通过实体类删除
     */
    @ApiOperation(value = "通过实体类删除", notes = "通过实体类删除")
    @SysLog("通过实体类删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdbankallocation_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        QueryWrapper wrapper = new QueryWrapper();
        Integer tenantId;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        wrapper.eq("tenant_id", tenantId);
        jjUtil.handleAllParamsToWrapper(map, wrapper);
        return R.ok(hdBankAllocationGfService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    @ApiOperation(value = "批量插入", notes = "批量插入")
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdbankallocation_import')")
    public R addList(@RequestBody List<HdBankAllocation> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("没有用户信息");
        }
        for (HdBankAllocation hdBankAllocation :
                list) {
            hdBankAllocation.setCreateBy(createBy);
            hdBankAllocation.setTenantId(tenantId);
            hdBankAllocation.setCreateTime(new Date());
        }

        boolean b = hdBankAllocationGfService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }

}
