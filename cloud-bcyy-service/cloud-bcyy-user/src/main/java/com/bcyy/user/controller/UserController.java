package com.bcyy.user.controller;

import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.user.service.WxUserService;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Api(value = "app端用户登录",tags = "app端用户登录")
public class UserController {
    @Autowired
    private WxUserService wxUserService;

    @GetMapping ("/login")
    @ApiOperation("用户登录")
    public ResponseResult login(String code){
        return wxUserService.login(code);
    }

    @GetMapping ("/get/user")
    @ApiOperation("获取用户信息")
    public ResponseResult getuser(@RequestParam(name = "openId",required = false) String openId){
        if(openId!=null){
            WxUser wxUser = wxUserService.getUser(openId);
            return ResponseResult.okResult(wxUser);
        }else {
            String openid = AppThreadLocalUtil.getUser().getOpenid();
            WxUser wxUser = wxUserService.getUser(openid);
            return ResponseResult.okResult(wxUser);
        }
    }
    @GetMapping ("/my")
    @ApiOperation("获取我的信息")
    public ResponseResult getMy(){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        WxUser wxUser = wxUserService.getUser(openid);
        return ResponseResult.okResult(wxUser);
    }
    @PostMapping ("/update")
    @ApiOperation("更新用户信息")
    public ResponseResult update(@RequestBody UserUp user){
        return wxUserService.updateUser(user);
    }

    @PostMapping("/image")
    @ApiOperation("图片上传")
    public ResponseResult image(MultipartFile multipartFile){

        return wxUserService.imageUpload(multipartFile);
    }
}
