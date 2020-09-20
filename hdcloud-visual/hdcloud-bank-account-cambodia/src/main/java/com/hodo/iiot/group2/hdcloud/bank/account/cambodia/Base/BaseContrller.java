package com.hodo.iiot.group2.hdcloud.bank.account.cambodia.Base;


import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBaseService;
import com.hodo.iiot.group2.hdcloud.bank.account.cambodia.feign.RemoteBillService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2019-11-08.
 */
public class BaseContrller {
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected RemoteBillService remoteBillService;
    @Autowired
    protected RemoteBaseService remoteBaseService;
}
