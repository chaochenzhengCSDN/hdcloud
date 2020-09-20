package com.hodo.iiot.group2.data.hdcloud.bank.account.bill.controller.gf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hodo.hdcloud.common.core.util.R;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.hdcloud.common.datasource.support.DynamicDataSourceContextHolder;
import com.hodo.hdcloud.common.log.annotation.SysLog;
import com.hodo.hdcloud.common.security.service.HodoUser;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.common.DataSourceId;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.entity.HdBankStatement;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.service.impl.gf.HdBankStatementGfServiceImpl;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.StringUtil;
import com.hodo.iiot.group2.data.hdcloud.bank.account.bill.util.jjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


/**
 * bank_account
 *
 * @author dq
 * @date 2019-10-29 15:28:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hdbankstatementty/gf")
@Api(value = "hdbankstatement", tags = "bank_account管理")
public class HdBankStatementGfController {

    private final HdBankStatementGfServiceImpl hdBankStatementGfService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHdBankStatementPage(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdBankStatement> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("account_date", "by2");
        Object ob = hdBankStatementGfService.page(page, queryWrapper);
        return R.ok(ob);
    }

    /**
     * 全部查询
     *
     * @return
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @GetMapping("/all")
    public R getHdBankStatementAll(@RequestParam Map<String, Object> map) {
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
        QueryWrapper<HdBankStatement> queryWrapper = new QueryWrapper<>();
        Page page = new Page(pageNumber, pageSize);
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        queryWrapper.eq("tenant_id",tenantId);
        queryWrapper.orderByDesc("accountDate", "by2");
        return R.ok(hdBankStatementGfService.list(queryWrapper));
    }

    /**
     * 获取sheetIds
     *
     * @return
     */
    @ApiOperation(value = "获取sheetids", notes = "获取sheetids")
    @GetMapping(value = "/sheetids")
    public List<String> getSheetIds() {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        return hdBankStatementGfService.getSheetIds(tenantId);
    }

    /**
     * 获取rids
     *
     * @return
     */
    @ApiOperation(value = "获取rids", notes = "获取rids")
    @GetMapping(value = "/rids")
    public List<String> getRIds() {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return new ArrayList<>();
        }
        return hdBankStatementGfService.getRIds(tenantId);
    }


    /**
     * 通过id查询bank_account
     *
     * @param id id0
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdBankStatementGfService.getById(id));
    }

    /**
     * 查一条
     *
     * @return
     */
    @ApiOperation(value = "查询一条", notes = "查询一条")
    @GetMapping("/selectOne")
    public HdBankStatement selectOne(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null || tenantId < 1) {
            return null;
        }
        jjUtil.handleParamsClean(map);
        QueryWrapper<HdBankStatement> queryWrapper = new QueryWrapper<>();
        jjUtil.handleAllParamsToWrapper(map, queryWrapper);
        try {
            return hdBankStatementGfService.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * 新增bank_account
//     * @param hdBankStatementTy bank_account
//     * @return R
//     */
//    @ApiOperation(value = "新增bank_account", notes = "新增bank_account")
//    @SysLog("新增bank_account")
//    @PostMapping
//    @PreAuthorize("@pms.hasPermission('hdbankstatement_add')")
//    public R save(@RequestBody HdBankStatementTy hdBankStatementTy) {
//
//        return R.ok(hdBankStatementGfService.save(hdBankStatementTy));
//    }

    /**
     * 修改bank_account
     *
     * @param hdBankStatement bank_account
     * @return R
     */
    @ApiOperation(value = "修改bank_account", notes = "修改bank_account")
    @SysLog("修改bank_account")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('hdbankstatementgf_edit')")
    public R updateById(@RequestBody HdBankStatement hdBankStatement) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        boolean b = hdBankStatementGfService.updateById(hdBankStatement);
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
    @PreAuthorize("@pms.hasPermission('hdbankstatementgf_del')")
    public R removeById(@PathVariable String id) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return R.ok(hdBankStatementGfService.removeById(id));
    }

    @ApiOperation(value = "通过ids删除", notes = "通过ids删除")
    @SysLog("通过ids删除")
    @PostMapping("/removeByIds")
    @PreAuthorize("@pms.hasPermission('hdbankstatementgf_deleteBatch')")
    public R removeByIds(@RequestBody List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        try {
            return R.ok(hdBankStatementGfService.removeByIds(list));
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
    @PreAuthorize("@pms.hasPermission('hdbankstatementgf_delete')")
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
        return R.ok(hdBankStatementGfService.remove(wrapper));
    }

    /**
     * 分页联表查询
     *
     * @return
     */
    @ApiOperation(value = "分页联表查询", notes = "分页联表查询")
    @GetMapping(value = "/page2")
    public List<HdBankStatement> getBankStatementListBySql(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        jjUtil.handleParamsClean(map);
        String page = jjUtil.handleParams(map, "page");
        String limit = jjUtil.handleParams(map, "limit");
        if (page == null || limit == null) {
            return new ArrayList();
        }
        String startTime = jjUtil.handleParams(map, "dateStart");
        String endTime = jjUtil.handleParams(map, "dateEnd");
        //2.1版本新增金额区间查询
        String moneyStart = jjUtil.handleParams(map, "moneyStart");
        String moneyEnd = jjUtil.handleParams(map, "moneyEnd");
        //增加到账日期，所属银行，账单类型
        String companyId = jjUtil.handleParams(map, "company_id");
        String bankAccountId = jjUtil.handleParams(map, "bank_account_id");
        String synStartTime = jjUtil.handleParams(map, "synDateStart");
        String synEndTime = jjUtil.handleParams(map, "synDateEnd");
        String accountType = jjUtil.handleParams(map, "account_type");
        List<String> accountTypeList = null;
        if (StringUtil.isNotEmpty(accountType)) {
            accountTypeList = Arrays.asList(accountType.split(","));
        }

        List list = hdBankStatementGfService.getBankAccountListBySql(moneyStart, moneyEnd, startTime, endTime,
                String.valueOf((Integer.parseInt(page) - 1) * Integer.parseInt(limit)), limit, map, companyId,tenantId, bankAccountId,accountTypeList,
                synStartTime, synEndTime);
        return list;
    }

    @ApiOperation(value = "分页联表查询", notes = "分页联表查询")
    @GetMapping(value = "/all2")
    public List<HdBankStatement> getAllBankAccountBySql(@RequestParam  Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        jjUtil.handleParamsClean(params);
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        Integer tenantId = TenantContextHolder.getTenantId();
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        String synStartTime = jjUtil.handleParams(params, "synDateStart");
        String companyId = jjUtil.handleParams(params, "company_id");
        String synEndTime = jjUtil.handleParams(params, "synDateEnd");
        String accountId = jjUtil.handleParams(params, "bank_account_id");
        String accountType = jjUtil.handleParams(params, "account_type");
        List<String> accountTypeList = null;
        if (StringUtil.isNotEmpty(accountType)) {
            accountTypeList = Arrays.asList(accountType.split(","));
        }
        List list = hdBankStatementGfService.getAllBankAccountBySql(moneyStart, moneyEnd, startTime, endTime,
                 params, companyId,tenantId, accountId,accountTypeList,
                synStartTime, synEndTime);
        return  list;
    }



    @ApiOperation(value = "分页联表查询", notes = "分页联表查询")
    @GetMapping(value = "/other")
    public Map<String, Object> getOtherVal(@RequestParam  Map<String, Object> params) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        jjUtil.handleParamsClean(params);
        jjUtil.handleParams(params, "page");
        jjUtil.handleParams(params, "limit");
        Integer tenantId = TenantContextHolder.getTenantId();
        String startTime = jjUtil.handleParams(params, "dateStart");
        String endTime = jjUtil.handleParams(params, "dateEnd");
        String moneyStart = jjUtil.handleParams(params, "moneyStart");
        String moneyEnd = jjUtil.handleParams(params, "moneyEnd");
        String synStartTime = jjUtil.handleParams(params, "synDateStart");
        String companyId = jjUtil.handleParams(params, "company_id");
        String synEndTime = jjUtil.handleParams(params, "synDateEnd");
        String accountId = jjUtil.handleParams(params, "bank_account_id");
        String accountType = jjUtil.handleParams(params, "account_type");
        List<String> accountTypeList = null;
        if (StringUtil.isNotEmpty(accountType)) {
            accountTypeList = Arrays.asList(accountType.split(","));
        }
        Map<String, Object> map = hdBankStatementGfService.getOtherVal(moneyStart, moneyEnd, startTime, endTime, params,
                companyId, tenantId, accountId, accountTypeList, synStartTime, synEndTime);
        return map;
    }

    ;

    /**
     * 获取期初值
     *
     * @return
     */
    @ApiOperation(value = "获取期初值", notes = "获取期初值")
    @GetMapping(value = "/getBalance")
    public String getBalance(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        String accountId = jjUtil.handleParams(map, "accountId");
        String accountDate = jjUtil.handleParams(map, "accountDate");
        String by2 = jjUtil.handleParams(map, "by2");
        jjUtil.handleParamsClean(map);
        return hdBankStatementGfService.getBalance(tenantId.toString(), accountId, accountDate, by2);
    }

    /**
     * 获取全部银行期初值
     *
     * @return
     */
    @ApiOperation(value = "获取全部银行期初值", notes = "获取全部银行期初值")
    @GetMapping(value = "/getAllBalance")
    public List<Map> getAllBalance(@RequestParam Map<String, Object> map) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        String accountIds = hdBankStatementGfService.getAllAccountId();
        List<String> accountList = Arrays.asList(accountIds.split(","));
        String accountDate = jjUtil.handleParams(map, "accountDate");
        String by2 = jjUtil.handleParams(map, "by2");
        jjUtil.handleParamsClean(map);
        return hdBankStatementGfService.getAllBalance(tenantId, accountList, accountDate, Integer.parseInt(by2));
    }

    /**
     * 获取最大编号
     *
     * @return
     */
    @ApiOperation(value = "获取最大编号", notes = "获取最大编号")
    @GetMapping(value = "/selectMaxNo")
    public synchronized BigDecimal selectMaxNo(@RequestParam(required = false) String accountDate) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        Integer tenantId = TenantContextHolder.getTenantId();
        if (accountDate == null) {
            return hdBankStatementGfService.selectMaxNo(tenantId);
        } else {
            return hdBankStatementGfService.selectMaxNoByDate(accountDate, tenantId);
        }
    }

    /**
     * 批量插入
     *
     * @return
     */
    @ApiOperation(value = "批量插入", notes = "批量插入")
    @PostMapping(value = "/addList")
    @PreAuthorize("@pms.hasPermission('hdbankstatementgf_import')")
    public synchronized R addList(@RequestBody List<HdBankStatement> list) {
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
        for (HdBankStatement hdBankStatementTy :
                list) {
//           BigDecimal bd =  hdBankStatementGfService.selectMaxNo(tenantId);
            hdBankStatementTy.setCreateBy(createBy);
            hdBankStatementTy.setTenantId(tenantId);
            hdBankStatementTy.setCreateTime(new Date());
//            hdBankStatementTy.setNo(bd);
        }

        boolean b = hdBankStatementGfService.saveBatch(list);
        if (b) {
            return R.ok("true");
        } else {
            return R.failed("false");
        }

    }

    /**
     * 批量删除
     *
     * @return
     */
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @GetMapping(value = "/deleteBatch")
    @PreAuthorize("@pms.hasPermission('hdbankstatementgf_batch_del')")
    public boolean deleteBatch(List<String> list) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.removeByIds(list);
    }



    @GetMapping(value = "/getBankReportBalance")
    public String getBankReportBalance(@RequestParam String startTime, @RequestParam String tenantId,
                                       @RequestParam String bankAccountId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getBankReportBalance(startTime, tenantId, bankAccountId);
    }



    @GetMapping(value = "/getComReportBalance")
    public String getComReportBalance(@RequestParam String startTime, @RequestParam String tenantId,
                                      @RequestParam String companyId) {
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceId.BANK_ACCOUNT_GF);
        return hdBankStatementGfService.getComReportBalance(startTime, tenantId, companyId);
    }

}
