package com.bcyy.model.user.dvos;

import lombok.Data;

import java.util.Date;
@Data
public class NoteDvo {
    //笔记
    private Integer noteId;
    private String title;
    private Date date;
    private String content;
}
