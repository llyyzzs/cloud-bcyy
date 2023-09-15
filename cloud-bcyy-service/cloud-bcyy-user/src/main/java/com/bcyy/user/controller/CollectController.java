package com.bcyy.user.controller;

import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dtos.WeChat;
import com.bcyy.model.user.pojos.MyChat;
import com.bcyy.user.service.impl.CollectServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@RestController
@RequestMapping("/user")
@Api(tags = "我的记录")
public class CollectController {
    @Autowired
    CollectServiceImpl collectService;


    @ApiOperation("添加收藏")
    @GetMapping("/addCollect")
    public ResponseResult addCollect(@RequestParam("id") String id){
        return  collectService.addCollect(id);
    }

    @ApiOperation("取消收藏")
    @GetMapping("/deleteCollect")
    public ResponseResult deleteCollect(@RequestParam("id") String id){
        return  collectService.deleteCollect(id);
    }

    @ApiOperation("获取收藏")
    @GetMapping("/getCollect")
    public ResponseResult getCollect(){
        return  collectService.getCollect();
    }

    @ApiOperation("获取某个招聘的收藏状态")
    @GetMapping("/getCollectOne")
    public ResponseResult getCollectOne(String itemId){
        return collectService.getCollectOne(itemId);
    }
//    @ApiOperation("添加沟通")
//    @PostMapping("/addCommunicate")
//    public ResponseResult addCommunicate(@RequestBody WeChat weChat){
//        return  collectService.addCommunicate(weChat);
//    }


    @ApiOperation("获取沟通")
    @GetMapping("/getCommunicate")
    public HashSet<MyChat> getCommunicate(){
        return  collectService.getCommunicate();
    }

    @ApiOperation("添加投递")
    @PostMapping("/addDeliver")
    public ResponseResult addDeliver(String id, MultipartFile multipartFile){
        return  collectService.addDeliver(id,multipartFile);
    }

    @ApiOperation("取消投递")
    @GetMapping("/deleteDeliver")
    public ResponseResult deleteDeliver(@RequestParam("id") String id){
        return  collectService.deleteDeliver(id);
    }

    @ApiOperation("获取投递")
    @GetMapping("/getDeliver")
    public ResponseResult getDeliver(){
        return  collectService.getDeliver();
    }

    @ApiOperation("更新投递状态")
    @GetMapping("/upDeliver")
    public ResponseResult upDeliver(@RequestParam("openid")String openid,@RequestParam("itemId") String itemId,@RequestParam("status") Integer status){
        return  collectService.upDeliver(openid,itemId,status);
    }
}
