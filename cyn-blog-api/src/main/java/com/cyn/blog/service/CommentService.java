package com.cyn.blog.service;

import com.cyn.blog.entity.param.CommentParam;
import com.cyn.blog.entity.vo.Result;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/31 13:26
 */
public interface CommentService {

    Result getCommentByArticleId(Long articleId);

    Result createComment(CommentParam commentParam);
}
