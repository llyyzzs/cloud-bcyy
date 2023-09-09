package com.bcyy.user.controller;

import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dvos.ResumeDvo;
import com.bcyy.user.service.impl.ResumeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "简历端")
public class ResumeController {
    @Autowired
    ResumeServiceImpl resumeService;
    @ApiOperation("更新、添加简历")
    @PostMapping("/resume")
    public ResponseResult upResume(@RequestBody ResumeDvo resumeDvo){
        return resumeService.upResume(resumeDvo);
    }

    @ApiOperation("获取所有简历")
    @GetMapping("/getResumeList")
    public ResponseResult getResumeList(){
        return resumeService.getResumeList();
    }

    @ApiOperation("获取简历")
    @GetMapping("/getResume")
    public ResponseResult getResume(Integer id){
        return resumeService.getResume(id);
    }

    @ApiOperation("删除简历")
    @GetMapping("/deleteResume")
    public ResponseResult deleteResume(Integer id){
        return resumeService.deleteResume(id);
    }
}
