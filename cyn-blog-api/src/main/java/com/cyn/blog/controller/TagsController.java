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

    @GetMapping("/hot")
    public Result getHotTags(){
        int limit = 6;
        return tagService.getHotTags(limit);
    }
}
