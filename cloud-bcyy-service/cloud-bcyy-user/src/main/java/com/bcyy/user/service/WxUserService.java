package com.bcyy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.pojos.WxUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

public interface WxUserService extends IService<WxUser> {
    ResponseResult login(String code);
    ResponseResult imageUpload(MultipartFile multipartFile);
    WxUser getUser(String openid);
    ResponseResult updateUser(UserUp user);
}
