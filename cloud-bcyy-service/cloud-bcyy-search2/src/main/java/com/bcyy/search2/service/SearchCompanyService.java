package com.bcyy.search2.service;

import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.search.dtos.RequestParams;

public interface SearchCompanyService {
    ResponseResult search(RequestParams params);
}
