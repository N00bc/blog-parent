package com.cyn.blog.service;

import com.cyn.blog.entity.vo.LoginParam;
import com.cyn.blog.entity.vo.Result;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/26 17:11
 */
public interface LoginService {
    Result login(LoginParam loginParam);

    Result logout(String token);

    @Transactional
    Result register(LoginParam loginParam);
}
