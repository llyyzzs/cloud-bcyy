package com.bcyy.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.common.redis.CacheService;
import com.bcyy.company.mapper.HomeCompanyMapper;
import com.bcyy.company.service.HomeCompanyService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.pojos.HomeCompany;
import com.bcyy.model.item.dtos.HomeItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class HomeCompanyServiceImpl extends ServiceImpl<HomeCompanyMapper, HomeCompany>implements HomeCompanyService {
    @Autowired
    HomeCompanyMapper homeCompanyMapper;
    @Autowired
    CacheService cacheService;
    /**
     * 获取公司列表
     * @return
     */
    @Override
    public ResponseResult homeCompany(HomeItemDto homeItemDto) {
        Page<HomeCompany> page = new Page<>();
        if (homeItemDto.getPage() == null) {
            homeItemDto.setPage(1);
        }
        if (homeItemDto.getSize() == null) {
            homeItemDto.setSize(10);
        }
        page.setCurrent(homeItemDto.getPage());
        page.setSize(homeItemDto.getSize());
        QueryWrapper<HomeCompany> queryWrapper = new QueryWrapper<>();
        if (homeItemDto.getKeywords() != null && !homeItemDto.getKeywords().equals("")) {
            queryWrapper.like("name",homeItemDto.getKeywords());
        }
        if (homeItemDto.getTags() != null) {
            queryWrapper.like("tags",homeItemDto.getTags());
        }
        queryWrapper.eq("state",1);
        Page<HomeCompany> homeItemPage = homeCompanyMapper.selectPage(page, queryWrapper);
        return ResponseResult.okResult(homeItemPage.getRecords());
    }
}
