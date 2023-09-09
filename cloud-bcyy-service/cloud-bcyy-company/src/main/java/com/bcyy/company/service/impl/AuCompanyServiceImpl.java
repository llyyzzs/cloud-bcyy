package com.bcyy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.apis.user.WxUserApi;
import com.bcyy.company.mapper.AuCompanyMapper;
import com.bcyy.company.mapper.HomeCompanyMapper;
import com.bcyy.company.service.AuCompanyService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dto.AuCompanyDto;
import com.bcyy.model.company.pojos.AuCompany;
import com.bcyy.model.company.pojos.HomeCompany;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@Transactional
public class AuCompanyServiceImpl extends ServiceImpl<AuCompanyMapper, AuCompany> implements AuCompanyService {
    @Autowired
    AuCompanyMapper auCompanyMapper;
    @Autowired
    WxUserApi wxUserApi;
    @Autowired
    HomeCompanyMapper homeCompanyMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /*
     * 认证公司
     * */
    public ResponseResult AuCompany(String name, HashSet<String> files){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Object data = wxUserApi.getuser(openid).getData();
        String s = JSON.toJSONString(data);
        WxUser user = JSON.parseObject(s, WxUser.class);
        if (user == null) {
            return ResponseResult.okResult(200,"未登录");
        }
        HomeCompany homeCompany = homeCompanyMapper.selectOne(new QueryWrapper<HomeCompany>().eq("name", name));
        if (homeCompany==null){
            return ResponseResult.okResult(200,"该公司不存在");
        }
        AuCompany auCompany = new AuCompany();
        auCompany.setUser(user);
        auCompany.setName(name);
        auCompany.setFiles(files);
        auCompany.setState(0);
        auCompany.setCompanyId(homeCompany.getId());
        auCompanyMapper.insert(auCompany);
        return ResponseResult.okResult(200,"提交成功");
    }
    /*
     * 获取待认证
     * */
    public ResponseResult getAu(){
        List<AuCompany> auCompanyList = auCompanyMapper.selectList(new QueryWrapper<AuCompany>().eq("state", 0));
        return ResponseResult.okResult(auCompanyList);
    }
    /*
     * 管理员审批
     * */
    public ResponseResult upAu(AuCompanyDto auCompanyDto){
        AuCompany auCompany = new AuCompany();
        BeanUtils.copyProperties(auCompanyDto,auCompany);
        auCompanyMapper.update(auCompany,new
                QueryWrapper<AuCompany>().eq("id",auCompanyDto.getAuId()));
        if (auCompanyDto.getState()==1){
            UserUp user = new UserUp();
            BeanUtils.copyProperties(auCompanyDto.getUser(),user);
            user.setCompanyId(auCompanyDto.getCompanyId());
            //异步通信
//            rabbitTemplate.convertAndSend("bcyy_company","upUser",user);
            wxUserApi.update(user);
        }
        return ResponseResult.okResult(200,"审批成功");
    }
}
