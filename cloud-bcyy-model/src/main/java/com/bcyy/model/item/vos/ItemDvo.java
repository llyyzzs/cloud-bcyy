package com.bcyy.model.item.vos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemDvo {
    private String  id;
    private String name;
    private String price;
    private String tags;
    private List<String> label;
    private String image;
    private String HRname;
    private Integer type;
    private String experience;
    private String degree;
    private String requirement;
    private String description;
    private String hrId;
    private ItemCompanyDvo itemCompanyDvo;
}
