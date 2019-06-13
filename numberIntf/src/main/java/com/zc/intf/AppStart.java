package com.zc.intf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.zc.*.mapper")
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@EnableTransactionManagement
public class AppStart {
	public static void main(String[] args) {
		SpringApplication.run(AppStart.class, args);
	}

}
