package com.cyn.blog.entity.param;

import com.cyn.blog.entity.vo.CategoryVo;
import com.cyn.blog.entity.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}