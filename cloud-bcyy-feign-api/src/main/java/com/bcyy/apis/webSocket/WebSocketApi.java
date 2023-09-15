package com.bcyy.apis.webSocket;

import com.bcyy.apis.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bcyy-webSocket",configuration = FeignConfig.class)
public interface WebSocketApi {
    //实时通信
    @GetMapping("/webSocker/push")
    void push(@RequestParam("message")String message, @RequestParam("uId")String uId);
}
