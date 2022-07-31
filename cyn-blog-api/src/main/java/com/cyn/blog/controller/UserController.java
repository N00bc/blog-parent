package com.cyn.blog.controller;

import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/27 14:37
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    public Result getCurrentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.getCurrentUser(token);
    }

}
