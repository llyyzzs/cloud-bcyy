package com.bcyy.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcyy.model.item.dtos.ScreenDto;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.item.vos.ItemDvo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface HomeItemMapper extends BaseMapper<HomeItem> {
    List<ItemDvo> screenList(@Param("screenDto") ScreenDto screenDto);
}
