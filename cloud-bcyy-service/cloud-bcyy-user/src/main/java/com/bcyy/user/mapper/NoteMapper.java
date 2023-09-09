package com.bcyy.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcyy.model.user.pojos.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {

}
