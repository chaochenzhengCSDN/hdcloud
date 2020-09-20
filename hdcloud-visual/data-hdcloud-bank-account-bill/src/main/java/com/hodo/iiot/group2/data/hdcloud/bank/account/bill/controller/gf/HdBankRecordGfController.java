package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller.gf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankRecord;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.gf.HdBankRecordGfServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.StringUtil;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:37:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankrecord/gf")
@Api(value = "hdbankrecord", tags = "bank_account管理")
public class HdBankRecordGfController {

    private final HdBankRecordGfServiceImpl hdBankRecordGfService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankRecordPage(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdBankRecord> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("num", "account_date");
        return R.ok(hdBankRecordGfService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @GetMapping("/all")
    public List<HdBankRecord> getHdBankRecordList(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdBankRecord> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("num", "account_date");
        return hdBankRecordGfService.list(queryWrapper);
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
        return R.ok(hdBankRecordGfService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @ApiOperation(value = "查询一条", notes = "查询一条")
    @GetMapping("/selectOne")
    public HdBankRecord selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankRecord> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdBankRecordGfService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 新增bank_account
     *
     * @param hdBankRecord bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdbankrecord_add')")
    public R save(@RequestBody HdBankRecord hdBankRecord) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdBankRecord.setCreateBy(createBy);
            hdBankRecord.setTenantId(tenantId);
            hdBankRecord.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        return R.ok(hdBankRecordGfService.save(hdBankRecord));
    }

    /**
     * 修改bank_account
     *
     * @param hdBankRecord bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdbankrecord_edit')")
    public R updateById(@RequestBody HdBankRecord hdBankRecord) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        boolean b = hdBankRecordGfService.updateById(hdBankRecord);
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
    @PreAuthorize("@pms.hasPermission('hdbankrecord_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdBankRecordGfService.removeById(id));
    }

    @ApiOperation(value = "通过ids删除", notes = "通过ids删除")
    @SysLog("通过ids删除")
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdbankrecord_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        try {
            return R.ok(hdBankRecordGfService.removeByIds(list));
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
    @PreAuthorize("@pms.hasPermission('hdbankrecord_delete')")
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
        return R.ok(hdBankRecordGfService.remove(wrapper));
    }

    /**
     * 批量插入
     *
     * @return
     */
    @ApiOperation(value = "批量插入", notes = "批量插入")
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdbankrecord_import')")
    public R addList(@RequestBody List<HdBankRecord> list) {
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
        for (HdBankRecord hdBankRecord :
                list) {
            hdBankRecord.setCreateBy(createBy);
            hdBankRecord.setTenantId(tenantId);
            hdBankRecord.setCreateTime(new Date());
        }
        boolean b = hdBankRecordGfService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }


    @GetMapping(value = "/page2")
    public List<HdBankRecord> getHdBankRecordList2(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        jjUtil.handleParamsClean(params);
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        String companyId = jjUtil.handleParams(params, "company_id");
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        //查询时间分离
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        //2.1版本新增金额区间查询
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        //序号，银行
        String num = jjUtil.handleParams(params, "num");
        String accountId = jjUtil.handleParams(params, "bank_account_id");
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        if (StringUtil.isEmpty(limit)) {
            limit = "10";
        }
        //空字符串转null
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
        int pageInt = Integer.parseInt(page);
        int limitInt = Integer.parseInt(limit);
        List<HdBankRecord> list = hdBankRecordGfService.getHdBankRecordList(moneyStart, moneyEnd, companyId, startTime, endTime,
                String.valueOf((pageInt - 1) * limitInt), limit, tenantId, params, num, accountId);
        return list;

    }

    ;


    @GetMapping(value = "/all2")
    public List<HdBankRecord> getAllBankRecord2(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        jjUtil.handleParamsClean(params);
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        String companyName = jjUtil.handleParams(params, "company_id");
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        String num = jjUtil.handleParams(params, "num");
        String accountId = jjUtil.handleParams(params, "bank_account_id");

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

        return hdBankRecordGfService.getAllBankRecord(moneyStart, moneyEnd, companyName, startTime, endTime,
                tenantId, params, num, accountId);
    }

    @GetMapping(value = "/other")
    public Map<String, Object> otherVal(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        jjUtil.handleParamsClean(params);
        Map map = new HashMap();
        if (tenantId == null || tenantId < 1) {
            return map;
        }
        String companyName = jjUtil.handleParams(params, "company");
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        String num = jjUtil.handleParams(params, "num");
        String accountId = jjUtil.handleParams(params, "accountId");

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
        String total = hdBankRecordGfService.otherVal(moneyStart, moneyEnd, companyName, startTime, endTime,
                tenantId, params, num, accountId);
        map.put("total", Integer.parseInt(total));
        map.put("income", 0);
        map.put("pay", 0);
        return map;
    }


}
