package com.cyn.blog.service;

import com.cyn.blog.entity.param.PageParams;
import com.cyn.blog.entity.vo.Result;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:38
 */

public interface ArticleService {
    Result listArticles(PageParams pageParams);

    Result getHotArticle(int limit);

    Result getNewArticle(int limit);

    Result getListArchives();

    Result getArticleBodyById(Long id);
}
