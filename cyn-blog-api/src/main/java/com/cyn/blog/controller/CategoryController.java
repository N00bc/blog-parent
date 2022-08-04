package com.cyn.blog.controller;

import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result getCategoriesList(){
        return categoryService.getAllCategories();
    }

}