package com.bcyy.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.common.redis.CacheService;
import com.bcyy.file.service.FileStorageService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.common.enums.AppHttpCodeEnum;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.pojos.Collect;
import com.bcyy.model.user.pojos.MyChat;
import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.user.mapper.CollectMapper;
import com.bcyy.user.mapper.WxUserMapper;
import com.bcyy.user.service.WxUserService;
import com.bcyy.utils.common.AppJwtUtil;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;

@Service
@Slf4j
@Transactional
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {
    String AppId = "wx0f22b975cb7b9c65";  //公众平台自己的appId
    String AppSecret = "1a07eb029a28f337311d9ea127fedac0";  //AppSecret
    @Autowired
    WxUserMapper wxUserMapper;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    CollectMapper collectMapper;
    @Autowired
    CacheService cacheService;

    /**
     * 登录
     * @param code
     * @return
     */
    @Override
    public ResponseResult login(String code) {
        String openid = getOpenid(code);
        if (openid == null) {
            return ResponseResult.errorResult(200,"code错误");
        }
        WxUser user = getOne(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getOpenid, openid));
        if(user==null){
            //添加用户
            WxUser user2=new WxUser();
            user2.setOpenid(openid);
            wxUserMapper.insert(user2);
            HashSet<String> hashSet = new HashSet<>();
            HashSet<MyChat> hashSet1 = new HashSet<>();
            HashMap<String, Integer> hashMap = new HashMap<>();
            Collect collect = new Collect();
            collect.setOpenid(openid);
            collect.setCollect(hashSet);
            collect.setCommunicate(hashSet1);
            collect.setDeliver(hashMap);
            collectMapper.insert(collect);
        }
        String token = AppJwtUtil.getToken(openid);
        return ResponseResult.okResult(token);
    }
    /**
     * 获取用户信息
     * @param openid
     * @return
     */
    @Override
    public WxUser getUser(String openid) {
        String s = cacheService.get("user_" + openid);
        if (s != null) {
            return JSON.parseObject(s,WxUser.class);
        }
        WxUser user = wxUserMapper.selectOne(new QueryWrapper<WxUser>().eq("openid", openid));
        cacheService.append("user_"+openid,JSON.toJSONString(user));
        return user;
    }
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUser(UserUp user) {
        WxUser user1 = AppThreadLocalUtil.getUser();
        if (user1 != null) {
            user.setOpenid(user1.getOpenid());
        }
        WxUser wxUser = new WxUser();
        BeanUtils.copyProperties(user,wxUser);
        wxUserMapper.update(wxUser,new QueryWrapper<WxUser>().eq("openid",user.getOpenid()));
        cacheService.delete("user_"+user.getOpenid());
        return ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS);
    }
    /**上传图片
     * @param
     * @param
     */
    @Override
    public ResponseResult imageUpload(MultipartFile multipartFile) {
        String openId = AppThreadLocalUtil.getUser().getOpenid();
        String fileUrl = fileStorageService.fileUrl(multipartFile);
        String avatar = wxUserMapper.selectOne(new QueryWrapper<WxUser>().eq("openid", openId)).getAvatar();
        //删除之前的图片
        if (avatar != null && !avatar.equals("null")) {
            fileStorageService.delete(avatar);
        }
        //3.保存到数据库中
        WxUser user = new WxUser();
        user.setAvatar(fileUrl);
        wxUserMapper.update(user, new QueryWrapper<WxUser>().eq("openid", openId));
        //4.返回结果
        return ResponseResult.okResult(fileUrl);
    }


    public String getOpenid(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + AppId +
                "&secret=" + AppSecret +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        //获取返回的数据 并将数据转换成json对象
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        if (StringUtils.contains(jsonData, "errcode")) {
            //出错了
            return null;
        }
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        //解密
        return openid;
    }
}
