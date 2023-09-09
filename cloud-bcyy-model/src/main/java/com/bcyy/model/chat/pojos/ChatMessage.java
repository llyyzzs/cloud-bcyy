package com.bcyy.model.chat.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("chat_Message")
public class ChatMessage {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "chat_room_id")
    private String chatRoomId;
    @TableField(value = "sender_id")
    private String senderId;
    @TableField(value = "receiver_id")
    private String receiverId;
    @TableField(value = "created_at")
    private Date createdAt;
    private String content;
    private Integer type;

}
