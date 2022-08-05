package com.cyn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyn.blog.entity.param.ArticleBodyParam;
import com.cyn.blog.entity.param.ArticleParam;
import com.cyn.blog.entity.param.PageParams;
import com.cyn.blog.entity.pojo.*;
import com.cyn.blog.entity.vo.*;
import com.cyn.blog.mapper.ArticleBodyMapper;
import com.cyn.blog.mapper.ArticleMapper;
import com.cyn.blog.mapper.ArticleTagMapper;
import com.cyn.blog.service.*;
import com.cyn.blog.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:40
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据创建时间降序显示文章
     * 00000
     *
     * @param pageParams:
     * @return com.cyn.blog.entity.vo.Result
     * @Description: 分页查询数据库表进行数据查询
     * @author G0dc
     * @date 2022/7/25 15:42
     */
    @Override
    public Result listArticles(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 根据置顶和创建时间排序
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> articles = articlePage.getRecords();
        List<ArticleVo> articleVoList = copyList(articles, true, true, false, false);
        return Result.success(articleVoList);
    }

    /**
     * @param limit: 查询文章数
     * @return com.cyn.blog.entity.vo.Result
     * @Description: 查询最热文章
     * @author G0dc
     * @date 2022/7/26 15:32
     */
    @Override
    public Result getHotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .orderByDesc(Article::getViewCounts)
                .select(Article::getId, Article::getTitle)
                .last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false, false, false));
    }

    /**
     * @param limit: 查询文章数目
     * @return com.cyn.blog.entity.vo.Result
     * @Description: 查询最新发布的文章
     * @author G0dc
     * @date 2022/7/26 15:33
     */
    @Override
    public Result getNewArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .orderByDesc(Article::getCreateDate)
                .select(Article::getTitle, Article::getId)
                .last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false, false, false));
    }

    /**
     * @return com.cyn.blog.entity.vo.Result
     * @Description: 查询文章归档
     * @author G0dc
     * @date 2022/7/26 15:40
     */
    @Override
    public Result getListArchives() {
        List<Archive> archiveList = articleMapper.getArchiveList();

        return Result.success(archiveList);
    }

    @Autowired
    private ThreadService threadService;

    /**
     * 查询完整文章内容
     *
     * @param id:
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/29 7:31
     */
    @Override
    public Result getArticleBodyById(Long id) {
        //
        Article article = articleMapper.selectById(id);
        // 2.将查得的结果转换为Vo对象
        ArticleVo articleVo = copyArticle(article, true, true, true, true);
        // 3.更新浏览数
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    @Autowired
    private ArticleTagMapper articleTagMapper;

    /**
     * 将文章写入数据库
     * 1.插入article表
     * 2.将文章主体插入article_body表
     * 3.将文章id插入article_tag表
     * 4.category新增
     * @param articleParam:新增文章实体类
     * @return com.cyn.blog.entity.vo.Result
     * @author Godc
     * @date 2022/8/5 16:16
     */
    @Override
    public Result publishArticle(ArticleParam articleParam) {
        // 获取当前用户信息
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId()); // 设置作者id
        article.setCreateDate(System.currentTimeMillis()); // 设置当前时间
        article.setTitle(articleParam.getTitle()); // 设置title
        article.setSummary(articleParam.getSummary()); // 设置概述
        article.setViewCounts(0); // 初始化浏览量
        article.setWeight(0); // 初始化为不置顶
        article.setCategoryId(articleParam.getCategory().getId()); // 设置Category
        article.setCommentCounts(0); //初始化评论数

        // tips:在将article放入数据库时会生成id
        // 获取id后再对tags和body等进行插入数据库表操作
        articleMapper.insert(article);
        Long articleId = article.getId();

        // 1.将文章内容插入article_body表 对应实体类为 ArticleBody
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(articleId);
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        Long articleBodyId = articleBody.getId();
        article.setBodyId(articleBodyId);
        // 2.将tag插入article_tag表 对应实体类为 ArticleTag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tagVo : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(tagVo.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        // final 更新article表
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",articleId.toString());
        return Result.success(map);
    }

    //  -------------------private methods---------------------

    /**
     * 将 Article 集合转换为 ArticleVo 集合
     *
     * @param articles:
     * @return java.util.List<com.cyn.blog.entity.vo.ArticleVo>
     * @author G0dc
     * @date 2022/7/25 22:11
     */
    private List<ArticleVo> copyList(List<Article> articles, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        return articles.stream()
                .map((Article article) -> copyArticle(article, isTag, isAuthor, isBody, isCategory))
                .collect(Collectors.toList());
    }

    /**
     * 将 Article 转换为 ArticleVo
     *
     * @param article:
     * @param isTag:    true需要标签信息
     * @param isAuthor: true需要作者信息
     * @return com.cyn.blog.entity.vo.ArticleVo
     * @author G0dc
     * @date 2022/7/25 22:10
     */
    private ArticleVo copyArticle(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        if (isTag) {
            // 文章标签信息
            articleVo.setTags(tagService.findTagsByArticleId(article.getId()));
        }
        if (isAuthor) {
            // 获取作者信息
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            // 获取文章本体
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            // 获取文章分类
            Long categoryId = article.getCategoryId();
            Category category = categoryService.findCategoryById(categoryId);
            articleVo.setCategory(category);
        }
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        // 展示MarkDown语法
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
