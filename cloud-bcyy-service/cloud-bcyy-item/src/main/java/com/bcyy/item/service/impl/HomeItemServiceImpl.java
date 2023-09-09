package com.bcyy.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.item.mapper.DetailsItemMapper;
import com.bcyy.item.mapper.HomeItemMapper;
import com.bcyy.item.service.HomeItemService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.item.dtos.CompanyItemDto;
import com.bcyy.model.item.dtos.HomeItemDto;
import com.bcyy.model.item.dtos.ScreenDto;
import com.bcyy.model.item.pojos.DetailsItem;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.item.vos.ItemDvo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class HomeItemServiceImpl extends ServiceImpl<HomeItemMapper, HomeItem> implements HomeItemService {
    @Autowired
    HomeItemMapper homeItemMapper;
    @Autowired
    DetailsItemMapper detailsItemMapper;
    /**
     * 获取岗位列表
     * @return
     */
    @Override
    public ResponseResult homeItem(HomeItemDto homeItemDto,Integer type) {
        Page<HomeItem> page = new Page<>();
        if (homeItemDto.getPage() == null) {
            page.setCurrent(1);
        }else {
            page.setCurrent(homeItemDto.getPage());
        }
        if (homeItemDto.getSize()!= null) {
            page.setSize(homeItemDto.getSize());
        }else {
            page.setSize(10);
        }
        QueryWrapper<HomeItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type);
        String keywords = homeItemDto.getKeywords();
        if (keywords != null && !keywords.equals("")) {
            queryWrapper.like("name",keywords);
        }
        if (homeItemDto.getTags() != null) {
            queryWrapper.like("tags",homeItemDto.getTags());
        }
        queryWrapper.eq("state",1);
        Page<HomeItem> homeItemPage = homeItemMapper.selectPage(page, queryWrapper);
        return ResponseResult.okResult(homeItemPage.getRecords());
    }
    /**
     * 获取某个公司的岗位列表
     * */
    public ResponseResult getCompanyItem(@NotNull CompanyItemDto companyItemDto, Integer type){
        Page<HomeItem> page = new Page<>();
        if (companyItemDto.getPage() == null) {
            companyItemDto.setPage(1);
        }
        if (companyItemDto.getSize() == null) {
            companyItemDto.setSize(10);
        }
        page.setCurrent(companyItemDto.getPage());
        page.setSize(companyItemDto.getSize());
        LambdaQueryWrapper<HomeItem> queryWrapper = new LambdaQueryWrapper<>();
        if (type!=null){
            queryWrapper.eq(HomeItem::getType,type);
        }
        if (companyItemDto.getKeywords() != null && !companyItemDto.getKeywords().equals("")) {
            queryWrapper.like(HomeItem::getName,companyItemDto.getKeywords());
        }
        if (companyItemDto.getTags() != null) {
            queryWrapper.eq(HomeItem::getTags,companyItemDto.getTags());
        }
        if(companyItemDto.getCompanyId()!=null){
            queryWrapper.like(HomeItem::getItemCompanyDvo,"\"id\": \"" +companyItemDto.getCompanyId()  + "\"");
        }
        Page<HomeItem> homeItemPage = homeItemMapper.selectPage(page, queryWrapper);
        return ResponseResult.okResult(homeItemPage);
    }
    /**
     * 筛选岗位
     * */
    public ResponseResult screen(ScreenDto screenDto){
        List<ItemDvo> homeItems = homeItemMapper.screenList(screenDto);
        return ResponseResult.okResult(homeItems);
    }
    /**
     * 删除该公司所有招聘
     * */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "deleteCompanyItem"),
            exchange = @Exchange(value = "company.topic"),
            key = "delete.company.item"
    ))
    public void deleteCompanyItem(String companyId){
        List<HomeItem> homeItemList = homeItemMapper.selectList(new LambdaQueryWrapper<HomeItem>()
                .like(HomeItem::getItemCompanyDvo, "\"id\": \"" + companyId + "\""));
        for (HomeItem homeItem: homeItemList) {
            homeItemMapper.delete(new QueryWrapper<HomeItem>()
                    .eq("id",homeItem.getItemId()));
            detailsItemMapper.delete(new QueryWrapper<DetailsItem>()
                    .eq("id",homeItem.getItemId()));
        }
    }
}
