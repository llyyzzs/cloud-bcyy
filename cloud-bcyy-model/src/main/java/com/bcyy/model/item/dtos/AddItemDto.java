package com.bcyy.model.item.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddItemDto {
    private String itemId;
    @ApiModelProperty("经验")
    private String experience;
    @ApiModelProperty("学历")
    private String degree;
    @ApiModelProperty("要求")
    private String requirement;
    @ApiModelProperty("工作内容")
    private String description;


    @ApiModelProperty("岗位")
    private String name;
    @ApiModelProperty("薪资")
    private String price;
    @ApiModelProperty("类别")
    private String tags;
    @ApiModelProperty("标签")
    private List<String> label;
    @ApiModelProperty("0全职 1兼职")
    private Integer type;
}
