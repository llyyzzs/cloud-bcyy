package com.bcyy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dvos.ResumeDvo;
import com.bcyy.model.user.pojos.Resume;
import com.bcyy.user.mapper.ResumeMapper;
import com.bcyy.user.service.ResumeService;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements ResumeService {
    @Autowired
    ResumeMapper resumeMapper;
    /*
    * 更新、添加简历
    * */
    public ResponseResult upResume(ResumeDvo resumeDvo){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        if (openid == null) {
            return ResponseResult.okResult(200,"未登录");
        }
        Resume resume = new Resume();
        BeanUtils.copyProperties(resumeDvo,resume);
        resume.setOpenid(openid);
        if (resumeDvo.getResumeId() != null) {
            resumeMapper.update(resume,new QueryWrapper<Resume>().eq("id",resumeDvo.getResumeId()));
            return ResponseResult.okResult(200,"更新成功");
        }
        else {
            resumeMapper.insert(resume);
            return ResponseResult.okResult(200,"添加成功");
        }
    }
    /*
    * 获取所有简历*/
    public ResponseResult getResumeList(){
        String openid = AppThreadLocalUtil.getUser().getOpenid();
        if (openid == null) {
            return ResponseResult.okResult(200,"未登录");
        }
        List<Resume> resumeList = resumeMapper.selectList(new QueryWrapper<Resume>()
                .eq("openid", openid));
        return ResponseResult.okResult(resumeList);
    }
    /*
    * 获取简历
    * */
    public ResponseResult getResume(Integer id){
        Resume resume = resumeMapper.selectOne(new QueryWrapper<Resume>().eq("id", id));
        return ResponseResult.okResult(resume);
    }
    /*
    * 删除简历
    * */
    public ResponseResult deleteResume(Integer id){
        if (id == null) {
            resumeMapper.delete(new QueryWrapper<>());
        }else{
            resumeMapper.delete(new QueryWrapper<Resume>().eq("id",id));
        }
        return ResponseResult.okResult(200,"删除成功");
    }

}
