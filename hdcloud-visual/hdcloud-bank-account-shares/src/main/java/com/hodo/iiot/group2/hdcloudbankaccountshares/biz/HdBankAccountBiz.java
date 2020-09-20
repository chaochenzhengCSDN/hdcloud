package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.ag.core.context.BaseContextHandler;
import com.hodo.hdcloud.common.data.tenant.TenantContextHolder;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankAccountFeign;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HdBankAccountBiz {

    @Autowired
    HdBankAccountFeign hdBankAccountFeign;


    //检查银行是否存在关联账户，存在不可删除
    public Boolean checkBankId(String bankId){
        Map<String, Object> map = new HashedMap();
        map.put("bankCode",bankId);
        List<HdBankAccount> hdBankAccounts =  hdBankAccountFeign.getHdBankAccountList(map);
        if(hdBankAccounts.size()>0){
            return true;
        }
        return false;
    }


    //根据外转账号获取内转实体类id
    public String getIdByTerm(String nzId,String wzId){
        Map<String,Object> map = new HashedMap();
        map.put("internalBankAccountId",nzId);
        map.put("externalBankAccountId",wzId);
        HdBankAccount hdBankAccount = hdBankAccountFeign.getHdBankAccountList(map).get(0);
        if(hdBankAccount != null){
            return hdBankAccount.getId();
        }
//        return hdBankAccountFeign.getIdByTerm(nzId,wzId, TenantContextHolder.getTenantId());
        return  "";
    }


    //根据外转账号获取银行id
    public String getBankNameByNZId(String wzId){
        Map<String,Object> map = new HashedMap();
        map.put("externalBankAccountId",wzId);
        HdBankAccount hdBankAccount = hdBankAccountFeign.getHdBankAccountList(map).get(0);
        if(hdBankAccount != null){
            return  hdBankAccount.getBankName();
        }
//        return hdBankAccountFeign.getBankNameByWZId(wzId,null,TenantContextHolder.getTenantId());
        return "";
    }

    //根据外转账号获取银行id
    public String getBankNameByWZId(String wzId){
        Map<String,Object> map = new HashedMap();
        map.put("externalBankAccountId",wzId);
        HdBankAccount hdBankAccount = hdBankAccountFeign.getHdBankAccountList(map).get(0);
        if(hdBankAccount != null){
            return  hdBankAccount.getBankName();
        }
        return "";
    }

    public String getNzDictByWbzh(String wbzh){
        Map<String,Object> map = new HashedMap();
        map.put("externalBankAccountId",wbzh);
        HdBankAccount hdBankAccount = hdBankAccountFeign.getHdBankAccountList(map).get(0);
        if(hdBankAccount != null){
            return  hdBankAccount.getId();
        }
        return "";
    }



}
