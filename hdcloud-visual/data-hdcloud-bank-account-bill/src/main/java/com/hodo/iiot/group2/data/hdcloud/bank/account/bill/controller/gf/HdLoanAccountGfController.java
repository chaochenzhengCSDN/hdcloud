package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller.gf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdLoanAccount;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.gf.HdLoanAccountGfServiceImpl;
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
 * @author zxw
 * @date 2019-11-06 14:01:28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdloanaccount/gf")
@Api(value = "hdloanaccount", tags = "管理")
public class HdLoanAccountGfController {

    private final HdLoanAccountGfServiceImpl hdLoanAccountService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdMatchCompanyPage(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdLoanAccount> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return R.ok(hdLoanAccountService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @GetMapping("/all")
    public List<HdLoanAccount> getHdLoanAccountList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdLoanAccount> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("create_time");
        return hdLoanAccountService.list(queryWrapper);
    }


    /**
     * 通过id查询
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdLoanAccountService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @ApiOperation(value = "查询一条", notes = "查询一条")
    @GetMapping("/selectOne")
    public HdLoanAccount selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdLoanAccount> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdLoanAccountService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增
     *
     * @param hdLoanAccount
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdloanaccount_add')")
    public R save(@RequestBody HdLoanAccount hdLoanAccount) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId ;
        String createBy;
        Integer createById;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            createById = hodoUser.getId();
            hdLoanAccount.setCreateBy(createBy);
            hdLoanAccount.setTenantId(tenantId);
            hdLoanAccount.setCreateTime(new Date());
            hdLoanAccount.setCreateById(createById);
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        return R.ok(hdLoanAccountService.save(hdLoanAccount));
    }

    /**
     * 修改
     *
     * @param hdLoanAccount
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdloanaccount_edit')")
    public R updateById(@RequestBody HdLoanAccount hdLoanAccount) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        boolean b = hdLoanAccountService.updateById(hdLoanAccount);
        if (b) {
            return R.ok(b);
        } else {
            return R.failed(b);
        }
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('hdloanaccount_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdLoanAccountService.removeById(id));
    }

    @ApiOperation(value = "通过ids删除", notes = "通过ids删除")
    @SysLog("通过ids删除")
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdloanaccount_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        try {
            return R.ok(hdLoanAccountService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

    }

    /**
     * 通过实体类删除
     */
    @ApiOperation(value = "通过实体类删除", notes = "通过实体类删除")
    @SysLog("通过实体类删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdloanaccount_delete')")
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
        return R.ok(hdLoanAccountService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    @ApiOperation(value = "批量插入", notes = "批量插入")
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdloanaccount_import')")
    public R addList(@RequestBody List<HdLoanAccount> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId = null;
        String createBy = null;
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
        for (HdLoanAccount hdLoanAccount :
                list) {
            hdLoanAccount.setCreateBy(createBy);
            hdLoanAccount.setTenantId(tenantId);
            hdLoanAccount.setCreateTime(new Date());
        }

        boolean b = hdLoanAccountService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }

}
