package com.bcyy.search2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.bcyy.search2.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bcyy.apis")
public class SearchCompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchCompanyApplication.class,args);
    }
}
