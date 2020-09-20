package com.hodo.iiot.group2.data.hdcloud.cash.account.bill;

import com.hodo.hdcloud.common.datasource.annotation.EnableDynamicDataSource;
import com.hodo.hdcloud.common.security.annotation.EnableHodoFeignClients;
import com.hodo.hdcloud.common.security.annotation.EnableHodoResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;


@SpringCloudApplication
@EnableHodoFeignClients
@EnableHodoResourceServer
@EnableAutoConfiguration
@EnableDynamicDataSource
@MapperScan("com.hodo.iiot.group2.data.hdcloud.cash.account.bill.mapper")
public class DataHdcloudCashAccountBillApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataHdcloudCashAccountBillApplication.class, args);
    }

}
