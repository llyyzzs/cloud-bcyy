package com.bcyy.admin.controller;

import com.bcyy.admin.service.impl.AdUserServiceImpl;
import com.bcyy.apis.company.DetailCompanyApi;
import com.bcyy.apis.item.DetailsItemApi;
import com.bcyy.model.admin.dtos.AdUserDto;
import com.bcyy.model.admin.dtos.AddAdmin;
import com.bcyy.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@Api(tags = "后台管理员")
public class AdUserController {
    @Autowired
    AdUserServiceImpl adUserService;
    @Autowired
    DetailCompanyApi detailCompanyApi;
    @Autowired
    DetailsItemApi detailsItemApi;
    @PostMapping("/login")
    @ApiOperation("登录")
    public ResponseResult login(@RequestBody AdUserDto dto){
        return adUserService.login(dto);
    }
    @PostMapping("/get/addAdmin")
    @ApiOperation("注册")
    public ResponseResult addAdmin(@RequestBody AddAdmin admin){
        return adUserService.addUser(admin);
    }
    @GetMapping("/upItem")
    @ApiOperation("修改招聘状态")
    public ResponseResult upItemState(@RequestParam("id")String id,@RequestParam("state")Integer state){
        return detailsItemApi.upState(id,state);
    }
    @GetMapping("/upCompany")
    @ApiOperation("修改公司状态")
    public ResponseResult upCompanyState(@RequestParam("id")String id,@RequestParam("state")Integer state){
        return detailCompanyApi.upState(id,state);
    }
}
