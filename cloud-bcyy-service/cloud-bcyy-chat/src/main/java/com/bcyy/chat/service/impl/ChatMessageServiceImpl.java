package com.bcyy.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.apis.webSocket.WebSocketApi;
import com.bcyy.chat.mapper.ChatMessageMapper;
import com.bcyy.chat.service.ChatMessageService;
import com.bcyy.model.chat.dto.SendMessage;
import com.bcyy.model.chat.dto.UpRoom;
import com.bcyy.model.chat.pojos.ChatMessage;
import com.bcyy.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    @Autowired
    ChatMessageMapper chatMessageMapper;
    @Autowired
    ChatRoomServiceImpl chatRoomService;
    @Autowired
    WebSocketApi webSocketApi;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     * */
    public ResponseResult send(SendMessage sendMessage){
        ChatMessage chatMessage = new ChatMessage();
        BeanUtils.copyProperties(sendMessage,chatMessage);
        chatMessage.setCreatedAt(new Date());
        chatMessageMapper.insert(chatMessage);
        try {
            webSocketApi.push(JSON.toJSONString(sendMessage),sendMessage.getReceiverId());
        } catch (Exception e) {
            log.info("用户不在线");
        }
        //更新聊天室的最后一条消息
        UpRoom upRoom = new UpRoom();
        upRoom.setRoomId(sendMessage.getChatRoomId());
        upRoom.setDescription(sendMessage.getContent());
        upRoom.setCreated(new Date());
        upRoom.setType(sendMessage.getType());
//        rabbitTemplate.convertAndSend("bcyy_chat","room",upRoom);
        chatRoomService.upRoom(upRoom);
        return ResponseResult.okResult(200,"发送成功");
    }
    /**
     * 获取消息
     * */
    public ResponseResult getMessage(String roomId){
        List<ChatMessage> chatMessages = chatMessageMapper.selectList(
                new QueryWrapper<ChatMessage>()
                .eq("chat_room_id", roomId)
                .orderByAsc("created_at"));
        UpRoom upRoom = new UpRoom();
        upRoom.setCount(0);
        upRoom.setRoomId(roomId);
//        rabbitTemplate.convertAndSend("bcyy_chat","room",upRoom);
        chatRoomService.upRoom(upRoom);
        return ResponseResult.okResult(chatMessages);
    }
    /**
     * 删除消息
     * */
    public ResponseResult delete(Integer id){
        chatMessageMapper.deleteById(id);
        return ResponseResult.okResult(200,"删除成功");
    }
}
