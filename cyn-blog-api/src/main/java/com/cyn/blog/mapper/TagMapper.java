package com.cyn.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyn.blog.entity.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:29
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id获取tags
     * @param articleId:
     * @return java.util.List<com.cyn.blog.entity.pojo.Tag>
     * @author G0dc
     * @date 2022/7/25 22:29
     */
    List<Tag> findTagsByArticleId(@Param("articleId") Long articleId);

    /**
     * 查询最热标签
     * @param limit: 显示前几的标签
     * @return java.util.List<java.lang.Long>
     * @author G0dc
     * @date 2022/7/25 23:13
     */
    List<Long> findTagIds(@Param("limit") int limit);
}
