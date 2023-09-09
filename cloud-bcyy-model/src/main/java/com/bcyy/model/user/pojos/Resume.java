package com.bcyy.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bcyy.model.item.dtos.Address;
import lombok.Data;

@Data
@TableName(value = "resume",autoResultMap = true)
public class Resume {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String want;
    @TableField(value = "address",typeHandler = JacksonTypeHandler.class)
    private Address address;
    private String major;
    private String advantage;
    private String evlauate;
    private String skill;
    @TableField("work_exp")
    private String workExp;
    @TableField("projetc_exp")
    private String projetcExp;
    @TableField("education_exp")
    private String educationExp;
    @TableField("certification")
    private String certification;
    @TableField("openid")
    private String openid;
}
