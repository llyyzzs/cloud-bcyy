package com.bcyy.search.service;

import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.search.dtos.RequestParams;
import com.bcyy.model.search.vos.PageResult;

public interface SearchItemService {
    ResponseResult search(RequestParams params);
}
