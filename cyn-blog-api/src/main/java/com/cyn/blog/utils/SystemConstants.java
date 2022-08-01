package com.cyn.blog.utils;

import com.cyn.blog.entity.pojo.SysUser;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/28 19:50
 */
public class SystemConstants {
    public static final String REDIS_TOKEN_KEY = "Token:";
    public static final SysUser DEFAULT_USER = createDefaultUser();

    private static SysUser createDefaultUser(){
        SysUser sysUser = new SysUser();
        sysUser.setId(1L);
        sysUser.setNickname("站长");
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        return sysUser;
    }
}
