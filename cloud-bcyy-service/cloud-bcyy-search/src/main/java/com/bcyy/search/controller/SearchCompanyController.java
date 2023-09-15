package com.bcyy.search.controller;


import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.search.dtos.RequestParams;
import com.bcyy.search.service.impl.SearchCompanyServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/company")
@Api(tags = "es公司搜索")
public class SearchCompanyController {

    @Autowired
    SearchCompanyServiceImpl searchCompanyService;
    @PostMapping("/list")
    @ApiOperation("分页搜索")
    public ResponseResult search(@RequestBody RequestParams params){
        return searchCompanyService.search(params);
    }
    @GetMapping("/aggs")
    @ApiOperation("检索标签")
    public ResponseResult aggs(String name){
        return searchCompanyService.aggTags(name);
    }
    @ApiOperation("搜索建议")
    @GetMapping("suggestion")
    public ResponseResult getSuggestions(@RequestParam("key") String prefix) {
        return searchCompanyService.getSuggestions(prefix);
    }
//    @PostMapping("/add")
//    @ApiOperation("添加文档")
//    public ResponseResult add(@RequestBody CompanyDvo companyDvo){
//        return searchCompanyService.add(companyDvo);
//    }
//    @GetMapping("/addList")
//    @ApiOperation("添加全部文档")
//    public ResponseResult addList(){
//        return searchCompanyService.addList();
//    }
//    @GetMapping("/delete")
//    @ApiOperation("删除文档")
//    public ResponseResult delete(String id){
//        return searchCompanyService.delete(id);
//    }

}
