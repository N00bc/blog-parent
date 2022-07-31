package com.cyn.blog.service;

import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.entity.vo.TagVo;

import java.util.List;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 22:13
 */
public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result getHotTags(int limit);
}
