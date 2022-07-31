package com.cyn.blog.controller;

import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/27 15:23
 */
@RestController
@RequestMapping("/logout")
public class LogoutController {
    @Autowired
    private LoginService loginService;
    @GetMapping
    public Result logOut(@RequestHeader("Authorization")String token){
        return loginService.logout(token);
    }
}
