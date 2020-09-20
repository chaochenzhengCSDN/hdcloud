package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankPending;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankRecord;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdBankStatement;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdBankRecordFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class HdBankRecordBiz {

    @Autowired
    HdBankRecordFeign hdBankRecordFeign;




    public void formatNo(HdBankRecord hdBankRecord){
        String no = String.valueOf(hdBankRecord.getNum());
        while(no.length()<6){
            no = "0"+no;
        }
        hdBankRecord.setBy1(no);
    }



    /**
     * 封装操作记录
     */
    public HdBankRecord getHdBankRecord(HdBankStatement hdBankAccount, String type) {
        boolean flag = true;
        if (hdBankAccount.getPay() != null) {
            BigDecimal bpay = hdBankAccount.getPay() != null ? hdBankAccount.getPay() : BigDecimal.ZERO;
            flag = bpay.compareTo(BigDecimal.ZERO) == 0;
        }
        HdBankRecord hdBankRecord = new HdBankRecord();
        hdBankRecord.setCompanyId(hdBankAccount.getCompanyId());
        hdBankRecord.setFlag(flag ? "0" : "1");
        hdBankRecord.setNum(hdBankAccount.getNo() != null ? hdBankAccount.getNo().toString() :
                "");
        hdBankRecord.setMoney(flag ? hdBankAccount.getIncome() != null ? hdBankAccount.getIncome().toString() :
                "" : hdBankAccount.getPay() != null ? hdBankAccount.getPay().toString() :
                "");
        //新增賬戶信息
        hdBankRecord.setBankAccountId(hdBankAccount.getBankAccountId());
        hdBankRecord.setRemark(hdBankAccount.getRemark());
        hdBankRecord.setBankAccountId(hdBankAccount.getBankAccountId());
        hdBankRecord.setOperType(type);
        hdBankRecord.setCreateDate(new Date());
//        hdBankRecord.setCreateName(BaseContextHandler.getName());
//        hdBankRecord.setCreateBy(BaseContextHandler.getUserID());
//        hdBankRecord.setTenantId(BaseContextHandler.getTenantID());
        return hdBankRecord;
    }




    public HdBankRecord getHdBankRecord(HdBankPending HdBankPending, String type) {
        boolean flag = true;
        if (HdBankPending.getPay() != null) {
            BigDecimal bpay = HdBankPending.getPay() != null ? HdBankPending.getPay() : BigDecimal.ZERO;
            flag = bpay.compareTo(BigDecimal.ZERO) == 0;
        }
        HdBankRecord hdBankRecord = new HdBankRecord();
        hdBankRecord.setCompanyId(HdBankPending.getCompanyName());
        hdBankRecord.setFlag(flag ? "0" : "1");
        hdBankRecord.setNum(HdBankPending.getNo() != null ? HdBankPending.getNo().toString() :
                "");
        hdBankRecord.setMoney(flag ? HdBankPending.getIncome() != null ? HdBankPending.getIncome().toString() :
                "" : HdBankPending.getPay() != null ? HdBankPending.getPay().toString() :
                "");
        hdBankRecord.setRemark(HdBankPending.getRemark());
        hdBankRecord.setOperType(type);
        hdBankRecord.setCreateDate(new Date());
        hdBankRecord.setBankAccountId(HdBankPending.getBankAccountId());
//        hdBankRecord.setCreateName(BaseContextHandler.getName());
//        hdBankRecord.setCreateBy(BaseContextHandler.getUserID());
//        hdBankRecord.setTenantId(BaseContextHandler.getTenantID());
        return hdBankRecord;
    }





    public void batchSave(List<HdBankRecord> hdBankRecords) {
        while (hdBankRecords.size() > 100) {
            List<HdBankRecord> hdBankRecordList = hdBankRecords.subList(0, 100);
            hdBankRecordFeign.addList(hdBankRecordList);
            hdBankRecords.removeAll(hdBankRecordList);
        }
        hdBankRecordFeign.addList(hdBankRecords);
    }


}
