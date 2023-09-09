package com.bcyy.model.item.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bcyy.model.item.vos.ItemCompanyDvo;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "home_item",autoResultMap = true)
public class HomeItem {
    @TableField(value = "id")
    private String itemId;
    private String name;
    private String price;
    @TableField(value = "itemCompanyDvo",typeHandler = JacksonTypeHandler.class)
    private ItemCompanyDvo itemCompanyDvo;
    private String tags;
    @TableField(value = "label",typeHandler = JacksonTypeHandler.class)
    private List<String> label;
    private String image;
    @TableField("hr_name")
    private String HRname;
    private Integer type;
    private Integer state;
}
