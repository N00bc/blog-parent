package com.cyn.blog.entity.param;

import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/26 17:10
 */
@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;
}
