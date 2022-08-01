package com.cyn.blog.service;

import com.cyn.blog.entity.pojo.SysUser;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.entity.vo.UserVo;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 22:44
 */
public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result getCurrentUser(String token);

    void saveUser(SysUser user);

    SysUser checkToken(String token);

    UserVo findUserVoById(Long authorId);
}
