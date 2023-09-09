package com.bcyy.chat.controller;

import com.bcyy.chat.service.impl.ChatRoomServiceImpl;
import com.bcyy.model.chat.dto.UpRoom;
import com.bcyy.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@Api(tags = "聊天室管理")
public class ChatRoomController {
    @Autowired
    ChatRoomServiceImpl chatRoomService;

    @ApiOperation("添加聊天室")
    @GetMapping("/addRoom")
    public ResponseResult addRoom(@RequestParam("itemId") String itemId){
        return chatRoomService.addRoom(itemId);
    }

    @ApiOperation("修改聊天室")
    @PostMapping("/upRoom")
    public ResponseResult upRoom(@RequestBody UpRoom upRoom){
        return chatRoomService.upRoom(upRoom);
    }

    @ApiOperation("删除聊天室")
    @GetMapping("/deleteRoom")
    public ResponseResult delete(@RequestParam("id") String id){
        return chatRoomService.delete(id);
    }

    @ApiOperation("获取聊天室")
    @GetMapping("/getRoom")
    public ResponseResult getRoom(){
        return chatRoomService.getRoom();
    }
}
