package com.hodo.hdcloud.daemon.quartz;

import com.hodo.hdcloud.common.security.annotation.EnableHodoFeignClients;
import com.hodo.hdcloud.common.security.annotation.EnableHodoResourceServer;
import com.hodo.hdcloud.common.swagger.annotation.EnableHodoSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author frwcloud
 * @date 2019/01/23
 * 定时任务模块
 */
@EnableHodoSwagger2
@SpringCloudApplication
@EnableHodoFeignClients
@EnableHodoResourceServer
public class HodoDaemonQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(HodoDaemonQuartzApplication.class, args);
	}
}
