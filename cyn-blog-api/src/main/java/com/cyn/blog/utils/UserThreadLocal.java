package com.cyn.blog.utils;

import com.cyn.blog.entity.pojo.SysUser;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/28 20:29
 */
public class UserThreadLocal {
    /**
     * 私有构造方法
     *
     * @return
     * @author G0dc
     * @date 2022/7/28 20:30
     */
    private UserThreadLocal() {
    }

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
