package com.cyn.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyn.blog.entity.pojo.Category;
import com.cyn.blog.entity.vo.CategoryVo;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.mapper.CategoryMapper;
import com.cyn.blog.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * @Description: TODO

     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/8/2 11:30
     */
    @Override
    public Result getAllCategories() {
        List<Category> categoriesList = categoryMapper.selectList(new LambdaQueryWrapper<Category>());
        List<CategoryVo> categoryVoList = BeanUtil.copyToList(categoriesList,CategoryVo.class);
        return Result.success(categoryVoList);
    }

    //  -------------------private methods---------------------
}
