package com.bcyy.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.apis.item.DetailsItemApi;
import com.bcyy.apis.user.WxUserApi;
import com.bcyy.chat.mapper.ChatRoomMapper;
import com.bcyy.chat.service.ChatRoomService;
import com.bcyy.model.chat.dto.UpRoom;
import com.bcyy.model.chat.pojos.ChatRoom;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.vos.ItemDvo;
import com.bcyy.model.user.dtos.WeChat;
import com.bcyy.model.user.pojos.MyChat;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoom> implements ChatRoomService {
    @Autowired
    ChatRoomMapper chatRoomMapper;
    @Autowired
    WxUserApi wxUserApi;
    @Autowired
    DetailsItemApi detailsItemApi;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 添加聊天室
     * */
    @GlobalTransactional
    public ResponseResult addRoom(String itemId){
        String id = UUID.randomUUID().toString();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setCreated( new Date());
        chatRoom.setId(id);
        chatRoom.setCount(0);
        chatRoomMapper.insert(chatRoom);
        Object data = detailsItemApi.getDetails(itemId).getData();
        String s = JSON.toJSONString(data);
        ItemDvo itemDvo = JSON.parseObject(s, ItemDvo.class);
        WeChat weChat = new WeChat();
        weChat.setHrId(itemDvo.getHrId());
        weChat.setOpenid(AppThreadLocalUtil.getUser().getOpenid());
        weChat.setRoomId(id);
        weChat.setName(itemDvo.getHRname());
        weChat.setItemId(itemId);
//        rabbitTemplate.convertAndSend("bcyy_chat","user",weChat);
        wxUserApi.addCommunicate(weChat);
        return ResponseResult.okResult(id);

    }
    /**
     * 修改聊天室
     * */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "upRoom"),
            exchange = @Exchange(value = "bcyy_chat",type = ExchangeTypes.DIRECT),
            key = "room"
    ))
    public ResponseResult upRoom(UpRoom upRoom){
        ChatRoom chatRoom = new ChatRoom();
        if (upRoom.getCount() != null) {
            chatRoom.setCount(upRoom.getCount());
        }else {
            ChatRoom chatRoom1 = chatRoomMapper.selectById(upRoom.getRoomId());
            BeanUtils.copyProperties(chatRoom1,chatRoom);
            chatRoom.setCount(chatRoom.getCount()+1);
        }
        if (upRoom.getCreated() != null) {
            chatRoom.setCreated(upRoom.getCreated());
        }
        if (upRoom.getDescription() != null) {
            chatRoom.setDescription(upRoom.getDescription());
        }
        if (upRoom.getName() != null) {
            chatRoom.setName(upRoom.getName());
        }
        chatRoomMapper.update(chatRoom,new QueryWrapper<ChatRoom>()
                .eq("id",upRoom.getRoomId()));
        return ResponseResult.okResult(chatRoom.getId());
    }
    /**
     * 删除聊天室
     * */
    public ResponseResult delete(String id){
        chatRoomMapper.deleteById(id);
        return ResponseResult.errorResult(200,"删除成功");
    }
    /**
    * 获取聊天室
     * */
    public ResponseResult getRoom(){
        HashSet<MyChat> communicate = wxUserApi.getCommunicate();
        HashSet<ChatRoom> roomList = new HashSet<>();
        for (MyChat myChat:communicate) {
            ChatRoom chatRoom = chatRoomMapper.selectById(myChat.getRoomId());
            chatRoom.setName(myChat.getName());
            roomList.add(chatRoom);
        }
        return ResponseResult.okResult(roomList);
    }
}
