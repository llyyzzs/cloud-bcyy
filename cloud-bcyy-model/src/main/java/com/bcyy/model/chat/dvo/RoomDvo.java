package com.bcyy.model.chat.dvo;

import lombok.Data;

import java.util.Date;
@Data
public class RoomDvo {
    private String id;
    private String name;
    private String description;
    private Date created;
    private Integer count;
    private String avatar;
    private Integer type;
}
