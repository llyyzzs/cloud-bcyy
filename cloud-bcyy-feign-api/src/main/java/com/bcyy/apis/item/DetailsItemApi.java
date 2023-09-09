package com.bcyy.apis.item;

import com.bcyy.apis.config.FeignConfig;
import com.bcyy.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bcyy-item",configuration = FeignConfig.class)
public interface DetailsItemApi {
    @GetMapping("/item/get/detail")
    ResponseResult getDetails(@RequestParam("id") String id);
    @GetMapping("/item/upState")
    ResponseResult upState(@RequestParam("id")String id,@RequestParam("state")Integer state);
}
