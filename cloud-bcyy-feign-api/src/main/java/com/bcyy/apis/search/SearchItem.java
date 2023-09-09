package com.bcyy.apis.search;

import com.bcyy.apis.config.FeignConfig;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.search.dtos.RequestParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "bcyy-search-item",configuration = FeignConfig.class)
public interface SearchItem {
    @PostMapping("/search/item/list")
    ResponseResult search(@RequestBody RequestParams params);
    @PostMapping("/search/item/add")
    ResponseResult add(@RequestBody HomeItem homeItem);
    @GetMapping("/search/item/delete")
    ResponseResult delete(String id);
}
