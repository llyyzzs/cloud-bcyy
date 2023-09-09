package com.bcyy.item.controller;

import com.bcyy.item.service.DetailsItemService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.AddItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
@Api(tags = "岗位详情管理")
public class DetailsItemController {
    @Autowired
    DetailsItemService detailsItemService;


    @GetMapping("/get/detail")
    @ApiOperation("获取岗位信息")
    public ResponseResult getDeatils(@RequestParam("id") String id){
        return  detailsItemService.GetDetailItem(id);
    }
    @PostMapping("/additem")
    @ApiOperation("添加更新岗位")
    public ResponseResult additem(@RequestBody AddItemDto addItemDto){
        return detailsItemService.AddItem(addItemDto);
    }
    @GetMapping("/upState")
    @ApiOperation("修改招聘状态")
    public ResponseResult upState(@RequestParam("id")String id,@RequestParam("state")Integer state){
        return detailsItemService.upState(id,state);
    }
    @GetMapping("/delete")
    @ApiOperation("删除岗位")
    public ResponseResult delete(@RequestParam("id") String id){
        return detailsItemService.delete(id);
    }
}
