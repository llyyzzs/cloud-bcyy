package com.bcyy.company.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.pojos.HomeCompany;
import com.bcyy.model.item.dtos.HomeItemDto;

public interface HomeCompanyService extends IService<HomeCompany> {
    ResponseResult homeCompany(HomeItemDto homeItemDto);
}
