package com.bcyy.model.item.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class ScreenDto {
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("薪资范围")
    private String price;
    @ApiModelProperty("类别")
    private String tags;
    @ApiModelProperty("是否全职")
    private Integer type;
    @ApiModelProperty("经验")
    private String experience;
    @ApiModelProperty("学历")
    private String degree;
    @ApiModelProperty("公司")
    private String name;
    @ApiModelProperty("城市")
    private String city;
}
