package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller.gf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.gf.HdTransferBankStatementGfServiceImpl;
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
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdtransferbankstatement/gf")
@Api(value = "hdtransferbankstatement", tags = "bank_account管理")
public class HdTransferBankStatementGfController {

    private final HdTransferBankStatementGfServiceImpl hdTransferBankStatementService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdTransferBankStatementPage(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdTransferBankStatement> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("no", "create_time");
        return R.ok(hdTransferBankStatementService.page(page, queryWrapper));
    }

    /**
     * 全部查询
     *
     * @return
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @GetMapping("/all")
    public List<HdTransferBankStatement> getHdTransferBankStatementList(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdTransferBankStatement> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("no", "account_date");
        return hdTransferBankStatementService.list(queryWrapper);
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
        return R.ok(hdTransferBankStatementService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @ApiOperation(value = "查询一条", notes = "查询一条")
    @GetMapping("/selectOne")
    public HdTransferBankStatement selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdTransferBankStatement> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdTransferBankStatementService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增bank_account
     *
     * @param hdTransferBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
    @SysLog("新增bank_account")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('hdtransferbankstatement_add')")
    public R save(@RequestBody HdTransferBankStatement hdTransferBankStatement) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        String createBy;
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            createBy = hodoUser.getUsername();
            hdTransferBankStatement.setCreateBy(createBy);
            hdTransferBankStatement.setTenantId(tenantId);
            hdTransferBankStatement.setCreateTime(new Date());
            if (tenantId == null || tenantId < 1) {
                return R.failed("没有租户id");
            }
        } else {
            return R.failed("用户信息获取失败");
        }
        return R.ok(hdTransferBankStatementService.save(hdTransferBankStatement));
    }

    /**
     * 修改bank_account
     *
     * @param hdTransferBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdtransferbankstatement_edit')")
    public R updateById(@RequestBody HdTransferBankStatement hdTransferBankStatement) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        boolean b = hdTransferBankStatementService.updateById(hdTransferBankStatement);
        if (b) {
            return R.ok(b);
        } else {
            return R.failed(b);
        }
    }

    @ApiOperation(value = "通过ids删除", notes = "通过ids删除")
    @SysLog("通过ids删除")
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdtransferbankstatement_batch_del')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        try {
            return R.ok(hdTransferBankStatementService.removeByIds(list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
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
    @PreAuthorize("@pms.hasPermission('hdtransferbankstatement_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdTransferBankStatementService.removeById(id));
    }

    /**
     * 通过实体类删除
     */
    @ApiOperation(value = "通过实体类删除", notes = "通过实体类删除")
    @SysLog("通过实体类删除")
    @DeleteMapping("/delete")
    @PreAuthorize("@pms.hasPermission('hdtransferbankstatement_delete')")
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
        return R.ok(hdTransferBankStatementService.remove(wrapper));
    }


    /**
     * 获取最大编号
     * @return
     */
//    @ApiOperation(value = "获取最大编号", notes = "获取最大编号")
//    @GetMapping(value = "/selectMaxNo")
//    public synchronized BigDecimal selectMaxNo(@RequestParam Map<String,Object> map) {
//        Integer tenantId = TenantContextHolder.getTenantId();
//        return hdTransferBankStatementService.selectMaxNo(tenantId);
//    }

    /**
     * 批量插入
     *
     * @return
     */
    @ApiOperation(value = "批量插入", notes = "批量插入")
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdtransferbankstatement_import')")
    public R addList(@RequestBody List<HdTransferBankStatement> list) {
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
        for (HdTransferBankStatement hdTransferBankStatement :
                list) {
            hdTransferBankStatement.setCreateBy(createBy);
            hdTransferBankStatement.setTenantId(tenantId);
            hdTransferBankStatement.setCreateTime(new Date());
        }
        boolean b = hdTransferBankStatementService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }


    @GetMapping(value = "/page2")
    @ApiOperation("查询对象列表")
    public List<HdTransferBankStatement> getNzbankAccountList(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        jjUtil.handleParamsClean(params);
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        String page = jjUtil.handleParams(params, "page");
        String limit = jjUtil.handleParams(params, "limit");
        if (page == null) {
            page = "1";
        }
        if (limit == null) {
            limit = "10";
        }
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");

        String paysubjectName = jjUtil.handleParams(params, "paysubjectName");
        String incomesubjectName = jjUtil.handleParams(params, "incomesubjectName");
        List<HdTransferBankStatement> list = hdTransferBankStatementService.getTynzbankAccountList(moneyStart, moneyEnd,
                startTime, endTime,
                String.valueOf((Integer.parseInt(page) - 1) * Integer.parseInt(limit)), limit, params,
                paysubjectName, incomesubjectName, tenantId);
        return list;

    }

    ;

    @GetMapping(value = "/all2")
    public List<HdTransferBankStatement> getAllNzbankAccountList(@RequestParam Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        jjUtil.handleParamsClean(params);
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        jjUtil.handleParams(params, "userName");
        jjUtil.handleParams(params, "token");
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String incomesubjectName = jjUtil.handleParams(params, "incomesubject");
        String paysubjectName = jjUtil.handleParams(params, "paysubject");

        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        List<HdTransferBankStatement> list =
                hdTransferBankStatementService.getTynzbankAccountListEx(moneyStart, moneyEnd,
                        startTime, endTime,
                        params,
                        paysubjectName, incomesubjectName, tenantId);
        return list;

    }

    ;

    @GetMapping(value = "/other")
    public Map<String, Object> getNzbankAccountCount(@RequestParam @ApiParam(value = "参数名称(除了dateStart,dataEnd)") Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tenantId;
        jjUtil.handleParamsClean(params);
        if (principal instanceof HodoUser) {
            HodoUser hodoUser = (HodoUser) principal;
            tenantId = hodoUser.getTenantId();
            if (tenantId == null || tenantId < 1) {
                return new HashedMap();
            }
        } else {
            return new HashedMap();
        }
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        String paysubjectName = jjUtil.handleParams(params, "paysubjectName");
        String incomesubjectName = jjUtil.handleParams(params, "incomesubjectName");
        String total = hdTransferBankStatementService.getTynzbankAccountCount(moneyStart, moneyEnd,
                startTime, endTime,
                params,
                paysubjectName, incomesubjectName, tenantId);
        Map map = new HashedMap();
        map.put("total", Integer.parseInt(total));
        map.put("income", 0);
        map.put("pay", 0);
        return map;
    }


}
