package com.bcyy.model.item.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "details_item",autoResultMap = true)
public class DetailsItem {
    private String id;
    private String experience;
    private String degree;
    private String requirement;
    private String description;
    @TableField("hr_id")
    private String hrId;
}
