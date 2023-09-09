package com.bcyy.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcyy.model.user.pojos.WxUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WxUserMapper extends BaseMapper<WxUser> {
    void updateUser(WxUser user);
}
