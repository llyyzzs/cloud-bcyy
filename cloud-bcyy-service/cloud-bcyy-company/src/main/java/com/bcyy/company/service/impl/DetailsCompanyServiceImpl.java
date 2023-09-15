package com.bcyy.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.apis.user.WxUserApi;
import com.bcyy.common.redis.CacheService;
import com.bcyy.company.mapper.DetailsCompanyMapper;
import com.bcyy.company.mapper.HomeCompanyMapper;
import com.bcyy.company.service.DetailsCompanyService;
import com.bcyy.file.service.FileStorageService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.company.dto.CompanyDto;
import com.bcyy.model.company.dto.CompanyUser;
import com.bcyy.model.company.dvo.CompanyDvo;
import com.bcyy.model.company.pojos.DetailsCompany;
import com.bcyy.model.company.pojos.HomeCompany;
import com.bcyy.model.user.dtos.UserUp;
import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class DetailsCompanyServiceImpl extends ServiceImpl<DetailsCompanyMapper, DetailsCompany>implements DetailsCompanyService {
    @Autowired
    DetailsCompanyMapper detailsCompanyMapper;
    @Autowired
    HomeCompanyMapper homeCompanyMapper;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    WxUserApi wxUserApi;
    @Autowired
    CacheService cacheService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     *获取公司详情
     * */
    public ResponseResult GetDetailCompany(String id){
        String s = cacheService.get("DetailCompany_" + id);
        if (s != null) {
            return ResponseResult.okResult(JSON.parseObject(s,CompanyDvo.class));
        }
        DetailsCompany detailsCompany = detailsCompanyMapper.selectById(id);
        HomeCompany homeCompany = homeCompanyMapper.selectById(id);
        CompanyDvo companyDvo = new CompanyDvo();
        BeanUtils.copyProperties(homeCompany,companyDvo);
        BeanUtils.copyProperties(detailsCompany,companyDvo);
        cacheService.append("DetailCompany_" + id,JSON.toJSONString(companyDvo));
        return ResponseResult.okResult(companyDvo);
    }

    /**
     * 添加或修改公司信息
     * @param companyDto
     */
    @GlobalTransactional
    public ResponseResult AddCompany(CompanyDto companyDto){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        Object data = wxUserApi.getuser(openid).getData();
        HomeCompany homeCompany1 = homeCompanyMapper.selectOne(new
                QueryWrapper<HomeCompany>().eq("name", companyDto.getName()));
        if (companyDto.getCompanyId() == null||homeCompany1!=null) {
            ResponseResult.okResult(200,"未登录或已存在该公司");
        }
        String s = JSON.toJSONString(data);
        WxUser user = JSON.parseObject(s, WxUser.class);
        //填充用户信息
        CompanyUser companyUser = new CompanyUser();
        BeanUtils.copyProperties(user,companyUser);
        DetailsCompany detailsCompany = new DetailsCompany();
        HomeCompany homeCompany = new HomeCompany();
        BeanUtils.copyProperties(companyDto,homeCompany);
        BeanUtils.copyProperties(companyDto,detailsCompany);
        if (companyDto.getCompanyId() == null) {
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            homeCompany.setId(id);
            detailsCompany.setId(id);
            HashSet<CompanyUser> users = new HashSet<>();
            users.add(companyUser);
            detailsCompany.setUsers(users);
            homeCompany.setState(0);
            detailsCompanyMapper.insert(detailsCompany);
            homeCompanyMapper.insert(homeCompany);
            //修改个人公司认证
            user.setCompanyId(id);
            UserUp userUp = new UserUp();
            BeanUtils.copyProperties(user,userUp);
//            rabbitTemplate.convertAndSend("bcyy_company","upUser",userUp);
            CompanyDvo companyDvo = new CompanyDvo();
            BeanUtils.copyProperties(homeCompany,companyDvo);
            BeanUtils.copyProperties(detailsCompany,companyDvo);
            rabbitTemplate.convertAndSend("company.topic","company.insert",companyDvo);
            wxUserApi.update(userUp);
        }else {
            homeCompany.setId(companyDto.getCompanyId());
            detailsCompany.setId(companyDto.getCompanyId());
            detailsCompanyMapper.update(detailsCompany,new QueryWrapper<DetailsCompany>()
                    .eq("id",detailsCompany.getId()));
            homeCompanyMapper.update(homeCompany,new QueryWrapper<HomeCompany>()
                    .eq("id",homeCompany.getId()));
            cacheService.delete("DetailCompany_" + companyDto.getCompanyId());
            rabbitTemplate.convertAndSend("company.topic","company.delete",homeCompany.getId());
            rabbitTemplate.convertAndSend("company.topic","company.insert",homeCompany);
        }
        return ResponseResult.okResult(200,"操作成功");
    }

    /*
    * 修改公司状态
    * */
    public ResponseResult upState(String id,Integer state){
        HomeCompany homeCompany = new HomeCompany();
        homeCompany.setState(state);
        homeCompanyMapper.update(homeCompany,new QueryWrapper<HomeCompany>().eq("id",id));
        return ResponseResult.okResult(200,"修改成功");
    }
    /**
     * 删除公司
     * */
    public ResponseResult delete(String id){
        detailsCompanyMapper.deleteById(id);
        homeCompanyMapper.deleteById(id);
        cacheService.delete("DetailCompany_" + id);
        //下架该公司所有招聘
        rabbitTemplate.convertAndSend("company.topic","delete.company.item",id);
        rabbitTemplate.convertAndSend("company.topic","company.insert",id);
        return ResponseResult.okResult(200,"删除成功");
    }
    /**
    * 上传图片
     * */
    public ResponseResult imageUpload(MultipartFile multipartFile){
        String fileUrl = fileStorageService.fileUrl(multipartFile);
        if (fileUrl == null) {
            return ResponseResult.errorResult(401,"文件上传失败");
        }
        else {
            return ResponseResult.okResult(fileUrl);
        }
    }
}
