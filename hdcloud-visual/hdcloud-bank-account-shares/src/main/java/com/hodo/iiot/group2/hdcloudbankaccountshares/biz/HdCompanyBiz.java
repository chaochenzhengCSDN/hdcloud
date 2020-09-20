package com.hodo.iiot.group2.hdcloudbankaccountshares.biz;

import com.github.ag.core.context.BaseContextHandler;
import com.hodo.iiot.group2.hdcloudbankaccountshares.entity.HdCompany;
import com.hodo.iiot.group2.hdcloudbankaccountshares.feign.HdCompanyFeign;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class HdCompanyBiz{

    @Autowired
    private HdCompanyFeign hdCompanyFeign;

//    //根据公司id获取公司名称
//    public String getCompanyNameById(String companyId) {
//        return this.mapper.getCompanyNameById(companyId, BaseContextHandler.getTenantID(),null);
//    }
//
//    //根据公司名称查找该公司实体类
//    public HdCompany getCompanyByName(String companyName) {
//        return this.mapper.getCompanyByName(companyName, BaseContextHandler.getTenantID(),null);
//    }
//    //根据公司code查找该公司实体类
//    public HdCompany getCompanyByCode(String code){
//        return this.mapper.getCompanyByCode(code,BaseContextHandler.getTenantID());
//    }

//    //获取所有公司名称
//    public List<HdCompany> getAllCompanyNames() {
//        List<HdCompany> hdCompanies = this.mapper.getAllCompanyNames(BaseContextHandler.getTenantID(),null);
//        if (hdCompanies != null && hdCompanies.size() > 0) {
//            for (HdCompany hdCompany : hdCompanies) {
//                hdCompany.setValue(hdCompany.getCompanyName());
//            }
//        }
//        return hdCompanies;
//    }
//
//    public List<HdCompany> getAllCompanyEx(String company,String code,String tenantId) {
//        return this.mapper.getAllCompanyEx(company,code,tenantId,null);
//    }
//
//    @Transactional
//    public void batchSave(List<HdCompany> hdCompanies) throws Exception {
//        for (HdCompany hdCompany : hdCompanies) {
//            hdCompany.setTenantId(BaseContextHandler.getTenantID());
//            //hdCompany.setCreateBy(BaseContextHandler.getUserID());
//            hdCompany.setCreateTime(new Date());
//            super.insertSelective(hdCompany);
//        }
//    }




    @Transactional
    public int batchSave(List<HdCompany> hdCompanies) throws Exception {
        for (HdCompany hdCompany : hdCompanies) {
            hdCompany.setCreateTime(new Date());
        }
        return  hdCompanyFeign.addList(hdCompanies).getCode();
    }


    public List<HdCompany> getAllCompanyEx(String company, String code) {
        Map<String, Object> map = new HashedMap();
        map.put("companyName",company);
        map.put("code",code);

        return  hdCompanyFeign.getHdCompanyList(map);
    }


    //获取所有公司名称
    public List<HdCompany> getAllCompanyNames() {
        Map<String, Object> map = new HashedMap();
        List<HdCompany> hdCompanies = hdCompanyFeign.getHdCompanyList(map);
        if (hdCompanies != null && hdCompanies.size() > 0) {
            for (HdCompany hdCompany : hdCompanies) {
                hdCompany.setValue(hdCompany.getName());
            }
        }
        return hdCompanies;
    }



}