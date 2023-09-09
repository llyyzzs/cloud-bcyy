package com.bcyy.company.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.bcyy.company.service.HomeCompanyService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.HomeItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Api(value = "首页内容",tags = "首页内容端")
public class HomeCompanyController {
    @Autowired
    HomeCompanyService homeCompanyService;
    @PostMapping("/get/home")
    @ApiOperation("获取首页内容")
    public ResponseResult homeCompany(@RequestBody HomeItemDto homeItemDto){
        return homeCompanyService.homeCompany(homeItemDto);
    }
}
