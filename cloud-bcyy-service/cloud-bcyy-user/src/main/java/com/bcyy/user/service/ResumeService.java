package com.bcyy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bcyy.model.common.dtos.ResponseResult;
import com.bcyy.model.user.dvos.ResumeDvo;
import com.bcyy.model.user.pojos.Resume;

public interface ResumeService extends IService<Resume> {
    ResponseResult upResume(ResumeDvo resumeDvo);
}
