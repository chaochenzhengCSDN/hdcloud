package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.util.EntityUtils;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdTransferBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdTransferBankStatementFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.rest.HdBankAccountController;
import com.hodo.iiot.group2.hdcloudbankaccountshares.rest.HdBankStatementGfController;
import com.hodo.iiot.group2.hdcloudbankaccountshares.rest.HdCompanyController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HdTransferBankStatementBiz {

    @Autowired
    private HdTransferBankStatementFeign hdTransferBankStatementFeign;

    @Autowired
    HdBankStatementGfBiz hdBankStatementGfBiz;

    @Autowired
    HdTransferBankStatementBiz hdTransferBankStatementBiz;

    @Autowired
    HdBankStatementGfController hdBankStatementGfController;

    @Autowired
    HdBankAccountController hdBankAccountController;

    @Autowired
    HdCompanyController hdCompanyController;


//    @Autowired
//    private HdBanktypeBiz hdBanktypeBiz;



    //重新新增增加序号,在从当前时间获取最大值
    public void insertEntity(HdTransferBankStatement entity) {
        EntityUtils.setCreatAndUpdatInfo(entity);
        BigDecimal money = entity.getMoney();
        if (money == null || "".equals(money)) {
            entity.setMoney(BigDecimal.ZERO);
        }
        Integer no = hdBankStatementGfBiz.getNoMaxCurrentTime();
        entity.setNo(new BigDecimal(no));
        //获取最大值
        hdTransferBankStatementFeign.insertSelective(entity);
        //插入银行账单,生成两条账单记录插入
        //封装两条记录
        saveAccount(entity);
    }




    public synchronized Integer getNoMaxCurrentTime() {
        BigDecimal maxNo = null;
        int No = 1;
        maxNo = hdTransferBankStatementFeign.selectMaxNo();
        if (maxNo != null) {
            No = maxNo.intValue() + 1;
        }
        //entity.setNo(Integer.valueOf(No));
        return Integer.valueOf(No);
    }



    public void deleteAccount(String id){
        //删除内账
        hdTransferBankStatementFeign.removeById(id);
        //删除内账关联账单
        hdBankStatementGfController.deleteBySourceId(id);
    }


    public String getSubject(String name){
        HdCompany hdCompany = hdCompanyController.getComByName(name);
        if(hdCompany!=null){
            return hdCompany.getId();
        }
        return null;
    }



    public void batchSave(List<HdTransferBankStatement> hdNzbankAccounts){
        Integer no = hdBankStatementGfBiz.getNoMaxCurrentTime();
        for(HdTransferBankStatement hdNzbankAccount:hdNzbankAccounts){
            hdNzbankAccount.setNo(new BigDecimal(no));
            hdTransferBankStatementFeign.insertSelective(hdNzbankAccount);
            saveAccount(hdNzbankAccount);
            no++;
        }
    }



    //根据id封装name和code
    public List<String> packNameAndCodeById(String id){
        HdCompany hdCompany = hdCompanyController.getComById(id);
        List<String> result = new ArrayList<>();
        if(hdCompany!=null){
            result.add(hdCompany.getName());
            result.add(hdCompany.getCode());
        }
        return result;
    }




    public String packNzno(String id){
        HdBankAccount hdNzDict = hdBankAccountController.getNzDictById(id);
        if(hdNzDict!=null){
            return hdNzDict.getExternalBankAccountId();
        }
        return "无效";
    }


    //根据id获取实体类
    public String packComIdAndName(String id){
        HdCompany hdCompany = hdCompanyController.getComById(id);
        if(hdCompany!=null){
            return hdCompany.getName();
        }
        return "无效";
    }



    public String packDictIdAndName(String id){
        HdBankAccount hdNzDict = hdBankAccountController.getNzDictById(id);
        if(hdNzDict!=null){
            return hdNzDict.getBankName();
        }
        return "无效";
    }



    //重新封装序号
    public List<HdTransferBankStatement> formatAllNo(List<HdTransferBankStatement> hdNzbankAccounts){
        if(hdNzbankAccounts!=null&&hdNzbankAccounts.size()>0){
            for(HdTransferBankStatement hdNzbankAccount:hdNzbankAccounts){
                formatNo(hdNzbankAccount,6);
            }
        }
        return hdNzbankAccounts;
    }



    public void formatNo(HdTransferBankStatement hdNzbankAccount,Integer num){
        String no = String.valueOf(hdNzbankAccount.getNo());
        while(no.length()<num){
            no = "0"+no;
        }
        hdNzbankAccount.setBy1(no);
    }



    public void updateAccount(HdTransferBankStatement hdNzbankAccount){
        hdTransferBankStatementFeign.updateSelectiveById(hdNzbankAccount);
        //先删除后添加
        hdBankStatementGfController.deleteBySourceId(hdNzbankAccount.getId());
        saveAccount(hdNzbankAccount);
    }



    public void saveAccount(HdTransferBankStatement hdNzbankAccount){
        HdBankStatement incomeAccount = packIncomeAccount(hdNzbankAccount);
        HdBankStatement payAccount = packPayAccount(hdNzbankAccount);
        List<HdBankStatement> bankAccountList = new ArrayList<>();
        bankAccountList.add(incomeAccount);
        bankAccountList.add(payAccount);
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(bankAccountList));
        hdBankStatementGfController.addNzAccount(array.toString());
    }







    /**
     * 封装生成的两条账单
     * @param hdNzbankAccount
     * @return
     */
    public HdBankStatement packIncomeAccount(HdTransferBankStatement hdNzbankAccount) {
        HdBankStatement hdBankAccount = new HdBankStatement();
        //凭证编号重新生成
        //借方科目就是借方公司
        hdBankAccount.setCompanyId(hdNzbankAccount.getIncomeSubjectName());
        //对方科目
        hdBankAccount.setSubjects(hdNzbankAccount.getPaySubjectId());
        hdBankAccount.setIncome(hdNzbankAccount.getMoney());
        hdBankAccount.setPay(BigDecimal.ZERO);
        hdBankAccount.setSourceId(hdNzbankAccount.getId());
//        hdBankAccount.setCreateBy(BaseContextHandler.getUserID());
        hdBankAccount.setTenantId(TenantContextHolder.getTenantId());
        hdBankAccount.setAccountDate(new Date());
        hdBankAccount.setSourceType("3");
        hdBankAccount.setAccountType("2");
        hdBankAccount.setRemark(hdNzbankAccount.getRemark());
        hdBankAccount.setNo(hdNzbankAccount.getNo());
        hdBankAccount.setBy2(String.valueOf(hdNzbankAccount.getNo().doubleValue()+0.1));
        //根据开户行获取科目，我方对方一样
        //根据开户行获取对应银行
        HdBankAccount hdNzDict = hdBankAccountController.getNzDictById(hdNzbankAccount.getBankAccountId());
        if(hdNzDict!=null){

            //hdBankAccount.setMysubjects(hdNzDict.getZhname());
            hdBankAccount.setBankName(hdNzDict.getBankName());
            //hdBankAccount.setBankname(hdNzDict.getBank());
            hdBankAccount.setBankAccountId(hdNzDict.getId());
        }
        return hdBankAccount;
    }

    public HdBankStatement packPayAccount(HdTransferBankStatement hdNzbankAccount) {
        HdBankStatement hdBankAccount = new HdBankStatement();
        //凭证编号重新生成
        hdBankAccount.setCompanyId(hdNzbankAccount.getPaySubjectName());
        //对方科目
        hdBankAccount.setSubjects(hdNzbankAccount.getIncomeSubjectId());
        hdBankAccount.setIncome(BigDecimal.ZERO);
        hdBankAccount.setPay(hdNzbankAccount.getMoney());
        hdBankAccount.setSourceId(hdNzbankAccount.getId());
//        hdBankAccount.setCreateBy(BaseContextHandler.getUserID());
        hdBankAccount.setTenantId(TenantContextHolder.getTenantId());
        hdBankAccount.setAccountDate(new Date());
        hdBankAccount.setSourceType("3");
        hdBankAccount.setAccountType("2");
        hdBankAccount.setRemark(hdNzbankAccount.getRemark());
        hdBankAccount.setNo(hdNzbankAccount.getNo());
        hdBankAccount.setBy2(String.valueOf(hdNzbankAccount.getNo().doubleValue()+0.2));
        HdBankAccount hdNzDict = hdBankAccountController.getNzDictById(hdNzbankAccount.getBankAccountId());
        if(hdNzDict!=null){
            //hdBankAccount.setSubjects(hdNzDict.getZhname());
            //hdBankAccount.setMysubjects(hdNzDict.getZhname());
            hdBankAccount.setBankName(hdNzDict.getBankName());
            //hdBankAccount.setBankname(hdNzDict.getBank());
            hdBankAccount.setBankAccountId(hdNzDict.getId());
        }
        return hdBankAccount;
    }






}
