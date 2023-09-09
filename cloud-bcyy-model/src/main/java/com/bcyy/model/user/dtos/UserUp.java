package com.bcyy.model.user.dtos;

import lombok.Data;

@Data
public class UserUp {
    private String name;
    private String openid;
    private String phone;
    private String avatar;
    private Boolean gender;
    private String email;
    private String birthday;
    private String degree;
    private String companyId;
    private String standing;
    private String information;
}
