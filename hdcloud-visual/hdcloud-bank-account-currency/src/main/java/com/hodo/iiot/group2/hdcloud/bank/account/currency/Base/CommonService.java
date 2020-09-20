package com.hodo.iiot.group2.hdcloud.bank.account.currency.Base;

import com.hodo.hdcloud.common.core.util.R;
import com.hodo.iiot.group2.hdcloud.bank.account.currency.feign.RemoteBaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2019-10-29.
 */
public interface CommonService<T> {
    //頁面查詢
    public R<T> page(Map<String,Object> params);
    //查询id
    R<T> getById(Serializable id);
    //编辑
    R updateById(T entity);
    //增加
    R save(T entity);
    //删除
    R removeById(Serializable id);

}