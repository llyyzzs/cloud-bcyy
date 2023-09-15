package com.bcyy.apis.chat;

import com.bcyy.apis.config.FeignConfig;
import com.bcyy.model.chat.dto.SendMessage;
import com.bcyy.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bcyy-chat",configuration = FeignConfig.class)
public interface ChatRoomApi {
    @GetMapping("/chat/addRoom")
    ResponseResult addRoom(@RequestParam("itemId") String itemId);
    @PostMapping("/chat/send")
    public ResponseResult send(@RequestBody SendMessage sendMessage);
}
