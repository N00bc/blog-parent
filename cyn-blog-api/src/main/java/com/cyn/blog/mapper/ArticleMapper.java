package com.cyn.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyn.blog.entity.pojo.Article;
import com.cyn.blog.entity.vo.Archive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:27
 */
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archive> getArchiveList();
}
