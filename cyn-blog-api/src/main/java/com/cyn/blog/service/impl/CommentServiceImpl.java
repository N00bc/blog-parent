package com.cyn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyn.blog.entity.pojo.Comment;
import com.cyn.blog.entity.vo.CommentVo;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.entity.vo.UserVo;
import com.cyn.blog.mapper.CommentMapper;
import com.cyn.blog.service.CommentService;
import com.cyn.blog.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/31 13:27
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 查询文章评论
     * @param articleId: 文章id
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/8/1 13:58
     */
    @Override
    public Result getCommentByArticleId(Long articleId) {
        /*
         * 1.从 comment 表根据文章id 查询 评论列表
         * 2.根据authorId 查询作者信息
         * 3.判断 如果 level = 1 需要去查询它有没有子评论
         * 4.如果有 根据评论id进行查询
         * */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Comment::getArticleId, articleId)
                // TODO 暂时没搞明白这边为啥要在条件语句中加level == 1
                .eq(Comment::getLevel, 1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    //  -------------------private methods---------------------

    private List<CommentVo> copyList(List<Comment> comments) {
        return comments.stream()
                .map(this::copyComment)
                .collect(Collectors.toList());
    }

    private CommentVo copyComment(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        // 查询作者信息
        UserVo userVo = sysUserService.findUserVoById(comment.getAuthorId());
        // 查询子评论
        commentVo.setAuthor(userVo);
        // 如果level == 1需要查询子评论
        Integer isChildComments = comment.getLevel();
        if(1 == isChildComments){
            Long id = comment.getId();
            List<CommentVo> childCommentList = findCommentsByParentId(id);
            commentVo.setChildrens(childCommentList);
        }
        // to User 给谁评论
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        return null;
    }

}