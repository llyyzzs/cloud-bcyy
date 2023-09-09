package com.bcyy.model.user.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;

@Data
@TableName(value = "collect",autoResultMap = true)
public class Collect {
    @TableField("openid")
    private String openid;
    @TableField(value = "collect",typeHandler = JacksonTypeHandler.class)
    private HashSet<String> collect;
    @TableField(value = "communicate",typeHandler = JacksonTypeHandler.class)
    private HashSet<MyChat> communicate;
    @TableField(value = "deliver",typeHandler = JacksonTypeHandler.class)
    private HashMap<String,Integer> deliver;
}
