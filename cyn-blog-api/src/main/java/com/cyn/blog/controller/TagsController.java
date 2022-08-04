package com.cyn.blog.controller;

import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 23:01
 */
@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    /**
     * 获取最热标签，只取前5
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/8/2 11:50
     */
    @GetMapping("/hot")
    public Result getHotTags(){
        int limit = 6;
        return tagService.getHotTags(limit);
    }
    /**
     * 获取所有标签
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/8/2 11:50
     */
    @GetMapping
    public Result getAllTags(){
        return tagService.getAllTags();
    }
}
