package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.util.EntityUtils;
import com.github.wxiaoqi.security.common.util.MyBeanUtils;
import com.github.wxiaoqi.security.common.util.StringUtil;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.*;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankAccountFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankPendingFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankStatementGfFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdCompanyFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.DbUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HdBankStatementGfBiz {

    @Autowired
    HdBankStatementGfFeign hdBankStatementGfFeign;

    @Autowired
    HdCompanyFeign hdCompanyFeign;

    @Autowired
    HdBankAccountFeign hdBankAccountFeign;

    @Autowired
    HdBankPendingFeign hdBankPendingFeign;

    @Autowired
    HdBankAccountBiz hdBankAccountBiz;

    @Autowired
    HdMatchCompanyBiz hdMatchCompanyBiz;

    @Autowired
    HdBankRecordBiz hdBankRecordBiz;

    @Autowired
    HdBankPendingBiz hdBankPendingBiz;



    //根据公司id查询账单是否存在否则无法删除公司
    public List<HdBankStatement> getAccountsByCom(String companyId){
        Map<String, Object> map = new HashedMap();
        map.put("companyId",companyId);
        return hdBankStatementGfFeign.getHdBankStatementGf(map);
    }

    //根据公司id查询账单是否存在否则无法删除公司
    public List<HdBankStatement> getAccountsByBank(String bankAccountId){
        Map<String, Object> map = new HashedMap();
        map.put("bankAccountId",bankAccountId);
        return hdBankStatementGfFeign.getHdBankStatementGf(map);
    }



    //重新新增增加序号,从所选时间获取最大值
    public void insertEntityFreeTime(HdBankStatement entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        BigDecimal income = entity.getIncome();
        BigDecimal pay = entity.getPay();
        if (income == null || "".equals(income)) {
            entity.setIncome(BigDecimal.ZERO);
        }
        if (pay == null || "".equals(pay)) {
            entity.setPay(BigDecimal.ZERO);
        }
        getNoMaxFreeTime(entity);
        List<HdBankStatement> list = new ArrayList<>();
        list.add(entity);
        hdBankStatementGfFeign.addList(list);
    }





    public synchronized void getNoMaxFreeTime(HdBankStatement entity) {
        Integer selfNo = 0;
        if(entity.getNo()!=null){
            selfNo = entity.getNo().intValue();
        }

        Integer maxNo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int No = 1;
        if (StringUtil.isNotEmpty(entity.getCompanyId())) {
            //如果无法获取时间以当前时间获取最大值
            String accountDateStr = sdf.format(entity.getAccountDate());
            if (entity.getAccountDate() != null) {
                maxNo = hdBankStatementGfFeign.selectMaxNoFreeTime(accountDateStr);
            } else {
                maxNo = hdBankStatementGfFeign.selectMaxNo().intValue();
            }

        }
        if (maxNo != null) {
            if(selfNo!=null&&selfNo==maxNo){
                No = maxNo;
            }else{
                No = maxNo + 1;
            }

        }
        entity.setBy2(String.valueOf(No));
        entity.setNo(new BigDecimal(No));
    }




    //批量增加
    public synchronized int batchSave(List<HdBankStatement> accountList) {
        int b = 1;
        String bankName = "";
        for(int i=0;i<accountList.size();i++){
            bankName = hdBankAccountFeign.selectById(accountList.get(i).getBankAccountId()).getData().getBankName();
            if(bankName!=null&&!"".equals(bankName)){
                accountList.get(i).setBankName(bankName);
            }
            bankName = "";
        }

        while (accountList.size() > 1000) {
            List<HdBankStatement> hdBankRecordList = accountList.subList(0, 999);
            b = hdBankStatementGfFeign.addList(hdBankRecordList).getCode();
            accountList.removeAll(hdBankRecordList);
        }
        b = hdBankStatementGfFeign.addList(accountList).getCode();
        return b;
    }




    //新增加入银行账户
    public List<HdBankStatement> addBankAccount(List<HdBankStatement> hdBankAccountList){
        if(hdBankAccountList!=null&&hdBankAccountList.size()>0){
            for(HdBankStatement hdBankAccount:hdBankAccountList){
                formatNo(hdBankAccount);
                //对方科目
                String subject = hdBankAccount.getSubjects();
                if(StringUtil.isNotEmpty(subject)){
                    HdCompany hdCompany = hdCompanyFeign.selectById(subject).getData();
                    if(hdCompany!=null){
                        hdBankAccount.setSubjects(hdCompany.getName());
                    }
                }
                String accountId = hdBankAccount.getBankAccountId();
                if(StringUtil.isNotEmpty(accountId)){
                    //根据银行账户id获取外部账户
                    HdBankAccount hdNzDict = hdBankAccountFeign.selectById(accountId).getData();
                    if(hdNzDict!=null){
                        hdBankAccount.setBankAccountId(hdNzDict.getExternalBankAccountId());
                        hdBankAccount.setBankName(hdNzDict.getBankName());
                    }
                }
            }
        }
        return hdBankAccountList;
    }



    public void formatNo(HdBankStatement hdBankStatement){
        String no = String.valueOf(hdBankStatement.getNo());
        while(no.length()<6){
            no = "0"+no;
        }
        hdBankStatement.setBy1(no);
    }



    //重新新增增加序号,在从当前时间获取最大值
    public int insertEntity(HdBankStatement entity) {
        int b = 1;
        EntityUtils.setCreatAndUpdatInfo(entity);
        BigDecimal income = entity.getIncome();
        BigDecimal pay = entity.getPay();
        if (income == null || "".equals(income)) {
            entity.setIncome(BigDecimal.ZERO);
        }
        if (pay == null || "".equals(pay)) {
            entity.setPay(BigDecimal.ZERO);
        }
        getNoMaxCurrentTime(entity);
        List<HdBankStatement> hdBankAccounts = new ArrayList<>();
        hdBankAccounts.add(entity);
        b = this.batchSave(hdBankAccounts);
        return b;
    }




    /**
     * 获取hd_account表中最大编号用于比较插入
     *
     * @return
     */
    public synchronized void getNoMaxCurrentTime(HdBankStatement entity) {
        Integer maxNo = null;
        int No = 1;
        if (StringUtil.isNotEmpty(entity.getCompanyId())) {
            maxNo = hdBankStatementGfFeign.selectMaxNo()!=null?hdBankStatementGfFeign.selectMaxNo().intValue():null;
        }
        if (maxNo != null) {
            No = maxNo + 1;
        }
        entity.setBy2(String.valueOf(No));
        entity.setNo(new BigDecimal(No));
    }



    public String Search() {
        //String sql = "SELECT DISTINCT REMARK from HD_BANK_ACCOUNT WHERE REMARK IS NOT NULL";
        List<String> list = hdBankStatementGfFeign.searchRemark(String.valueOf(TenantContextHolder.getTenantId()),null);
        String str = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                str += (String) list.get(i) + ",";
            }
        }
        return str;
    }



    //导入时获取最大值
    public synchronized Integer getNoMaxCurrentTime() {
        Integer maxNo = null;
        int No = 1;
        maxNo = hdBankStatementGfFeign.selectMaxNo().intValue();
        if (maxNo != null) {
            No = maxNo + 1;
        }
        return No;
    }




    //内部互转同步
    public Integer synNZData(List<HdBankStatement> accountList,
                             List<HdBankPending> pendingList,
                             List<String> sheetIdInBankAccount,
                             List<String> sheetIdInBankPend,
                             String query_begin, String query_end) {
        int num = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rsn = null;
        try {
            conn = DbUtil.getConnection();
            //查询所有内部账单号然后拼接查询条件
            String sql = sqlBuild(sqlBuildParam1());
            ps = conn.prepareStatement(sql);
            ps.setString(1, query_begin);
            ps.setString(2, query_end);
            ResultSet rs = ps.executeQuery();
            String companyA = "";
            String companyB = "";
            while (rs.next()) {
                if (!sheetIdInBankAccount.contains(rs.getString(1)) &&
                        !sheetIdInBankPend.contains(rs.getString(1))) {
                    String explain = "";
                    if (StringUtil.isNotEmpty(rs.getString(5))) {
                        explain = rs.getString(5).trim();
                    }
                    //System.out.println("互相内转explain:" + explain+",income:" + rs.getString(6));
                    if (explain != null) {
                        if (getCompanyExplain(explain).contains("付")) {
                            companyA = getCompanyName(getCompanyExplain(explain), "pay");
                            companyB = getCompanyName(getCompanyExplain(explain), "income");
                            //查询A公司和B公司
//                            HdCompany hdCompanyA = hdCompanyBiz.getCompanyByName(companyA);
//                            HdCompany hdCompanyB = hdCompanyBiz.getCompanyByName(companyB);
                            Map<String,Object> map = new HashedMap();
                            map.put("code",companyA);
                            HdCompany hdCompanyA = hdCompanyFeign.getHdCompanyList(map).get(0);
                            map.clear();
                            map.put("code",companyB);
                            HdCompany hdCompanyB = hdCompanyFeign.getHdCompanyList(map).get(0);
                            if (hdCompanyA != null) {
                                //支出公司存在,加入账单数组
                                accountList.add(
                                        saveAccount(rs.getDate(4),
                                                explain, new BigDecimal("0"), new BigDecimal(rs.getString(6)),
                                                hdCompanyA.getId(), rs.getString(1), rs.getString(41),"1",
                                                rs.getString(8),rs.getString(40),BaseContextHandler.getUserID()));
                            } else {
                                //支出公司不存在，加入待处理数组
                                pendingList.add(savePend(rs.getDate(4),
                                        explain, new BigDecimal("0"), new BigDecimal(rs.getString(6)),
                                        "待处理", rs.getString(1),
                                        rs.getString(8), rs.getString(40), rs.getString(41),"1"));
                            }
                            if (hdCompanyB != null) {
                                //收入公司存在，加入账单数组
                                accountList.add(saveAccount(rs.getDate(4),
                                        explain, new BigDecimal(rs.getString(6)), new BigDecimal("0"),
                                        hdCompanyB.getId(), rs.getString(1), rs.getString(40),"1",
                                        rs.getString(10),rs.getString(41),BaseContextHandler.getUserID()));
                            } else {
                                //收入公司不存在，加入待处理数组
                                pendingList.add(savePend(rs.getDate(4),
                                        explain, new BigDecimal(rs.getString(6)), new BigDecimal("0"),
                                        "待处理", rs.getString(1),
                                        rs.getString(10), rs.getString(41), rs.getString(40),"1"));
                            }
                        } else {
                            //摘要第一个逗号前字符串不带付字
                            pendingList.add(savePend(rs.getDate(4), explain, new BigDecimal("0"), new BigDecimal(rs.getString(6)), "待处理", rs.getString(1), rs.getString(8), rs.getString(40), rs.getString(41),"1"));
                            pendingList.add(savePend(rs.getDate(4), explain, new BigDecimal(rs.getString(6)), new BigDecimal("0"), "待处理", rs.getString(1), rs.getString(10), rs.getString(41), rs.getString(40),"1"));
                        }

                    } else {
                        //摘要为空
                        pendingList.add(savePend(rs.getDate(4), explain, new BigDecimal("0"), new BigDecimal(rs.getString(6)), "待处理", rs.getString(1), rs.getString(8), rs.getString(40), rs.getString(41),"1"));
                        pendingList.add(savePend(rs.getDate(4), explain, new BigDecimal(rs.getString(6)), new BigDecimal("0"), "待处理", rs.getString(1), rs.getString(10), rs.getString(41), rs.getString(40),"1"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rsn != null) {
                try {
                    rsn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return num;
    }





    //内部转外部同步,收入
    public Integer synNZIncome(List<HdBankStatement> accountList,
                               List<HdBankPending> pendingList,
                               List<String> sheetIdInBankAccount,
                               List<String> sheetIdInBankPend,
                               String query_begin, String query_end) {
        int num = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rsn = null;
        try {
            conn = DbUtil.getConnection();
            String sqlIncome = sqlBuild(sqlBuildParam3());
            ps = conn.prepareStatement(sqlIncome);
            ps.setString(1, query_begin);
            ps.setString(2, query_end);
            ResultSet rs = ps.executeQuery();
            String companyName = "";
            while (rs.next()) {
                if (!sheetIdInBankAccount.contains(rs.getString(1)) &&
                        !sheetIdInBankPend.contains(rs.getString(1))) {
                    String explain = "";
                    if (StringUtil.isNotEmpty(rs.getString(5))) {
                        explain = rs.getString(5).trim();
                    }
                    //System.out.println("内转收入explain:" + explain+",income:" + rs.getString(6));
                    if (explain != null) {
                        //获取收支公司名，第一个逗号之前的字符串带付字
                        //根据是否带付字获取收入公司名称：
                        //有付字，截取付字之前的为收入公司名称；无付字，直接为收入公司名称
                        if (getCompanyExplain(explain).contains("付")) {
                            companyName = getCompanyName(getCompanyExplain(explain), "income");
                        } else {
                            companyName = getCompanyExplain(explain);
                        }
                        //HdCompany hdCompany = hdCompanyBiz.getCompanyByName(companyName);
                        Map<String,Object> map = new HashedMap();
                        map.put("code",companyName);
                        HdCompany hdCompany = hdCompanyFeign.getHdCompanyList(map).get(0);
                        if (hdCompany != null) {
                            //收入公司存在，加入账单数组
                            accountList.add(saveAccount(rs.getDate(4), explain , new BigDecimal(rs.getString(6)),
                                    new BigDecimal("0"), hdCompany.getId(),
                                    rs.getString(1), rs.getString(40),"1",rs.getString(10),rs.getString(41),BaseContextHandler.getUserID()));
                        } else {
                            //收入公司不存在，加入待处理数组
                            pendingList.add(savePend(rs.getDate(4), explain, new BigDecimal(rs.getString(6)),
                                    new BigDecimal("0"), "待处理",
                                    rs.getString(1), rs.getString(10),
                                    rs.getString(41), rs.getString(40),"1"));
                        }
                    } else {
                        pendingList.add(savePend(rs.getDate(4), explain,
                                new BigDecimal(rs.getString(6)),
                                new BigDecimal("0"), "待处理",
                                rs.getString(1), rs.getString(10),
                                rs.getString(41), rs.getString(40),"1"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rsn != null)
                try {
                    rsn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return num;
    }





    //内部转外部同步,支出
    public Integer synNZPay(List<HdBankStatement> accountList,
                            List<HdBankPending> pendingList,
                            List<String> sheetIdInBankAccount,
                            List<String> sheetIdInBankPend,
                            String query_begin, String query_end) {
        int num = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rsn = null;
        try {
            conn = DbUtil.getConnection();
            String sqlPay = sqlBuild(sqlBuildParam2());

            ps = conn.prepareStatement(sqlPay);
            ps.setString(1, query_begin);
            ps.setString(2, query_end);
            ResultSet rs = ps.executeQuery();
            String companyName = "";
            while (rs.next()) {
                if (!sheetIdInBankAccount.contains(rs.getString(1)) &&
                        !sheetIdInBankPend.contains(rs.getString(1))) {
                    String explain = "";
                    if (StringUtil.isNotEmpty(rs.getString(5))) {
                        explain = rs.getString(5).trim();
                    }
                    if (explain != null) {
                        //System.out.println("内转支出explain:" + rs.getString(5) + ",money:" + rs.getString(6));
                        //获取收支公司名，第一个逗号之前的字符串带付字
                        //根据是否带付字获取支出公司名称：
                        //有付字，截取付字之前的为支出公司名称；无付字，直接为支出公司名称
                        if (getCompanyExplain(explain).contains("付")) {
                            companyName = getCompanyName(getCompanyExplain(explain), "pay");
                        } else {
                            companyName = getCompanyExplain(explain);
                        }
                        //匹配支出公司
                        //HdCompany hdCompany = hdCompanyBiz.getCompanyByName(companyName);
                        Map<String,Object> map = new HashedMap();
                        map.put("code",companyName);
                        HdCompany hdCompany = hdCompanyFeign.getHdCompanyList(map).get(0);
                        if (hdCompany != null) {
                            //支出公司存在，加入账单数组
                            if (StringUtil.isNotEmpty(rs.getString(21))) {
                                explain += rs.getString(21);
                            } else {
                                explain += rs.getString(41);
                            }
                            accountList.add(saveAccount(rs.getDate(4),
                                    explain, new BigDecimal("0"), new BigDecimal(rs.getString(6)),
                                    hdCompany.getId(), rs.getString(1), rs.getString(41),"1",rs.getString(8),rs.getString(40),
                                    BaseContextHandler.getUserID()));
                        } else {
                            //支出公司不存在，加入待处理数组
                            pendingList.add(savePend(rs.getDate(4),
                                    explain,
                                    new BigDecimal("0"), new BigDecimal(rs.getString(6))
                                    , "待处理", rs.getString(1),
                                    rs.getString(8), rs.getString(40),
                                    rs.getString(41),"1"));
                        }
                    } else {
                        pendingList.add(savePend(rs.getDate(4),
                                explain,
                                new BigDecimal("0"), new BigDecimal(rs.getString(6))
                                , "待处理", rs.getString(1),
                                rs.getString(8), rs.getString(40),
                                rs.getString(41),"1"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rsn != null) {
                try {
                    rsn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return num;
    }




    //外转
    public Integer synWZ(List<HdBankStatement> accountList,
                         List<HdBankPending> pendingList,
                         List<String> rIdInBankAccount,
                         List<String> rIdInBankPend,
                         String query_begin, String query_end) {
        int num = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        HdBankStatement hdBankAccountAdd = null;
        HdCompany hdCompany = null;
        try {
            String sqlWZ = sqlBuildWZ(sqlBuildWZParam(), query_begin, query_end);
            System.out.println(sqlWZ+"----------------");
            conn = DbUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlWZ);
            while (rs.next()) {
                String info = "";
                String companyId = "";
                String explain = "";
                String opAcntName = "";
                String acntName = "";
                if (StringUtil.isNotEmpty(rs.getString(1))) {
                    explain = rs.getString(1).trim();
                }
                if (StringUtil.isNotEmpty(rs.getString(6))) {
                    opAcntName = rs.getString(6).trim();
                }
                if (StringUtil.isNotEmpty(rs.getString(8))) {
                    acntName = rs.getString(8).trim();
                }
                if (!rIdInBankAccount.contains(rs.getString(3)) &&
                        !rIdInBankPend.contains(rs.getString(3))) {
                    //1、先按摘要匹配
                    info = explain;
                    //System.out.println("外转账单explain:" + explain+",money:" + rs.getString(5));
                    if (explain != null) {
                        //九恒星中中国银行摘要会自动变成OBSS011310531050GIRO000001209448内二//内二;内二
                        //针对此情况提取匹配公司
                        //摘要匹配公司
                        if (rs.getString(7).equals("中国银行")&&
                                explain.contains("//")) {
                            Pattern p = Pattern.compile("\\//(.*?)\\,");;//正则表达式，取=和|之间的字符串，不包括=和|
                            explain = explain.replace("，",",");
                            Matcher m = p.matcher(explain);
                            if( m.find() ){
                                System.out.println( m.group(1) );
                                String companyName = getCompanyName(getCompanyExplain(m.group(1)), "pay");
                                //HdCompany hdCompany = hdCompanyBiz.getCompanyByName(companyName);
                                Map<String,Object> map = new HashedMap();
                                map.put("code",companyName);
                                hdCompany = hdCompanyFeign.getHdCompanyList(map).get(0);
                                if (hdCompany != null) {
                                    companyId = hdCompany.getId();
                                }
                            }else{
                                System.out.println(" 没有匹配到内容....");
                            }
                        } else {
                            String companyName = getCompanyName(getCompanyExplain(explain), "pay");
                            //HdCompany hdCompany = hdCompanyBiz.getCompanyByName(companyName);
                            Map<String,Object> map = new HashedMap();
                            map.put("code",companyName);
                            hdCompany = hdCompanyFeign.getHdCompanyList(map).get(0);
                            if (hdCompany != null) {
                                companyId = hdCompany.getId();
                            }
                        }
                        info += ":" + companyId;
                        if (StringUtil.isNotEmpty(companyId)) {
                            // 如果匹配成功
                            hdBankAccountAdd = getHdBankEntity(rs, companyId,BaseContextHandler.getUserID());
                            if (hdBankAccountAdd != null) {
                                accountList.add(hdBankAccountAdd);
                            }
                            //一旦匹配成功，无论结果如何，此次循环结束
                            continue;
                        }
                        //2、再按外单位匹配
                        info += ";" + opAcntName + ":" + acntName;
                        if (StringUtil.isNotEmpty(opAcntName)) {
                            companyId = getMatchCompanyId(opAcntName, acntName);
                            info += ":" + companyId;
                            if (StringUtil.isNotEmpty(companyId)) {
                                //獲取的是公司code
                                Map<String,Object> map = new HashedMap();
                                map.put("code",companyId);
                                hdCompany = hdCompanyFeign.getHdCompanyList(map).get(0);
                                // 如果匹配外部维护公司
                                if(hdCompany!=null){
                                    hdBankAccountAdd = getHdBankEntity(rs, hdCompany.getId(),BaseContextHandler.getUserID());
                                    if (hdBankAccountAdd != null) {
                                        accountList.add(hdBankAccountAdd);
                                    }
                                }
                                //一旦匹配成功，无论结果如何，此次循环结束
                                continue;
                            }
                        }
                        info += "匹配失败";
                        // 进入待转数据
                        HdBankPending hdBankPending = new HdBankPending();
                        hdBankPending.setAccountDate(rs.getDate(4));
                        hdBankPending.setSynaccountDate(rs.getDate(4));
                        hdBankPending.setRid(rs.getString(3));
                        hdBankPending.setRemark(explain);

                        //银行通过外转id获取银行id和银行账户id
                        //hdBankPending.setBankname(rs.getString(7));
                        hdBankPending.setMySubjects(rs.getString(8));
                        hdBankPending.setCompanyName("待处理");
                        String dirflag = rs.getString(2);
                        hdBankPending.setSubjects(opAcntName);
                        //6.25
                        hdBankPending.setCreateBy(BaseContextHandler.getUserID());
                        hdBankPending.setTenantId(TenantContextHolder.getTenantId());
                        hdBankPending.setAccountType("0");
                        // 数值为支出
                        if ("1".equals(dirflag)) {
                            hdBankPending.setIncome(new BigDecimal("0.00"));
                            hdBankPending.setPay(new BigDecimal(rs.getString(5) == null ? "0.00" : rs.getString(5)));
                        }
                        // 数值为收入
                        if ("2".equals(dirflag)) {
                            hdBankPending.setPay(new BigDecimal("0.00"));
                            hdBankPending.setIncome(new BigDecimal(rs.getString(5) == null ? "0.00" : rs.getString(5)));
                        }
                        //7.2增加银行账户id用于统计
                        if(StringUtil.isNotEmpty(rs.getString(9))){
                            hdBankPending.setBankName(hdBankAccountBiz.getBankNameByWZId(rs.getString(9)));
                            hdBankPending.setBankAccountId(getHdNzDictByTerm(null,rs.getString(9)));
                            //System.out.println("测试4："+getHdNzDictByTerm(null,rs.getString(9))+"---------"+rs.getString(9));
                        }
                        //FileOperation.contentToTxt("c://info.txt", info);
                        pendingList.add(hdBankPending);
                    }else{
                        //如果摘要为空，如何处理
                        // 进入待转数据
                        HdBankPending hdBankPending = new HdBankPending();
                        hdBankPending.setAccountDate(rs.getDate(4));
                        hdBankPending.setSynaccountDate(rs.getDate(4));
                        hdBankPending.setRid(rs.getString(3));
                        hdBankPending.setRemark(explain);

                        //银行通过外转id获取银行id和银行账户id
                        //hdBankPending.setBankname(rs.getString(7));
                        hdBankPending.setMySubjects(rs.getString(8));
                        hdBankPending.setCompanyName("待处理");
                        String dirflag = rs.getString(2);
                        hdBankPending.setSubjects(opAcntName);
                        //6.25
                        hdBankPending.setCreateBy(BaseContextHandler.getUserID());
                        hdBankPending.setTenantId(TenantContextHolder.getTenantId());
                        hdBankPending.setAccountType("0");
                        // 数值为支出
                        if ("1".equals(dirflag)) {
                            hdBankPending.setIncome(new BigDecimal("0.00"));
                            hdBankPending.setPay(new BigDecimal(rs.getString(5) == null ? "0.00" : rs.getString(5)));
                        }
                        // 数值为收入
                        if ("2".equals(dirflag)) {
                            hdBankPending.setPay(new BigDecimal("0.00"));
                            hdBankPending.setIncome(new BigDecimal(rs.getString(5) == null ? "0.00" : rs.getString(5)));
                        }
                        //7.2增加银行账户id用于统计
                        if(StringUtil.isNotEmpty(rs.getString(9))){
                            hdBankPending.setBankName(hdBankAccountBiz.getBankNameByWZId(rs.getString(9)));
                            hdBankPending.setBankAccountId(getHdNzDictByTerm(null,rs.getString(9)));
                            //System.out.println("测试4："+getHdNzDictByTerm(null,rs.getString(9))+"---------"+rs.getString(9));
                        }
                        pendingList.add(hdBankPending);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null){
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null){
                    stmt.close();}
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null){
                    conn.close();}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return num;
    }




    //封裝handBankAccountList
    public void handBankAccountList(List<HdBankStatement> accountList, Date accountDay) {
        Map<String, Map<Integer, Object>> mapResult = new HashedMap();
        Integer maxNo = null;
        int No = 1;
        maxNo = hdBankStatementGfFeign.selectMaxNo().intValue();
        if (maxNo != null) {
            No = maxNo + 1;
        }
        for (HdBankStatement hdBankAccount : accountList) {
            try {
                //添加用户
//                String userId = BaseContextHandler.getUserID();
//                hdBankAccount.setCreateBy(userId);
                if (accountDay != null) {
                    hdBankAccount.setAccountDate(accountDay);
                }
                //如果年月日相同修改記賬日期
//                String nowD = null;
//                String accountDate = null;
//                nowD = DateUtils.date_sdf.format(new Date());
//                accountDate = DateUtils.date_sdf.format(new Date());
//                if(StringUtil.isNotEmpty(nowD)&&
//                        StringUtil.isNotEmpty(accountDate)){
//                    if(nowD.equals(accountDate)){
//                        hdBankAccount.setAccountDate(new Date());
//                    }
//                }
                //EntityUtils.setCreatAndUpdatInfo(hdBankAccount);
                BigDecimal income = hdBankAccount.getIncome();
                BigDecimal pay = hdBankAccount.getPay();
                if (income == null || "".equals(income)) {
                    hdBankAccount.setIncome(BigDecimal.ZERO);
                }
                if (pay == null || "".equals(pay)) {
                    hdBankAccount.setPay(BigDecimal.ZERO);
                }

                //逐个添加No
                hdBankAccount.setBy2(String.valueOf(No));
                hdBankAccount.setNo(new BigDecimal(No));
                No++;
//                if (mapResult.size() > 0) {
//                    System.out.println(hdBankAccount.getCompanyName());
//
//                    if (mapResult.keySet().contains(hdBankAccount.getCompanyName())) {
//                        //如果包含该公司在最大基础上+1
//                        //获取最大的key
//
//                        Set<Integer> numSet = mapResult.get(hdBankAccount.getCompanyName()).keySet();
//                        Integer maxNum = Collections.max(numSet) + 1;
//                        hdBankAccount.setNo(maxNum);
//                        mapResult.get(hdBankAccount.getCompanyName()).put(maxNum, hdBankAccount);
//                    } else {
//
//                        Map<Integer, Object> valueResult = new HashedMap();
//                        //获取最大值
//                        getNoMaxCurrentTime(hdBankAccount);
//                        valueResult.put(hdBankAccount.getNo(), hdBankAccount);
//                        mapResult.put(hdBankAccount.getCompanyName(), valueResult);
//                    }
//                } else {
//
//                    Map<Integer, Object> valueResult = new HashedMap();
//                    //获取最大值
//                    getNoMaxCurrentTime(hdBankAccount);
//                    valueResult.put(hdBankAccount.getNo(), hdBankAccount);
//                    mapResult.put(hdBankAccount.getCompanyName(), valueResult);
//                }
            } catch (Exception e) {
                System.out.println("錯誤信息" + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
    }







    private String sqlBuild(String sqlBuildParam) {
        return "SELECT * FROM NSTCSA.VW_CNTBUSSSHEET where " + sqlBuildParam +
                " AND ACTDATE between TO_DATE(?, 'yyyy-mm-dd') and " +
                "TO_DATE(?,'yyyy-mm-dd') AND (explain not in('资金上存','资金上收'," +
                "'用款','资金下拨','上收') or explain is null) and (extno is null or(extno is not null and extname is null and extbank is null)) order by ACTDATE desc";
    }

    private String sqlBuildWZ(String sqlBuildParam, String startTime,
                              String endTime) {
        return "select explain,DIRFLAG,rid,ACTDATE,amount,OpAcntName,BANKNAME,ACNTNAME,ACNTNO from NSTCSA.VW_BP_RECORD where "
                + sqlBuildParam + " AND ACTDATE between (to_date('"
                + startTime + "','yyyy-mm-dd'))" + " and (to_date('" + endTime
                + "','yyyy-mm-dd'))"
                + " and (explain not in('资金上存','资金上收','用款','资金下拨','上收') or explain is null) and (OpAcntName!='红豆集团财务有限公司' or OpAcntName is null) order by ACTDATE desc";
    }


    //拼接查询语句1
    private String sqlBuildParam1() {
        Map<String,Object> map = new HashedMap();
        List<HdBankAccount> hdNzDictList = hdBankAccountFeign.getHdBankAccountList(map);
        StringBuffer sb = new StringBuffer("(JNO IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(" and DNO IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(")");
        return sb.toString();
    }


    private String sqlBuildParam2() {
        Map<String,Object> map = new HashedMap();
        List<HdBankAccount> hdNzDictList = hdBankAccountFeign.getHdBankAccountList(map);
        StringBuffer sb = new StringBuffer("(JNO IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(" and DNO NOT IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(")");
        return sb.toString();
    }


    private String sqlBuildParam3() {
        Map<String,Object> map = new HashedMap();
        List<HdBankAccount> hdNzDictList = hdBankAccountFeign.getHdBankAccountList(map);
        StringBuffer sb = new StringBuffer("(JNO NOT IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(" and DNO IN(");
        sqlBuildNzDict(sb, hdNzDictList);
        sb.append(")");
        return sb.toString();
    }

    private String sqlBuildWZParam() {
        Map<String,Object> map = new HashedMap();
        List<HdBankAccount> hdNzDictList = hdBankAccountFeign.getHdBankAccountList(map);
        StringBuffer sb = new StringBuffer("(ACNTNO in(");
        sqlBuildWzDict(sb, hdNzDictList);
        sb.append(")");
        return sb.toString();
    }


    //拼接查询语句内转子类
    private StringBuffer sqlBuildNzDict(StringBuffer sb, List<HdBankAccount> hdNzDictList) {
        if (hdNzDictList != null) {
            for (HdBankAccount hdNzDict : hdNzDictList) {
                sb.append("'" + hdNzDict.getInternalBankAccountId() + "',");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        return sb;
    }


    //拼接查询语句外转子类
    private StringBuffer sqlBuildWzDict(StringBuffer sb, List<HdBankAccount> hdNzDictList) {
        if (hdNzDictList != null) {
            for (HdBankAccount hdNzDict : hdNzDictList) {
                sb.append(hdNzDict.getExternalBankAccountId() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        return sb;
    }



    //外转封装
    public HdBankStatement getHdBankEntity(ResultSet rs, String comId, String userId) throws SQLException {
        HdBankStatement hdBank = new HdBankStatement();
        hdBank.setAccountDate(rs.getDate(4));
        hdBank.setSynaccountDate(rs.getDate(4));
        hdBank.setCompanyId(comId);
        hdBank.setRid(rs.getString(3));
        hdBank.setRemark(rs.getString(1));
        hdBank.setSubjects(rs.getString(6));
        hdBank.setMySubjects(rs.getString(8));
        //6.25
        hdBank.setTenantId(TenantContextHolder.getTenantId());
        hdBank.setCreateBy(userId);
        hdBank.setAccountType("0");
        hdBank.setSourceType("0");
        String dirflag = rs.getString(2);
        // 数值为支出
        if ("1".equals(dirflag)) {
            hdBank.setIncome(new BigDecimal("0.00"));
            hdBank.setPay(new BigDecimal(rs.getString(5) == null ? "0.00" : rs.getString(5)));
        }
        // 数值为收入
        if ("2".equals(dirflag)) {
            hdBank.setPay(new BigDecimal("0.00"));
            hdBank.setIncome(new BigDecimal(rs.getString(5) == null ? "0.00" : rs.getString(5)));
        }
        //7.2增加银行账户id用于统计
        if(StringUtil.isNotEmpty(rs.getString(9))){
            hdBank.setBankName(hdBankAccountBiz.getBankNameByWZId(rs.getString(9)));
            hdBank.setBankAccountId(getHdNzDictByTerm(null,rs.getString(9)));
            System.out.println("测试3："+getHdNzDictByTerm(null,rs.getString(9))+"---------"+rs.getString(9));
        }

        return hdBank;
    }



    //判断是否含有逗号，若有去第一个逗号前的内容
    private static String getCompanyExplain(String explain) {
        int a = explain.indexOf(",");
        int b = explain.indexOf("，");
        if (a > 0 && b > 0) {
            if (a > b) {
                return explain.substring(0, b);
            } else {
                return explain.substring(0, a);
            }
        }
        if (a > 0 && b == -1) {
            return explain.substring(0, a);
        }
        if (a == -1 && b > 0) {
            return explain.substring(0, b);
        }
        return explain;
    }


    //获取支出或者收入公司名
    private static String getCompanyName(String explain, String flag) {
        //判断是否含有”付“字
        if (explain.indexOf("付") > 0) {
            if ("income".equals(flag)) {
                //收入
                explain = explain.substring(explain.indexOf("付") + 1);
            } else {
                //支出
                explain = explain.substring(0, explain.indexOf("付"));
            }
        }
        return explain;
    }



    // 判断是否匹配外部账户维护表
    private String getMatchCompanyId(String opAcntName, String acntname) {
        List<HdMatchCompany> matchComList = new ArrayList<HdMatchCompany>();
        //String hql = "from HdMatchCompanyEntity where customerName = '"+opAcntName+"'";
        String matchComName = "";
        //根据外部单位查找内部匹配公司
        if(StringUtil.isNotEmpty(opAcntName)){
            matchComList = hdMatchCompanyBiz.getMatchCompanyByTerm(opAcntName);
            if (matchComList != null && matchComList.size() > 0) {
                //循环找到对应的内公司名称
                for (HdMatchCompany matchCom : matchComList) {
//                //内部抬头不为空
//                if (StringUtil.isNotEmpty(matchCom.getInnerCompany())) {
//                    //内部抬头相匹配，则划分到对应公司
//                    if (acntname.equals(matchCom.getInnerCompany().trim())) {
//                        matchComName = matchCom.getCompanyName();
//                        //提前结束循环
//                        break;
//                    }
//                    //内部抬头不匹配，则跳过
//                } else {
//                    //内部抬头为空,直接确认公司
//                    matchComName = matchCom.getCompanyName();
//                }
                    if(StringUtil.isNotEmpty(matchCom.getCompanyCode())){
                        matchComName = matchCom.getCompanyCode();
                        break;
                    }
                }
            }
        }

        //没有匹配到结果返回“”
        return matchComName;
    }





    //从九恒星拉取数据进行封装
    private HdBankStatement saveAccount(
            Date date, String explain, BigDecimal income, BigDecimal pay,
            String companyName, String sheetId, String subjects, String accountType, String nzId,
            String mySubjects, String userId
    ) {
        HdBankStatement entity = new HdBankStatement();
        entity.setTenantId(TenantContextHolder.getTenantId());
        entity.setCreateBy(userId);
        entity.setAccountDate(date);
        entity.setSynaccountDate(date);
        entity.setRemark(explain);
        entity.setIncome(income);
        entity.setPay(pay);
        entity.setCompanyId(companyName);
        entity.setSubjects(subjects);
        entity.setSheetid(sheetId);
        entity.setAccountType(accountType);
        entity.setSourceType("0");
        //7.2增加银行账户id用于统计
        if(StringUtil.isNotEmpty(nzId)){
            String id = getHdNzDictByTerm(nzId,null);
            System.out.println("测试2："+nzId+"---------"+id);
            entity.setBankAccountId(id);
            //内转账号根据nzid获取银行
            String hdNzName = hdBankAccountBiz.getBankNameByNZId(nzId);
            String bankName = "";
            if (StringUtil.isNotEmpty(hdNzName)) {
                bankName = hdNzName;
            }
            entity.setBankName(bankName);
        }
        entity.setMySubjects(mySubjects);
        return entity;
    }




    //从九恒星拉取数据进行封装
    private HdBankPending savePend(
            Date date, String explain, BigDecimal income, BigDecimal pay,
            String companyName, String sheetId, String nzId,
            String mySubjects, String subjects,String accountType
    ) {
        HdBankPending entity = new HdBankPending();
        entity.setTenantId(TenantContextHolder.getTenantId());
//        entity.setCreateBy(BaseContextHandler.getUserID());
        entity.setAccountDate(date);
        entity.setSynaccountDate(date);
        entity.setRemark(explain);
        entity.setIncome(income);
        entity.setPay(pay);
        entity.setMySubjects(mySubjects);
        entity.setSubjects(subjects);
        //HdNzDict hdNzDict = hdNzDictBiz.selectById(nzId);
        entity.setCompanyName(companyName);
        entity.setSheetid(sheetId);
        entity.setAccountType(accountType);
        //7.2增加银行账户id用于统计
        if(StringUtil.isNotEmpty(nzId)){
            //内转账号根据nzid获取银行
            String hdNzName = hdBankAccountBiz.getBankNameByNZId(nzId);
            String bankName = "";
            if (StringUtil.isNotEmpty(hdNzName)) {
                bankName = hdNzName;
            }
            entity.setBankName(bankName);
            String id = getHdNzDictByTerm(nzId,null);
            System.out.println("测试1："+nzId+"---------"+id);
            entity.setBankAccountId(id);
        }
        return entity;
    }



    public Integer getSaveCount(String startTime, String endTime) {
        return hdBankStatementGfFeign.getSaveCount(startTime, endTime, TenantContextHolder.getTenantId());
    }


    public String getHdNzDictByTerm(String nzId,String wzId){
        return hdBankAccountBiz.getIdByTerm(nzId,wzId);
    }




    /*********************************标准版本二次修改*********************************/
    //6.26
    //分账处理
    public void handFenAccounts(List<HdBankStatement> fenAccounts){
        //如果分账中rid或者sheetid相同，按一笔还原
        Set<String> fenSheets = new HashSet<>();
        Set<String> fenRids = new HashSet<>();
        if(fenAccounts!=null&&fenAccounts.size()>0){
            for(HdBankStatement hdBankAccount:fenAccounts){
                if(StringUtil.isNotEmpty(hdBankAccount.getSheetid())){
                    fenSheets.add(hdBankAccount.getSheetid());
                }
                if(StringUtil.isNotEmpty(hdBankAccount.getRid())){
                    fenRids.add(hdBankAccount.getRid());
                }
            }
        }
        handFenAccountsBySheet("0",fenSheets);
        handFenAccountsBySheet("1",fenRids);
    }




    private void handFenAccountsBySheet(String flag,Set<String> ids){
        BigDecimal sumIncome = BigDecimal.ZERO;
        BigDecimal sumPay = BigDecimal.ZERO;
        List<HdBankStatement> hdBankAccounts = new ArrayList<>();
        List<HdBankRecord> hdBankRecords = new ArrayList<>();
        HdBankPending hdBankPending = new HdBankPending();
        try {
            if (ids != null && ids.size() > 0) {
                for (String id : ids) {
                    //根据sheetId获取对应所有账单
                    if("0".equals(flag)){
                        Map<String,Object> map = new HashedMap();
                        map.put("sheetid",id);
                        hdBankAccounts = hdBankStatementGfFeign.getHdBankStatementGf(map);
//                        hdBankAccounts = this.mapper.selAccountBySheet(id);
                    }else {
                        Map<String,Object> map = new HashedMap();
                        map.put("rid",id);
                        hdBankAccounts = hdBankStatementGfFeign.getAllBankAccountBySql(map);
//                        hdBankAccounts = this.mapper.selAccountByRid(id);
                    }
                    if (hdBankAccounts != null && hdBankAccounts.size() > 0) {
                        for (HdBankStatement hdBankAccount : hdBankAccounts) {
                            sumIncome = sumIncome.add(hdBankAccount.getIncome());
                            sumPay = sumPay.add(hdBankAccount.getPay());
                            hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(hdBankAccount, "分账后还原"));
                        }
                        //获取第一家公司账单，把值赋给还原的账单
                        HdBankStatement hdBankAccount0 = hdBankAccounts.get(0);
                        //生成待处理账单
                        MyBeanUtils.copyBeanNotNull2Bean(hdBankAccount0, hdBankPending);
                        if (sumIncome.subtract(sumPay).compareTo(BigDecimal.ZERO) > 0) {
                            //收入大于支出
                            hdBankPending.setIncome(sumIncome.subtract(sumPay));
                            hdBankPending.setPay(BigDecimal.ZERO);
                        } else {
                            //收入小于支出
                            hdBankPending.setIncome(BigDecimal.ZERO);
                            hdBankPending.setPay(sumPay.subtract(sumIncome));
                        }
                        hdBankPending.setCompanyName("待处理");
                        hdBankPending.setNo(null);
                        hdBankPending.setCreateBy(BaseContextHandler.getUserID());
                        hdBankPendingFeign.insertSelective(hdBankPending);
                        //批量删除账单
                        deleteAll(hdBankAccounts);
                    }
                    //批量记录操作信息
                    hdBankRecordBiz.batchSave(hdBankRecords);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //划账处理
    public void handHuaAccounts(List<HdBankStatement> huaAccounts){
        if(huaAccounts!=null&&huaAccounts.size()>0){
            HdBankPending hdBankPending = new HdBankPending();
            List<HdBankRecord> hdBankRecords = new ArrayList<>();
            try{
                for(HdBankStatement hdBankAccount:huaAccounts){
                    MyBeanUtils.copyBeanNotNull2Bean(hdBankAccount, hdBankPending);
                    hdBankPending.setCompanyName("待处理");
                    hdBankPending.setNo(null);
                    hdBankPending.setCreateBy(BaseContextHandler.getUserID());
                    if("2".equals(hdBankAccount.getSourceType())){
                        hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(hdBankAccount, "划账后还原"));
                    }else{
                        hdBankRecords.add(hdBankRecordBiz.getHdBankRecord(hdBankAccount, "九恒星还原"));
                    }

                    hdBankPendingFeign.insertSelective(hdBankPending);
                    hdBankStatementGfFeign.removeById(hdBankAccount.getId());
//                    this.mapper.delete(hdBankAccount);
                }
                //批量记录操作信息
                hdBankRecordBiz.batchSave(hdBankRecords);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }





    private void deleteAll(List<HdBankStatement> hdBankAccounts){
        if(hdBankAccounts!=null&&hdBankAccounts.size()>0){
            for(HdBankStatement hdBankAccount:hdBankAccounts){
                hdBankStatementGfFeign.removeById(hdBankAccount.getId());
//                this.mapper.delete(hdBankAccount);
            }
        }
    }



    //批量更新账单时间
    public void batchUpdate(List<HdBankStatement> hdBankAccounts){
        for(HdBankStatement hdBankAccount:hdBankAccounts){
            hdBankStatementGfFeign.updateSelectiveById(hdBankAccount);
//            this.mapper.updateByPrimaryKeySelective(hdBankAccount);
        }
    }




}
