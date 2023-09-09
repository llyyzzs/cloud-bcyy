package com.bcyy.company.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dto.CompanyDto;
import com.bcyy.model.company.pojos.DetailsCompany;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

public interface DetailsCompanyService extends IService<DetailsCompany> {
    ResponseResult GetDetailCompany(String id);
    ResponseResult AddCompany(CompanyDto companyDto);
    ResponseResult imageUpload(MultipartFile multipartFile);
    ResponseResult delete(String id);
    ResponseResult upState(String id,Integer state);
}
