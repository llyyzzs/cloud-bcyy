package com.bcyy.chat.controller;

import com.bcyy.chat.service.impl.ChatMessageServiceImpl;
import com.bcyy.model.chat.dto.SendMessage;
import com.bcyy.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@Api(tags = "聊天信息管理")
public class ChatMessageController {
    @Autowired
    ChatMessageServiceImpl chatMessageService;

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public ResponseResult send(@RequestBody SendMessage sendMessage){
        return chatMessageService.send(sendMessage);
    }

    @ApiOperation("获取消息")
    @GetMapping("/getMessage")
    public ResponseResult getMessage(@RequestParam("roomId") String roomId){
        return chatMessageService.getMessage(roomId);
    }

    @ApiOperation("删除消息")
    @GetMapping("/deleteMessage")
    public ResponseResult delete(@RequestParam("id")Integer id){
        return chatMessageService.delete(id);
    }
}
