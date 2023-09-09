package com.bcyy.model.company.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Welfares {
    @ApiModelProperty("工作时间")
    private String workTime;
    @ApiModelProperty("公司福利")
    private List<String> welfare;
}
