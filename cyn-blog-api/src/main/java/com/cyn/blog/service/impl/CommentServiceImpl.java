package com.cyn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyn.blog.entity.param.CommentParam;
import com.cyn.blog.entity.pojo.Comment;
import com.cyn.blog.entity.pojo.SysUser;
import com.cyn.blog.entity.vo.CommentVo;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.entity.vo.UserVo;
import com.cyn.blog.gloableEnum.ErrorCode;
import com.cyn.blog.mapper.CommentMapper;
import com.cyn.blog.service.CommentService;
import com.cyn.blog.service.SysUserService;
import com.cyn.blog.utils.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
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
     *
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
                .eq(Comment::getLevel, 1)
                .orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    @Override
    public Result createComment(CommentParam commentParam) {
        // 1.对评论内容进行合法性判断
        if (StringUtils.isBlank(commentParam.getContent()))
            return Result.fail(ErrorCode.COMMENT_BODY_EMPTY.getCode(), ErrorCode.COMMENT_BODY_EMPTY.getMsg());
        // 2.获取当前用户信息
        SysUser sysUser = UserThreadLocal.get();
        // 3.将评论记录到数据库
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        // 3.1 设置parent
        Long parentId = commentParam.getParent();
        comment.setParentId(parentId == null ? 0 : parentId);
        // 3.2 设置Level
        Integer level = (parentId == null || parentId == 0) ? 1 : 2;
        comment.setLevel(level);
        // 3.3 设置to_uid
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentMapper.insert(comment);
        return null;
    }

    //  -------------------private methods---------------------

    /**
     * 将Comment集合转换为CommentVo集合
     *
     * @param comments:评论集合
     * @return java.util.List<com.cyn.blog.entity.vo.CommentVo>
     * @author G0dc
     * @date 2022/8/1 19:36
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        return comments.stream()
                .map(this::copyComment)
                .collect(Collectors.toList());
    }

    /**
     * 根据数据库中Comment信息封装CommentVo
     *
     * @param comment:
     * @return com.cyn.blog.entity.vo.CommentVo
     * @author G0dc
     * @date 2022/8/1 19:34
     */
    private CommentVo copyComment(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        // 查询作者信息
        UserVo userVo = sysUserService.findUserVoById(comment.getAuthorId());
        // 查询子评论
        commentVo.setAuthor(userVo);
        // 如果level == 1需要查询子评论
        Integer isChildComments = comment.getLevel();
        if (1 == isChildComments) {
            Long id = comment.getId();
            List<CommentVo> childCommentList = findCommentsByParentId(id);
            commentVo.setChildrens(childCommentList);
        }
        // to User 给谁评论
        if (isChildComments > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /**
     * 查询子评论
     *
     * @param id:父评论的id
     * @return java.util.List<com.cyn.blog.entity.vo.CommentVo>
     * @author G0dc
     * @date 2022/8/1 19:33
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Comment::getParentId, id)
                .eq(Comment::getLevel, 2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }

}
