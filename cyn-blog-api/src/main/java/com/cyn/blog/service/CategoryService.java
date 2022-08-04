package com.cyn.blog.service;

import com.cyn.blog.entity.pojo.Category;
import com.cyn.blog.entity.vo.CategoryVo;
import com.cyn.blog.entity.vo.Result;

import java.util.List;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/29 12:46
 */
public interface CategoryService {
    Category findCategoryById(Long categoryId);

    Result getAllCategories();
}
