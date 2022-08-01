package com.cyn.blog.entity.param;

import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/8/1 20:13
 */
@Data
public class CommentParam {

    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
