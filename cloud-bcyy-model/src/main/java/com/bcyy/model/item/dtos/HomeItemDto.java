package com.bcyy.model.item.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HomeItemDto {
    @ApiModelProperty("类别")
    String tags;
    Integer page;
    Integer size;
    String keywords;
}
