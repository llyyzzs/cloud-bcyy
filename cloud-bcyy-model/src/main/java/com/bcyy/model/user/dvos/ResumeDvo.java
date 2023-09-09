package com.bcyy.model.user.dvos;

import com.bcyy.model.item.dtos.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResumeDvo {
    private Integer resumeId;
    @ApiModelProperty("名字")
    private String name;
    @ApiModelProperty("工作意向")
    private String want;
    @ApiModelProperty("地址")
    private Address address;
    @ApiModelProperty("专业")
    private String major;
    @ApiModelProperty("优势")
    private String advantage;
    @ApiModelProperty("")
    private String evlauate;
    @ApiModelProperty("技能")
    private String skill;
    @ApiModelProperty("工作经历")
    private String workExp;
    @ApiModelProperty("项目经验")
    private String projetcExp;
    @ApiModelProperty("教育经历")
    private String educationExp;
    @ApiModelProperty("证书")
    private String certification;
}
