package com.bcyy.search2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcyy.model.company.pojos.HomeCompany;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchCompanyMapper extends BaseMapper<HomeCompany> {
}
