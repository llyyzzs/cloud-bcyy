package com.bcyy.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.apis.company.DetailCompanyApi;
import com.bcyy.apis.search.SearchItem;
import com.bcyy.apis.user.WxUserApi;
import com.bcyy.common.redis.CacheService;
import com.bcyy.item.mapper.DetailsItemMapper;
import com.bcyy.item.mapper.HomeItemMapper;
import com.bcyy.item.service.DetailsItemService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dvo.CompanyDvo;
import com.bcyy.model.item.dtos.AddItemDto;
import com.bcyy.model.item.pojos.DetailsItem;
import com.bcyy.model.item.pojos.HomeItem;
import com.bcyy.model.item.vos.ItemCompanyDvo;
import com.bcyy.model.item.vos.ItemDvo;
import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class DetailsItemServiceImpl extends ServiceImpl<DetailsItemMapper, DetailsItem> implements DetailsItemService {
    @Autowired
    DetailsItemMapper detailsItemMapper;
    @Autowired
    HomeItemMapper homeItemMapper;
    @Autowired
    WxUserApi wxUserApi;
    @Autowired
    DetailCompanyApi detailCompanyApi;
    @Autowired
    CacheService cacheService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     *获取招聘详情
     * */
    public ResponseResult GetDetailItem(String id){
        String s = cacheService.get("DetailItem_" + id);
        if (s != null) {
            return ResponseResult.okResult(JSON.parseObject(s, ItemDvo.class));
        }
        DetailsItem detailsItem = detailsItemMapper.selectOne(new
                QueryWrapper<DetailsItem>().eq("id", id));
        HomeItem homeItem = homeItemMapper.selectOne(new
                QueryWrapper<HomeItem>().eq("id", id));
        ItemDvo itemDvo = new ItemDvo();
        BeanUtils.copyProperties(homeItem,itemDvo);
        BeanUtils.copyProperties(detailsItem,itemDvo);
        cacheService.append("DetailItem_" + id,JSON.toJSONString(itemDvo));
        return ResponseResult.okResult(itemDvo);
    }

    /**
     * 添加或修改招聘岗位
     * @param addItemDto
     */
    @Override
    @GlobalTransactional
    public ResponseResult AddItem(AddItemDto addItemDto) {
        DetailsItem detailsItem = new DetailsItem();
        HomeItem homeItem = new HomeItem();
        //将招聘信息进行填充
        BeanUtils.copyProperties(addItemDto,detailsItem);
        BeanUtils.copyProperties(addItemDto,homeItem);
        //获取公司信息
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        if (openid==null){
            return  ResponseResult.okResult(200,"未登录");
        }
        Object data = wxUserApi.getuser(openid).getData();
        String s = JSON.toJSONString(data);
        WxUser user = JSON.parseObject(s, WxUser.class);
        if (user == null|| user.getCompanyId()==null){
            return ResponseResult.okResult(200,"未注册公司");
        }
        homeItem.setHRname(user.getName());
        homeItem.setImage(user.getAvatar());
        detailsItem.setHrId(openid);
        //将公司信息添加到招聘信息中
        Object data1 = detailCompanyApi.getDeatils(user.getCompanyId()).getData();
        String s1 = JSON.toJSONString(data1);
        CompanyDvo companyDvo = JSON.parseObject(s1, CompanyDvo.class);
        ItemCompanyDvo itemCompanyDvo = new ItemCompanyDvo();
        BeanUtils.copyProperties(companyDvo, itemCompanyDvo);
        homeItem.setItemCompanyDvo(itemCompanyDvo);
        if (addItemDto.getItemId() == null) {
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            homeItem.setItemId(id);
            detailsItem.setId(id);
            homeItem.setState(0);
            detailsItemMapper.insert(detailsItem);
            homeItemMapper.insert(homeItem);
            rabbitTemplate.convertAndSend("item.topic","item.insert",homeItem);
//            searchItem.add(homeItem);
        }else {
            detailsItemMapper.update(detailsItem,new QueryWrapper<DetailsItem>()
                    .eq("id",addItemDto.getItemId()));
            homeItemMapper.update(homeItem,new QueryWrapper<HomeItem>()
                    .eq("id",addItemDto.getItemId()));
            cacheService.delete("DetailItem_" + addItemDto.getItemId());
            rabbitTemplate.convertAndSend("item.topic","item.delete",homeItem.getItemId());
            rabbitTemplate.convertAndSend("item.topic","item.insert",homeItem);
//            searchItem.delete(homeItem.getItemId());
//            searchItem.add(homeItem);
        }
        return ResponseResult.okResult(200,"添加成功");
    }
    /*
    * 修改招聘状态*/
    public ResponseResult upState(String id,Integer state){
        HomeItem homeItem = new HomeItem();
        homeItem.setState(state);
        homeItemMapper.update(homeItem,new QueryWrapper<HomeItem>().eq("id",id));
        return ResponseResult.okResult(200,"修改成功");
    }
    /**
    * 删除岗位
    * */
    public ResponseResult delete(String id){
        detailsItemMapper.delete(new QueryWrapper<DetailsItem>().eq("id",id));
        homeItemMapper.delete(new QueryWrapper<HomeItem>().eq("id",id));
        cacheService.delete("DetailItem_" + id);
        rabbitTemplate.convertAndSend("item.topic","item.delete",id);
//        searchItem.delete(id);
        return ResponseResult.okResult(200,"删除成功");
    }
}
