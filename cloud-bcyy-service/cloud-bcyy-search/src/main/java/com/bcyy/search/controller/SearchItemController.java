package com.bcyy.search.controller;


import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.search.dtos.RequestParams;
import com.bcyy.search.service.impl.SearchItemServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/item")
@Api(tags = "es搜索")
public class SearchItemController {

    @Autowired
    SearchItemServiceImpl searchItemService;
    @PostMapping("/list")
    @ApiOperation("分页搜索")
    public ResponseResult search(@RequestBody RequestParams params){
        return searchItemService.search(params);
    }
    @GetMapping("/aggs")
    @ApiOperation("检索标签")
    public ResponseResult aggs(String name){
        return searchItemService.aggTags(name);
    }
    @ApiOperation("搜索建议")
    @GetMapping("suggestion")
    public ResponseResult getSuggestions(@RequestParam("key") String prefix) {
        return searchItemService.getSuggestions(prefix);
    }
//    @PostMapping("/add")
//    @ApiOperation("添加文档")
//    public ResponseResult add(@RequestBody HomeItem homeItem){
//        return searchItemService.add(homeItem);
//    }
    @GetMapping("/addList")
    @ApiOperation("添加全部文档")
    public ResponseResult addList(){
        return searchItemService.addList();
    }
//    @GetMapping("/delete")
//    @ApiOperation("删除文档")
//    public ResponseResult delete(String id){
//        return searchItemService.delete(id);
//    }

}
