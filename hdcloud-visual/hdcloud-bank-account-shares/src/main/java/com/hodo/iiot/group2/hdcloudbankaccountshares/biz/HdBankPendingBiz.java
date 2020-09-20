package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankAccount;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankAccountFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankPendingFeign;
import com.hodo.iiot.group2.hdcloudbankaccountshares.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HdBankPendingBiz {

    @Autowired
    HdBankAccountFeign hdBankAccountFeign;
    @Autowired
    HdBankPendingFeign hdBankPendingFeign;


    public HdBankPending addAccountInfo(HdBankPending hdBankPending){
        String accountId = hdBankPending.getBankAccountId();
        if(StringUtil.isNotEmpty(accountId)){
            //根据银行账户id获取外部账户
            HdBankAccount hdNzDict = hdBankAccountFeign.selectById(accountId).getData();
            if(hdNzDict!=null){
                hdBankPending.setBankAccountId(hdNzDict.getExternalBankAccountId());
                hdBankPending.setBankName(hdNzDict.getBankName());
            }
        }
        return hdBankPending;
    }


    //新增加入银行账户
    public List<HdBankPending> addBankAccount(List<HdBankPending> hdBankPendingList){
        if(hdBankPendingList!=null&&hdBankPendingList.size()>0){
            for(HdBankPending hdBankPending:hdBankPendingList){
                hdBankPending = addAccountInfo(hdBankPending);
            }
        }
        return hdBankPendingList;
    }


    public List<HdBankPending> getAllBankPending(Map<String, Object> params) {
        List<HdBankPending> allHdBankPendings = hdBankPendingFeign.getAllBankPending(params);
        packBankPendingList(allHdBankPendings);
        return allHdBankPendings;
    }



    private void packBankPendingList(List<HdBankPending> hdBankPendings){
        if(hdBankPendings!=null&&hdBankPendings.size()>0){
            for(HdBankPending hdBankPending:hdBankPendings){
                String bankId = hdBankPending.getBankName();
                if(com.github.wxiaoqi.security.common.util.StringUtil.isNotEmpty(bankId)){
                    HdBankAccount hdBank = hdBankAccountFeign.selectById(bankId).getData();
                    if(hdBank!=null){
                        hdBankPending.setBankName(hdBank.getBankName());
                    }
                }
            }
        }
    }



    public void batchSave(List<HdBankPending> hdBankPendingList) {

        while (hdBankPendingList.size() > 1000) {
            List<HdBankPending> hdBankPendings = hdBankPendingList.subList(0, 999);
            hdBankPendingFeign.addList(hdBankPendings);
            hdBankPendingList.removeAll(hdBankPendings);
        }
        hdBankPendingFeign.addList(hdBankPendingList);
    }


}
