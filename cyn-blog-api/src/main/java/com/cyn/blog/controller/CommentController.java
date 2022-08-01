package com.cyn.blog.controller;

import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/31 13:22
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    /**
     * 根据文章id查询评论
     * @param articleId:文章id
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/31 13:24
     */
    @GetMapping("/article/{id}")
    public Result getComment(@PathVariable("id") Long articleId) {
        return commentService.getCommentByArticleId(articleId);
    }
}
