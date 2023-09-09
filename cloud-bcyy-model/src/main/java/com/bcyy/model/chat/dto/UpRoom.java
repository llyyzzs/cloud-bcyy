package com.bcyy.model.chat.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpRoom {
    private String roomId;
    private String name;
    private String description;
    private Integer count;
    private Date created;
}
