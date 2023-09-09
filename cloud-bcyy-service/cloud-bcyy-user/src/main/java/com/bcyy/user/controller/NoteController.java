package com.bcyy.user.controller;


import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dvos.NoteDvo;
import com.bcyy.model.user.dvos.ResumeDvo;
import com.bcyy.user.service.impl.NoteServiceImpl;
import com.bcyy.user.service.impl.ResumeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "笔记端")
public class NoteController {
    @Autowired
    NoteServiceImpl noteService;

    @ApiOperation("更新、添加笔记")
    @PostMapping("/note")
    public ResponseResult upResume(@RequestBody  NoteDvo noteDvo){
        return noteService.upNote(noteDvo);
    }

    @ApiOperation("获取所有笔记")
    @GetMapping("/getNoteList")
    public ResponseResult getResumeList(){
        return noteService.getNoteList();
    }

    @ApiOperation("获取笔记")
    @GetMapping("/getNote")
    public ResponseResult getResume(Integer id){
        return noteService.getNote(id);
    }

    @ApiOperation("删除笔记")
    @GetMapping("/deleteNote")
    public ResponseResult deleteNote(Integer id){
        return noteService.deleteNote(id);
    }
}
