package com.bcyy.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.admin.mapper.AdUserMapper;
import com.bcyy.admin.service.AdUserService;
import com.bcyy.apis.company.DetailCompanyApi;
import com.bcyy.apis.item.DetailsItemApi;
import com.bcyy.model.admin.dtos.AdUserDto;
import com.bcyy.model.admin.dtos.AddAdmin;
import com.bcyy.model.admin.pojos.AdUser;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.common.enums.AppHttpCodeEnum;
import com.bcyy.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    @Autowired
    AdUserMapper adUserMapper;
    public ResponseResult login(AdUserDto dto){
        //1.检查参数
        if(StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"用户名或密码为空");
        }

        //2.查询用户
        AdUser adUser = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, dto.getName()));
        if(adUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String salt = adUser.getSalt();
        String password = dto.getPassword();
        password= DigestUtils.md5DigestAsHex((salt + password).getBytes());
        if (password.equals(adUser.getPassword())){
            //4.返回数据  jwt
            Map<String,Object> map  = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(String.valueOf(adUser.getId().longValue())));
            adUser.setSalt("");
            adUser.setPassword("");
            map.put("user",adUser);
            return ResponseResult.okResult(map);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
    }
    public ResponseResult addUser(AddAdmin admin){
        if (admin.getPassword() == null || admin.getName()==null) {
            return ResponseResult.okResult(200,"参数错误");
        }
        AdUser adUser1 = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, admin.getName()));
        if (adUser1 != null) {
            return ResponseResult.okResult(200,"用户已存在");
        }
        AdUser adUser = new AdUser();
        BeanUtils.copyProperties(admin,adUser);
        adUser.setPassword(DigestUtils.md5DigestAsHex((admin.getSalt() + admin.getPassword()).getBytes()));
        adUser.setCreatedTime(new Date());
        adUser.setLoginTime(new Date());
        adUser.setStatus(0);
        adUserMapper.insert(adUser);
        AdUserDto adUserDto = new AdUserDto();
        BeanUtils.copyProperties(admin,adUserDto);
        return login(adUserDto);
    }

}
