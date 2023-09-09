package com.bcyy.utils.thread;


import com.bcyy.model.user.pojos.WxUser;

public class AppThreadLocalUtil {

    private final static ThreadLocal<WxUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 添加用户
     * @param User
     */
    public static void  setUser(WxUser User){
        WM_USER_THREAD_LOCAL.set(User);
    }

    /**
     * 获取用户
     */
    public static WxUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    /**
     * 清理用户
     */
    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }
}
