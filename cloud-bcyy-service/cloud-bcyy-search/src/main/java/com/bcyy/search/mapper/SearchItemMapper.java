package com.bcyy.search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcyy.model.item.pojos.HomeItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchItemMapper extends BaseMapper<HomeItem> {
}
