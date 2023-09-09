package com.bcyy.webSocket.controller;

import com.bcyy.webSocket.websocket.WebSocketServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webSocker")
public class WebSocketController {
    @Autowired
    WebSocketServerUtil socketServer;
    @GetMapping("/push")
    public void push(@RequestParam("message")String message,
                     @RequestParam("uId")String uId){
        try {
            socketServer.sendInfo(message,uId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
