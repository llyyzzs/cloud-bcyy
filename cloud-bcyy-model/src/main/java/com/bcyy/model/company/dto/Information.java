package com.bcyy.model.company.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Information {
    @ApiModelProperty("公司全名")
    private String name;
    @ApiModelProperty("法定代表人")
    private String representative;
    @ApiModelProperty("注册资本")
    private String registered;
    @ApiModelProperty("注册时间")
    private String time;
    @ApiModelProperty("公司状态")
    private String status;
}
