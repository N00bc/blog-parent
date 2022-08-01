package com.cyn.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cyn.blog.entity.pojo.Article;
import com.cyn.blog.mapper.ArticleMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/30 13:21
 */
@Component
public class ThreadService {

    /**
     * 更新viewCount字段
     * 此操作在线程池异步更新
     *
     * @param articleMapper:
     * @param article:
     * @return void
     * @author G0dc
     * @date 2022/7/30 13:26
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        Integer viewCount = article.getViewCounts();
        Article toUpdateArticle = new Article();
        toUpdateArticle.setViewCounts(viewCount + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Article::getId, article.getId())
                .eq(Article::getViewCounts, viewCount);
        articleMapper.update(toUpdateArticle,updateWrapper);
        System.out.println("viewCount字段更新完成");
    }
}
