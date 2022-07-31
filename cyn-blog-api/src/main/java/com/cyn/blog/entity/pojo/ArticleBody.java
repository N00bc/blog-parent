package com.cyn.blog.entity.pojo;

import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/29 6:59
 */
@Data
public class ArticleBody {
    private Long id;
    private String content;
    private String contentHtml;
    private Long articleId;
}
