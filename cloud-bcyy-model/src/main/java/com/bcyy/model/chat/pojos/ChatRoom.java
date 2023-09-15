package com.bcyy.model.chat.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "chat_room",autoResultMap = true)
public class ChatRoom {
    private String id;
    private String description;
    @TableField(value = "created_at")
    private Date created;
    private Integer count;
    private Integer type;
}
