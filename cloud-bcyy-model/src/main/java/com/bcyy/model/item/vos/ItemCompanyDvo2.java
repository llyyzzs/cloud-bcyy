package com.bcyy.model.item.vos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bcyy.model.item.dtos.Address;
import com.bcyy.model.item.dtos.Address2;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItemCompanyDvo2 {
    private String id;
    @ApiModelProperty("公司名字")
    private String name;
    @ApiModelProperty("规模")
    private String scale;
    @ApiModelProperty("热度")
    private Integer heat;
    @ApiModelProperty("行业")
    private String industry;
    @ApiModelProperty("上市")
    private String launch;
    @ApiModelProperty("标签")
    private String tags;
    @ApiModelProperty("地址")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Address2 address;
}
