package com.bcyy.model.company.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bcyy.model.company.dto.CompanyUser;
import com.bcyy.model.company.dto.Information;
import com.bcyy.model.company.dto.Welfares;
import com.bcyy.model.item.dtos.Address;
import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
@TableName(value = "details_company",autoResultMap = true)
public class DetailsCompany {
    private String id;
    private String introduce;
    @TableField(value = "address",typeHandler = JacksonTypeHandler.class)
    private Address address;
    @TableField(value = "welfare",typeHandler = JacksonTypeHandler.class)
    private Welfares welfares;
    @TableField(value = "images",typeHandler = JacksonTypeHandler.class)
    private HashSet<String> images;
    @TableField(value = "information",typeHandler = JacksonTypeHandler.class)
    private Information information;
    @TableField(value = "users",typeHandler = JacksonTypeHandler.class)
    private HashSet<CompanyUser> users;
    @TableField(value = "service",typeHandler = JacksonTypeHandler.class)
    private HashSet<CompanyUser> service;
}
