package com.bcyy.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "note")
public class Note {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private Date date;
    private String content;
    private String openid;

}
