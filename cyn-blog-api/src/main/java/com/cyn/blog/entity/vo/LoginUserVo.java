package com.cyn.blog.entity.vo;

import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/27 14:39
 */
@Data
public class LoginUserVo {
    private Long id;

    private String account;

    private String avatar;

    private String nickname;
}
