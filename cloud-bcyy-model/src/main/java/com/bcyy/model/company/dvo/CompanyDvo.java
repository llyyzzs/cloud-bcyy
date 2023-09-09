package com.bcyy.model.company.dvo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bcyy.model.company.dto.CompanyUser;
import com.bcyy.model.company.dto.Information;
import com.bcyy.model.company.dto.Welfares;
import com.bcyy.model.item.dtos.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashSet;
@Data
public class CompanyDvo {
    private String id;
    @ApiModelProperty("公司名字")
    private String name;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("规模")
    private String scale;
    @ApiModelProperty("热度")
    private Integer heat;
    @ApiModelProperty("行业")
    private String industry;
    @ApiModelProperty("在招岗位")
    private String jobCount;
    @ApiModelProperty("平均薪资")
    private String salary;
    @ApiModelProperty("状态")
    private String launch;
    @ApiModelProperty("标签")
    private String tags;
    @ApiModelProperty("介绍")
    private String introduce;
    @ApiModelProperty("地址")
    @TableField(value = "address",typeHandler = JacksonTypeHandler.class)
    private Address address;
    @ApiModelProperty("福利和工作时间")
    @TableField(value = "welfare",typeHandler = JacksonTypeHandler.class)
    private Welfares welfares;
    @ApiModelProperty("公司图片")
    @TableField(value = "images",typeHandler = JacksonTypeHandler.class)
    private HashSet<String> images;
    @ApiModelProperty("工商信息")
    @TableField(value = "information",typeHandler = JacksonTypeHandler.class)
    private Information information;
    @ApiModelProperty("产品简介")
    @TableField(value = "service",typeHandler = JacksonTypeHandler.class)
    private HashSet<CompanyUser> service;
    @TableField(value = "users",typeHandler = JacksonTypeHandler.class)
    private HashSet<CompanyUser> users;
}
