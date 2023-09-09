package com.bcyy.chat.interceptor;

import com.bcyy.model.user.pojos.WxUser;
import com.bcyy.utils.thread.AppThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userOpenId = request.getHeader("userId");
        if(userOpenId != null){
            //存入到当前线程中
            WxUser apUser = new WxUser();
            apUser.setOpenid(userOpenId);
            AppThreadLocalUtil.setUser(apUser);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppThreadLocalUtil.clear();
    }
}
