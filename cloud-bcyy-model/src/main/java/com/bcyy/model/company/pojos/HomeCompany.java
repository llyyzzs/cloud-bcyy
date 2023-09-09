package com.bcyy.model.company.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "home_company",autoResultMap = true)
public class HomeCompany {
    private String id;
    private String name;
    private String avatar;
    private String scale;
    private Integer heat;
    private String industry;
    @TableField("job_count")
    private String jobCount;
    private String salary;
    private String launch;
    private String tags;
    private Integer state;
}
