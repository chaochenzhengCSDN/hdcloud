package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdLoanAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankAccountFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdLoanAccountFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.rest.HdBankAccountController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HdLoanAccountBiz {

    @Autowired
    HdLoanAccountFeign loanAccountFeign;

    @Autowired
    HdBankAccountController hdBankAccountController;


    public void addBankInfo(HdLoanAccount hdLoanAccount){
        HdBankAccount hdNzDict = hdBankAccountController.getNzDictById(hdLoanAccount.getBankId());
        if(hdNzDict!=null){
            hdLoanAccount.setBankno(hdNzDict.getBankName());
            hdLoanAccount.setBankAccount(hdNzDict.getExternalBankAccountId());
        }
    }


    public String getIdByBankAccount(String bankAccount){
        return hdBankAccountController.getNzDictByWbzh(bankAccount);
    }


    public void addBankName(HdLoanAccount hdLoanAccount){
        HdBankAccount hdNzDict = hdBankAccountController.getNzDictById(hdLoanAccount.getBankId());
        if(hdNzDict!=null){
            hdLoanAccount.setBankno(hdNzDict.getBankName());
        }
    }


    public void batchSave(List<HdLoanAccount> hdLoanAccounts){
        for(HdLoanAccount hdLoanAccount:hdLoanAccounts){
            loanAccountFeign.insertSelective(hdLoanAccount);
        }
    }


}
