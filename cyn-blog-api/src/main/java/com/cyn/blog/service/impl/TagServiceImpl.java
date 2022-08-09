package com.cyn.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyn.blog.entity.pojo.Tag;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.entity.vo.TagVo;
import com.cyn.blog.mapper.TagMapper;
import com.cyn.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 22:16
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // 根据id 从cyn_article_tag --> tagId 再从cyn_tag中根据 tagId --> tagName
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result getHotTags(int limit) {
        List<Long> tagIds = tagMapper.findTagIds(limit);
        List<Tag> tags;
        if (tagIds == null) tags = Collections.emptyList();
        else tags = tagMapper.selectBatchIds(tagIds);

        return Result.success(tags);
    }

    /**
     * 查询所有标签
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/8/2 11:51
     */
    @Override
    public Result getAllTags() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        List<TagVo> tagVos = BeanUtil.copyToList(tags, TagVo.class);
        return Result.success(tagVos);
    }

    @Override
    public Result getAllDetail() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(tags);
    }

    @Override
    public Result getDetailById(Long id) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Tag::getId,id);
        Tag tag = tagMapper.selectOne(queryWrapper);
        TagVo tagVo = copyTag(tag);
        return Result.success(tagVo);
    }

    //  -------------------private methods---------------------
    private List<TagVo> copyList(List<Tag> tags) {
        return tags.stream()
                .map(tag -> copyTag(tag))
                .collect(Collectors.toList());
    }

    private TagVo copyTag(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }
}
