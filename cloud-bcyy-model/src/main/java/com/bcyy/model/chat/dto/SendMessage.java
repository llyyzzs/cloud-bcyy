package com.bcyy.model.chat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SendMessage {
    @ApiModelProperty("聊天室id")
    private String chatRoomId;
    @ApiModelProperty("发送者id")
    private String senderId;
    @ApiModelProperty("接收者id")
    private String receiverId;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("类型")
    private Integer type;
}
