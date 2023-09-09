package com.bcyy.apis.company;

import com.bcyy.apis.config.FeignConfig;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dto.AuCompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "bcyy-company",configuration = FeignConfig.class)
public interface DetailCompanyApi {
    @GetMapping("/company/get/detail")
    ResponseResult getDeatils(@RequestParam("id") String id);
    @PostMapping("/company/imageUpload")
    ResponseResult imageUpload(@RequestBody MultipartFile multipartFile);
    @GetMapping("/company/upState")
    ResponseResult upState(@RequestParam("id")String id,@RequestParam("state")Integer state);
    @GetMapping("/company/getAu")
    ResponseResult getAu();
    @PostMapping("/company/upAu")
    ResponseResult upAu(@RequestBody AuCompanyDto auCompanyDto);
}
