package com.hodo.iiot.group2.data.hdcloud.bank.account.report;

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
@MapperScan("com.hodo.iiot.group2.data.hdcloud.bank.account.report.mapper")
public class DataHdcloudBankAccountReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataHdcloudBankAccountReportApplication.class, args);
    }

}
