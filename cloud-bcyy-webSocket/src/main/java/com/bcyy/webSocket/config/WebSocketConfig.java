package com.bcyy.webSocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@Slf4j
public class WebSocketConfig {
    /**
     * 给spring容器注入这个ServerEndpointExporter对象
     * 相当于xml：
     * <beans>
     * <bean id="serverEndpointExporter" class="org.springframework.web.socket.server.standard.ServerEndpointExporter"/>
     * </beans>
     * <p>
     * 检测所有带有@serverEndpoint注解的bean并注册他们。
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("serverEndpointExporter被注入了");
        return new ServerEndpointExporter();
    }
}
