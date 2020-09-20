package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.impl;


import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base.CommonService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBillService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2019-10-29.
 */
public class CommonServiceImpl<T> implements CommonService<T> {
    @Autowired
    protected RemoteBaseService remoteBaseService;
    @Autowired
    protected RemoteBillService remoteBillService;
    public CommonServiceImpl() {

    }
    // 分页查询
    //頁面查詢
    public R page(Map<String,Object> params){
        return null;
    }
    //查询id
    public R getById(Serializable id){
        return null;
    }
    //编辑
    public R updateById(T entity){
        return null;
    }
    //增加
    public R save(T entity){
        return null;
    }
    //删除
    public R removeById(Serializable id){
        return null;
    }
}
