package com.bcyy.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.bcyy.search.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.bcyy.apis")
public class SearchItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchItemApplication.class,args);
    }
}
