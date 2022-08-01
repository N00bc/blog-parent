package com.cyn.blog.controller;

import com.cyn.blog.entity.param.LoginParam;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/26 17:05
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
