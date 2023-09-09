package com.bcyy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dvos.NoteDvo;
import com.bcyy.model.user.pojos.Note;
import com.bcyy.model.user.pojos.Resume;
import com.bcyy.user.mapper.NoteMapper;
import com.bcyy.user.service.NoteService;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    @Autowired
    NoteMapper noteMapper;
    /*
    * 添加、更新笔记
    * */
    public ResponseResult upNote(NoteDvo noteDvo){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        if (openid == null) {
            return ResponseResult.okResult(200,"未登录");
        }
        Note note = new Note();
        BeanUtils.copyProperties(noteDvo,note);
        note.setOpenid(openid);
        note.setDate(new Date());
        if (noteDvo.getNoteId() != null) {
            noteMapper.update(note,new QueryWrapper<Note>().eq("id",noteDvo.getNoteId()));
            return ResponseResult.okResult(200,"更新成功");
        }
        else {
            noteMapper.insert(note);
            return ResponseResult.okResult(200,"添加成功");
        }
    }
    /*
    * 获取所有笔记
    * */
    public ResponseResult getNoteList(){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        if (openid == null) {
            return ResponseResult.okResult(200,"未登录");
        }
        List<Note> noteList = noteMapper.selectList(new QueryWrapper<Note>()
                .eq("openid", openid));
        return ResponseResult.okResult(noteList);
    }
    /*
    * 获取笔记
    * */
    public ResponseResult getNote(Integer id){
        Note note = noteMapper.selectOne(new QueryWrapper<Note>().eq("id", id));
        return ResponseResult.okResult(note);
    }
    /*
    * 删除笔记
    * */
    public ResponseResult deleteNote(Integer id){
        if (id == null) {
            noteMapper.delete(new QueryWrapper<>());
        }else {
            noteMapper.delete(new QueryWrapper<Note>().eq("id",id));
        }
        return ResponseResult.okResult(200,"删除成功");
    }
}
