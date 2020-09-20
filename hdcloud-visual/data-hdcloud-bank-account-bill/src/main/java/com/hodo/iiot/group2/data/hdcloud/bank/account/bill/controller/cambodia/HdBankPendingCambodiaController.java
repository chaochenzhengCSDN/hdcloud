package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller.cambodia;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankPending;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.cambodia.HdBankPendingCambodiaServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.StringUtil;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
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
@RequestMapping("/hdbankpending/cambodia")
@Api(value = "hdbankpending", tags = "bank_account管理")
public class HdBankPendingCambodiaController {

    private final HdBankPendingCambodiaServiceImpl hdBankPendingService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankPendingPage(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return R.failed("没有租户id");
        }

        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankPending> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByDesc("account_date", "company_name");
        return R.ok(hdBankPendingService.page(page, queryWrapper));
    }


    /**
     * 全部查询
     *
     * @return
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @GetMapping("/all")
    public List<HdBankPending> getHdBankPendingList(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }


        String pagestr = jjUtil.handleParams(map, "page");
        String limitstr = jjUtil.handleParams(map, "limit");
        Integer pageNumber = pagestr == null ? 1 : Integer.parseInt(pagestr);
        Integer pageSize = limitstr == null ? 10 : Integer.parseInt(limitstr);
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankPending> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id", tenantId);
        queryWrapper.orderByDesc("account_date", "company_name");
        return hdBankPendingService.list(queryWrapper);
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
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return R.ok(hdBankPendingService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @ApiOperation(value = "查询一条", notes = "查询一条")
    @GetMapping("/selectOne")
    public HdBankPending selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankPending> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdBankPendingService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/page2")
    public List<HdBankPending> getBankPendingList(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Integer tenantId = TenantContextHolder.getTenantId();
        jjUtil.handleParamsClean(params);
        if (tenantId == null || tenantId < 1) {
            return new ArrayList();
        }
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");

        String bankName = jjUtil.handleParams(params, "bank_name");
        if (StringUtil.isEmpty(moneyStart)) {
            moneyStart = null;
        } else {
            moneyStart = moneyStart.replace(",", "");
        }
        if (StringUtil.isEmpty(moneyEnd)) {
            moneyEnd = null;
        } else {
            moneyEnd = moneyEnd.replace(",", "");
        }
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        return hdBankPendingService.getBankPendingList(moneyStart, moneyEnd, startTime, endTime,
                String.valueOf((pageInt - 1) * limitInt), limit, tenantId, params, bankName);
    }


    @GetMapping("/other")
    public Map<String, Object> getOtherVal(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        jjUtil.handleParamsClean(params);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new HashedMap();
        }
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");

        String bankName = jjUtil.handleParams(params, "account_id");
        if (StringUtil.isEmpty(moneyStart)) {
            moneyStart = null;
        } else {
            moneyStart = moneyStart.replace(",", "");
        }
        if (StringUtil.isEmpty(moneyEnd)) {
            moneyEnd = null;
        } else {
            moneyEnd = moneyEnd.replace(",", "");
        }
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");

        Map map = hdBankPendingService.getOtherVal(moneyStart, moneyEnd, startTime,
                endTime, tenantId, params, bankName);
        return map;
    }


    @GetMapping("/all2")
    public List<HdBankPending> getAllBankPending(@RequestParam @ApiParam(value = "参数名称(subjects,page,limit,dateStart,dataEnd," +
            "remark,bankName,mySubjects)") Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        jjUtil.handleParamsClean(params);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        //2.1版本新增金额区间查询
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        return hdBankPendingService.getAllBankPending(moneyStart, moneyEnd, startTime, endTime, tenantId, params);
    }


    /**
     * 新增bank_account
     *
     * @param hdBankPending bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdbankpending_add')")
    public R save(@RequestBody HdBankPending hdBankPending) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId = null;
        String createBy = null;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdBankPending.setCreateBy(createBy);
            hdBankPending.setTenantId(tenantId);
            hdBankPending.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        return R.ok(hdBankPendingService.save(hdBankPending));
    }

    /**
     * 修改bank_account
     *
     * @param hdBankPending bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdbankpending_edit')")
    public R updateById(@RequestBody HdBankPending hdBankPending) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        boolean b = hdBankPendingService.updateById(hdBankPending);
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
    @PreAuthorize("@pms.hasPermission('hdbankpending_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        return R.ok(hdBankPendingService.removeById(id));
    }

    @ApiOperation(value = "通过ids删除", notes = "通过ids删除")
    @SysLog("通过ids删除")
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdbankpending_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        try {
            return R.ok(hdBankPendingService.removeByIds(list));
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
    @PreAuthorize("@pms.hasPermission('hdbankpending_delete')")
    public R removeById(@RequestBody Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
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
        return R.ok(hdBankPendingService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    @ApiOperation(value = "批量插入", notes = "批量插入")
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdbankpending_import')")
    public R addList(@RequestBody List<HdBankPending> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_CAMBODIA);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
//        System.out.println("进来了");
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
//        System.out.println(list.size()+"做处理");
        for (HdBankPending hdBankPending :
                list) {
            hdBankPending.setCreateBy(createBy);
            hdBankPending.setTenantId(tenantId);
            hdBankPending.setCreateTime(new Date());

        }
//        System.out.println("处理完");
        boolean b = hdBankPendingService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }


}
