package com.bcyy.user.controller;

import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.user.service.WxUserService;
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

    @GetMapping ("/getuser")
    @ApiOperation("获取用户信息")
    public ResponseResult getuser(String openid){
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
