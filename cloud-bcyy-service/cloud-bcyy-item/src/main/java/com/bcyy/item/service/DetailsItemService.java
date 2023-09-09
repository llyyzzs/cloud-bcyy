package com.bcyy.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.AddItemDto;
import com.bcyy.model.item.pojos.DetailsItem;

public interface DetailsItemService extends IService<DetailsItem> {
    ResponseResult GetDetailItem(String id);
    ResponseResult AddItem(AddItemDto addItemDto);
    ResponseResult delete(String id);
    ResponseResult upState(String id,Integer state);
}
