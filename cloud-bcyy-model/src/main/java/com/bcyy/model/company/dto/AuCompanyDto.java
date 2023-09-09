package com.bcyy.model.company.dto;

import com.bcyy.model.user.pojos.WxUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class AuCompanyDto {
    private Integer auId;
    private String companyId;
    private WxUser user;
    private Integer state;
}
