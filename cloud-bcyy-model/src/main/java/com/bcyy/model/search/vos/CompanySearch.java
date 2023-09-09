package com.bcyy.model.search.vos;

import com.bcyy.model.company.dvo.CompanyDvo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class CompanySearch {
    private String id;
    private String name;
    private String avatar;
    private String scale;
    private Integer heat;
    private String industry;
    private String jobCount;
    private String salary;
    private String launch;
    private String tags;
    private String position;
    private String city;
    // 排序时的 距离值
    private Object distance;
    //建议
    private List<String> suggestion;
    public CompanySearch(CompanyDvo companyDvo){
        this.id=companyDvo.getId();
        this.name=companyDvo.getName();
        this.avatar=companyDvo.getAvatar();
        this.scale=companyDvo.getScale();
        this.heat=companyDvo.getHeat();
        this.industry=companyDvo.getIndustry();
        this.jobCount=companyDvo.getJobCount();
        this.salary=companyDvo.getSalary();
        this.launch=companyDvo.getLaunch();
        this.tags=companyDvo.getTags();
        this.position=companyDvo.getAddress().getPosition().getLongitude()
                +", "+companyDvo.getAddress().getPosition().getLatitude();
        this.city=companyDvo.getAddress().getCity();
        this.suggestion= Arrays.asList(this.name,this.city);
    }
}
