package com.bcyy.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcyy.model.chat.pojos.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}
