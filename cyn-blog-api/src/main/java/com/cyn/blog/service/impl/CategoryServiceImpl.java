package com.cyn.blog.service.impl;

import com.cyn.blog.entity.pojo.Category;
import com.cyn.blog.entity.vo.CategoryVo;
import com.cyn.blog.mapper.CategoryMapper;
import com.cyn.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/29 12:46
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据文章分类id查找 分类实体
     *
     * @param categoryId: 文章分类id
     * @return com.cyn.blog.entity.vo.CategoryVo
     * @author G0dc
     * @date 2022/7/29 23:08
     */
    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }
    //  -------------------private methods---------------------
}
