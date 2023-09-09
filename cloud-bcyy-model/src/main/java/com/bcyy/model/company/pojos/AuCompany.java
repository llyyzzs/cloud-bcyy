package com.bcyy.model.company.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bcyy.model.user.pojos.WxUser;
import lombok.Data;

import java.util.HashSet;

@Data
@TableName(value = "au_company",autoResultMap = true)
public class AuCompany {
    @TableField(value = "id")
    private Integer id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "files",typeHandler = JacksonTypeHandler.class)
    private HashSet<String> files;
    @TableField(value = "user",typeHandler = JacksonTypeHandler.class)
    private WxUser user;
    @TableField(value = "state")
    private Integer state;
    @TableField(value = "company_id",typeHandler = JacksonTypeHandler.class)
    private String companyId;
}
