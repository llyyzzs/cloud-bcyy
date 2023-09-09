package com.bcyy.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.apis.item.DetailsItemApi;
import com.bcyy.apis.webSocket.WebSocketApi;
import com.bcyy.file.service.FileStorageService;
import com.bcyy.model.chat.dto.SendMessage;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.vos.ItemDvo;
import com.bcyy.model.user.dtos.WeChat;
import com.bcyy.model.user.pojos.Collect;
import com.bcyy.model.user.pojos.MyChat;
import com.bcyy.user.mapper.CollectMapper;
import com.bcyy.user.service.CollectService;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@Service
@Transactional
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect>implements CollectService {
    @Autowired
    CollectMapper collectMapper;
    @Autowired
    DetailsItemApi detailsItemApi;
    @Autowired
    WebSocketApi webSocketApi;
    @Autowired
    FileStorageService fileStorageService;
    /*
    * 添加收藏
    * */
    public ResponseResult addCollect(String id){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        collect.getCollect().add(id);
        collect.setCollect(collect.getCollect());
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",openid));
        return ResponseResult.okResult(200,"添加成功");
    }
    /*
    * 取消收藏
    * */
    public ResponseResult deleteCollect(String id){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        if (id == null) {
            collect.getCollect().clear();
        }else {
            collect.getCollect().remove(id);
        }
        collect.setCollect(collect.getCollect());
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",openid));
        return ResponseResult.okResult(200,"取消成功");
    }
    /*
    * 获取收藏
    * */
    public ResponseResult getCollect(){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        HashSet<String> list = collect.getCollect();
        HashSet<Object> objects = new HashSet<>();
        for (String itemId:list) {
            objects.add(detailsItemApi.getDetails(itemId).getData());
        }
        return  ResponseResult.okResult(objects);
    }

    /*
    * 添加沟通
    * */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "communicate"),
//            exchange = @Exchange(name = "bcyy_chat",type = ExchangeTypes.DIRECT),
//            key = "user"
//    ))
    public ResponseResult addCommunicate(WeChat weChat){
//        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",weChat.getOpenid()));
        Collect collectHR = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid", weChat.getHrId()));
        MyChat myChat = new MyChat();
        myChat.setItemId(weChat.getItemId());
        myChat.setRoomId(weChat.getRoomId());
        myChat.setName(weChat.getName());
        //分别为HR和应聘者添加聊天室
        collect.getCommunicate().add(myChat);
        collectHR.getCommunicate().add(myChat);
        collect.setCommunicate(collect.getCommunicate());
        collectHR.setCommunicate(collectHR.getCommunicate());
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",weChat.getOpenid()));
        collectMapper.update(collectHR,new QueryWrapper<Collect>().eq("openid",weChat.getHrId()));
        return ResponseResult.okResult(200,"添加成功");
    }
    /*
     * 取消沟通
     * */
    public ResponseResult deleteCommunicate(String id){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        if (id == null) {
            collect.getCommunicate().clear();
        }else {
            collect.getCommunicate().remove(id);
        }
        collect.setCommunicate(collect.getCommunicate());
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",openid));
        return ResponseResult.okResult(200,"取消成功");
    }

    /*
     * 获取沟通
     * */
    public HashSet<MyChat>  getCommunicate(){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        HashSet<MyChat> list = collect.getCommunicate();
        return list;
    }
    /*
    * 添加投递
    * */
    public ResponseResult addDeliver(String itemId , MultipartFile multipartFile){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        collect.getDeliver().put(itemId,0);
        collect.setDeliver(collect.getDeliver());
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",openid));
        //获取HRid
        Object detailsItem =detailsItemApi.getDetails(itemId).getData();
        String s = JSON.toJSONString(detailsItem);
        ItemDvo itemDvo = JSON.parseObject(s, ItemDvo.class);
        String hrId = itemDvo.getHrId();
        String fileUrl = fileStorageService.fileUrl(multipartFile);
        HashSet<MyChat> communicate = collect.getCommunicate();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType(1);
        sendMessage.setContent(fileUrl);
        sendMessage.setSenderId(openid);
        sendMessage.setReceiverId(hrId);
        for (MyChat myChat:communicate) {
            if(myChat.getItemId()==itemId){
                sendMessage.setChatRoomId(myChat.getRoomId());
            }
        }
        try {
            webSocketApi.push(JSON.toJSONString(sendMessage), hrId);
        } catch (Exception e) {
            log.info("没有该用户");
        }
        return ResponseResult.okResult(200,"添加成功");
    }
    /*
     * 取消投递
     * */
    public ResponseResult deleteDeliver(String id){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        collect.setOpenid(openid);
        if (id == null) {
            collect.getDeliver().clear();
        }else {
            collect.getDeliver().remove(id);
        }
        collect.setDeliver(collect.getDeliver());
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",openid));
        return ResponseResult.okResult(200,"取消成功");
    }
    /*
     * 获取投递
     * */
    public ResponseResult getDeliver(){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>().eq("openid",openid));
        HashMap<String,Integer> list = collect.getDeliver();
        HashSet<Object> objects = new HashSet<>();
        for (String itemId:list.keySet()) {
            Object data = detailsItemApi.getDetails(itemId).getData();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("item",data);
            hashMap.put("status",list.get(itemId));
            objects.add(hashMap);
        }
        return  ResponseResult.okResult(objects);
    }
    /*
    * 修改投递状态
    * */
    public ResponseResult upDeliver(String openid,String itemId,Integer status){
        Collect collect = collectMapper.selectOne(new QueryWrapper<Collect>()
                .eq("openid",openid));
        HashMap<String,Integer> list = collect.getDeliver();
        list.put(itemId,status);
        collect.setDeliver(list);
        collectMapper.update(collect,new QueryWrapper<Collect>().eq("openid",openid));
        return ResponseResult.okResult(200,"更新成功");
    }
}
