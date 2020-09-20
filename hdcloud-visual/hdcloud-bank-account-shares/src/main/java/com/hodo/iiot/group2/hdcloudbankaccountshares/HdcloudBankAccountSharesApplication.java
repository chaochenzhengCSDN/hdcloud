package com.hodo.iiot.group2.hdcloudbankaccountshares;

import com.hodo.hdcloud.common.security.annotation.EnableHodoFeignClients;
import com.hodo.hdcloud.common.security.annotation.EnableHodoResourceServer;
import com.hodo.hdcloud.common.swagger.annotation.EnableHodoSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;


@SpringCloudApplication
@EnableHodoFeignClients(basePackages = "com.hodo")
@EnableHodoResourceServer
@EnableAutoConfiguration
@EnableHodoSwagger2
//@MapperScan("com.hodo.iiot.group2.data.hdcloud.bank.account.base.mapper")
public class HdcloudBankAccountSharesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HdcloudBankAccountSharesApplication.class, args);
    }

}
