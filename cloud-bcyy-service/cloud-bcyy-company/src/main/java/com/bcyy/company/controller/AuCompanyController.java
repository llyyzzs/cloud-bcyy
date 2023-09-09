package com.bcyy.company.controller;

import com.bcyy.company.service.impl.AuCompanyServiceImpl;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dto.AuCompanyDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
@RestController
@RequestMapping("/company")
public class AuCompanyController {
    @Autowired
    AuCompanyServiceImpl auCompanyService;

    @GetMapping("/au")
    @ApiOperation("认证公司")
    public ResponseResult AU(@RequestParam("name")String name, @RequestParam("files") HashSet<String> files){
        return auCompanyService.AuCompany(name,files);
    }
    @GetMapping("/getAu")
    @ApiOperation("获取待认证")
    public ResponseResult getAu(){
        return auCompanyService.getAu();
    }
    @PostMapping("/upAu")
    @ApiOperation("管理员审批")
    public ResponseResult upAu(@RequestBody AuCompanyDto auCompanyDto){
        return auCompanyService.upAu(auCompanyDto);
    }
}
