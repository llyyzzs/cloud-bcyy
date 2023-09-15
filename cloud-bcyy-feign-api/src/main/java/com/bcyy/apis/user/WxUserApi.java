package com.bcyy.apis.user;

import com.bcyy.apis.config.FeignConfig;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.dtos.WeChat;
import com.bcyy.model.user.pojos.MyChat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;

@FeignClient(value = "bcyy-user",configuration = FeignConfig.class)
public interface WxUserApi {
    @GetMapping("/user/getuser")
    ResponseResult getuser(@RequestParam("openid")String openid);
    @PostMapping("/user/update")
    ResponseResult update(@RequestBody UserUp user);
    @GetMapping("/user/getCommunicate")
    HashSet<MyChat> getCommunicate();
//    @PostMapping("/user/addCommunicate")
//    ResponseResult addCommunicate(@RequestBody WeChat weChat);
}
