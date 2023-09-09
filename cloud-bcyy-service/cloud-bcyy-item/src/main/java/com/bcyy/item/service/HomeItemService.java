package com.bcyy.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.HomeItemDto;
import com.bcyy.model.item.pojos.HomeItem;

import java.util.List;

public interface HomeItemService extends IService<HomeItem> {
    ResponseResult homeItem(HomeItemDto homeItemDto, Integer type);
}
