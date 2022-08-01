package com.cyn.blog.controller;

import com.cyn.blog.entity.param.PageParams;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:31
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleServiceImpl;

    /**
     * 首页 文章列表
     *
     * @param pageParams: 分页
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/25 15:37
     */
    @PostMapping
    public Result listArticles(@RequestBody PageParams pageParams){
        return articleServiceImpl.listArticles(pageParams);
    }

    /**
     * @Description: 首页  最热文章(取前五条)

     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/26 15:29
     */
    @PostMapping("/hot")
    public Result getHotArticle(){
        int limit = 5;
        return articleServiceImpl.getHotArticle(limit);
    }

    /**
     * @Description: 列出最新文章

     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/26 15:38
     */

    @PostMapping("/new")
    public Result getNewArticle(){
        int limit = 5;
        return articleServiceImpl.getNewArticle(limit);
    }

    /**
     * @Description: 查询文章归档

     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/26 15:39
     */

    @PostMapping("/listArchives")
    public Result getListArchives(){
        return articleServiceImpl.getListArchives();
    }

    @PostMapping("/view/{id}")
    public Result getArticleBodyById(@PathVariable("id") Long id){
        return articleServiceImpl.getArticleBodyById(id);
    }
}

