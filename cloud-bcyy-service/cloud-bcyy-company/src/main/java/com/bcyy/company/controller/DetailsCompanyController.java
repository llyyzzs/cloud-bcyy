package com.bcyy.company.controller;

import com.bcyy.company.service.DetailsCompanyService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dto.CompanyDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/company")
@Api(tags = "公司管理")
public class DetailsCompanyController {
    @Autowired
    DetailsCompanyService detailsCompanyService;
    @GetMapping("/get/detail")
    @ApiOperation("获取公司信息")
    public ResponseResult getDeatils(@RequestParam("id") String id){
        return detailsCompanyService.GetDetailCompany(id);
    }
    @PostMapping("/addcompany")
    @ApiOperation("添加更新公司")
    public ResponseResult addCompany(@RequestBody CompanyDto companyDto){
       return detailsCompanyService.AddCompany(companyDto);
    }
    @GetMapping("/upState")
    @ApiOperation("修改公司状态")
    public ResponseResult upState(@RequestParam("id")String id,@RequestParam("state")Integer state){
        return detailsCompanyService.upState(id,state);
    }
    @PostMapping("/imageUpload")
    @ApiOperation("文件上传")
    public ResponseResult imageUpload(@RequestBody MultipartFile multipartFile){
        return detailsCompanyService.imageUpload(multipartFile);
    }
    @GetMapping("/delete")
    @ApiOperation("删除公司信息")
    public ResponseResult delete(@RequestParam("id") String id){
        return detailsCompanyService.delete(id);
    }
}
