package com.bcyy.item.controller;

import com.bcyy.item.service.impl.HomeItemServiceImpl;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.CompanyItemDto;
import com.bcyy.model.item.dtos.HomeItemDto;
import com.bcyy.model.item.dtos.ScreenDto;
import com.bcyy.model.item.pojos.HomeItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@Api(value = "首页内容",tags = "首页内容端")
public class HomeItemController {
    @Autowired
    private HomeItemServiceImpl homeItemService;
    @PostMapping("/get/home")
    @ApiOperation("获取首页内容")
    public ResponseResult homeItem(@RequestBody HomeItemDto homeItemDto){
        return homeItemService.homeItem(homeItemDto,1);
    }
    @GetMapping("/get/itemOne")
    @ApiOperation("获取单个招聘简略信息")
    public ResponseResult getItem(@RequestParam("itemId") String itemId){
        return homeItemService.getItem(itemId);
    }
    @PostMapping("/get/companyItem")
    @ApiOperation("获取公司岗位")
    public ResponseResult getCompanyItem(@RequestBody CompanyItemDto companyItemDto){
        return homeItemService.getCompanyItem(companyItemDto,1);
    }
    @PostMapping("/get/screen")
    @ApiOperation("筛选查询")
    public ResponseResult screenList(@RequestBody ScreenDto screenDto){
        return homeItemService.screen(screenDto);
    }
}
