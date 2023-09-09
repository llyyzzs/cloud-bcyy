package com.bcyy.model.company.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompanyUser {
    @ApiModelProperty("id")
    private String openid;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("名字")
    private String name;
    @ApiModelProperty("身份")
    private String standing;
    @ApiModelProperty("介绍")
    private String information;
}
