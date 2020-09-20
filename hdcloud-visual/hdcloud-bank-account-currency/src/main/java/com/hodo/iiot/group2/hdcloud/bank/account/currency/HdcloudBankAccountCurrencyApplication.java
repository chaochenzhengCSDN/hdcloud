package com.hodo.iiot.group2.hdcloud.bank.account.currency;


import com.hodo.hdcloud.common.datasource.DataSourceAutoConfiguration;
import com.hodo.hdcloud.common.security.annotation.EnableHodoFeignClients;
import com.hodo.hdcloud.common.security.annotation.EnableHodoResourceServer;
import com.hodo.hdcloud.common.swagger.annotation.EnableHodoSwagger2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@SpringCloudApplication
@EnableHodoResourceServer
@EnableHodoSwagger2
@EnableHodoFeignClients(basePackages = "com.hodo")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@MapperScan("com.hodo.iiot.group2.hdcloud.bank.account.currency.mapper")
public class HdcloudBankAccountCurrencyApplication {
    public static void main(String[] args) {
        SpringApplication.run(HdcloudBankAccountCurrencyApplication.class, args);
    }

}
