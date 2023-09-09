package com.bcyy.model.item.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Position {
    @ApiModelProperty("经度")
    private Float longitude;
    @ApiModelProperty("维度")
    private Float latitude;
}
