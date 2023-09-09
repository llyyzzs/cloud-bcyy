package com.bcyy.app.gateway.filter;

import com.bcyy.app.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request，response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //是否是登录

        if (request.getURI().getPath().contains("/login")
                || request.getURI().getPath().contains("/get/")
                || request.getURI().getPath().contains("/search")
                || request.getURI().getPath().contains("/webSocket")
                || request.getURI().getPath().contains("/v2/api-docs")){
            return chain.filter(exchange);
        }
        //获取token
        String token = request.getHeaders().getFirst("token");
        //判断token是否存在
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //校验token
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int i = AppJwtUtil.verifyToken(claimsBody);
            if (i==2||i==1){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            //获取用户id
            Object userId = claimsBody.get("id");
            //将id添加到头部
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId + "");
            }).build();
            //替换成新的头部
            exchange.mutate().request(serverHttpRequest).build();
        }catch (Exception e){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //6.放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置  值越小  优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
