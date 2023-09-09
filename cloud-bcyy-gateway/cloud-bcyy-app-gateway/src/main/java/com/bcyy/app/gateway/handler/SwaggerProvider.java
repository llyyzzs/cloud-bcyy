package com.bcyy.app.gateway.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
    /**
     * swagger2默认的url后缀
     */
    private static final String SWAGGER2_URL = "/v2/api-docs";

    /**
     * 路由定位器
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String gatewayName;

    /**
     * 获取 Swagger 资源
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 1. 获取路由Uri 中的Host=> 服务注册则为服务名=》app-service001
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> !gatewayName.equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));
        // 2. 创建自定义资源
        for (String routeHost : routeHosts) {
            String serviceUrl = "/" + routeHost + SWAGGER2_URL; // 后台访问添加服务名前缀
            SwaggerResource swaggerResource = new SwaggerResource(); // 创建Swagger 资源
            swaggerResource.setUrl(serviceUrl); // 设置访问地址
            swaggerResource.setName(routeHost); // 设置名称
            swaggerResource.setSwaggerVersion("3.0.0");
            resources.add(swaggerResource);
        }
        return resources;
    }
}

