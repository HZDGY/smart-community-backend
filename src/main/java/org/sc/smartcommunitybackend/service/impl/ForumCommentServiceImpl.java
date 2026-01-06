package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.*;
import org.sc.smartcommunitybackend.dto.request.CreateCommentRequest;
import org.sc.smartcommunitybackend.dto.response.CommentResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.*;
import org.sc.smartcommunitybackend.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 论坛评论Service实现
 */
@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment>
        implements ForumCommentService {
    
    @Autowired
    private ForumCommentMapper forumCommentMapper;
    
    @Autowired
    private ForumPostMapper forumPostMapper;
    
    @Autowired
    private ForumCommentLikeMapper commentLikeMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createComment(Long userId, CreateCommentRequest request) {
        // 验证帖子是否存在
        ForumPost post = forumPostMapper.selectById(request.getPostId());
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在或已删除");
        }
        
        // 如果是回复评论，验证父评论是否存在
        if (request.getParentId() != null && request.getParentId() > 0) {
            ForumComment parentComment = getById(request.getParentId());
            if (parentComment == null || parentComment.getStatus() == 0) {
                throw new BusinessException("父评论不存在或已删除");
            }
        }
        
        // 创建评论
        ForumComment comment = new ForumComment();
        comment.setPostId(request.getPostId());
        comment.setUserId(userId);
        comment.setParentId(request.getParentId() != null ? request.getParentId() : 0L);
        comment.setReplyToUserId(request.getReplyToUserId());
        comment.setContent(request.getContent());
        comment.setLikeCount(0);
        comment.setStatus(1);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        
        save(comment);
        
        // 增加帖子评论数
        forumPostMapper.incrementCommentCount(request.getPostId());
        
        return comment.getCommentId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComment(Long userId, Long commentId) {
        ForumComment comment = getById(commentId);
        if (comment == null || comment.getStatus() == 0) {
            throw new BusinessException("评论不存在或已删除");
        }
        
        // 验证是否是评论作者
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此评论");
        }
        
        // 软删除
        comment.setStatus(0);
        comment.setUpdateTime(new Date());
        boolean result = updateById(comment);
        
        if (result) {
            // 减少帖子评论数
            forumPostMapper.decrementCommentCount(comment.getPostId());
        }
        
        return result;
    }
    
    @Override
    public Page<CommentResponse> getCommentsByPostId(Long postId, Long userId, Integer pageNum, Integer pageSize) {
        // 查询所有一级评论
        LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumComment::getPostId, postId)
                .eq(ForumComment::getParentId, 0)
                .eq(ForumComment::getStatus, 1)
                .orderByDesc(ForumComment::getCreateTime);
        
        Page<ForumComment> page = new Page<>(pageNum, pageSize);
        Page<ForumComment> resultPage = page(page, wrapper);
        
        // 转换为响应对象
        Page<CommentResponse> responsePage = new Page<>();
        responsePage.setCurrent(resultPage.getCurrent());
        responsePage.setSize(resultPage.getSize());
        responsePage.setTotal(resultPage.getTotal());
        responsePage.setPages(resultPage.getPages());
        
        List<CommentResponse> responseList = resultPage.getRecords().stream()
                .map(comment -> convertToResponse(comment, userId))
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeComment(Long userId, Long commentId) {
        // 检查是否已点赞
        LambdaQueryWrapper<ForumCommentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumCommentLike::getCommentId, commentId)
                .eq(ForumCommentLike::getUserId, userId);
        
        if (commentLikeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经点赞过了");
        }
        
        // 添加点赞记录
        ForumCommentLike like = new ForumCommentLike();
        like.setCommentId(commentId);
        like.setUserId(userId);
        like.setCreateTime(new Date());
        commentLikeMapper.insert(like);
        
        // 增加评论点赞数
        forumCommentMapper.incrementLikeCount(commentId);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeComment(Long userId, Long commentId) {
        // 删除点赞记录
        LambdaQueryWrapper<ForumCommentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumCommentLike::getCommentId, commentId)
                .eq(ForumCommentLike::getUserId, userId);
        
        int deleted = commentLikeMapper.delete(wrapper);
        if (deleted > 0) {
            // 减少评论点赞数
            forumCommentMapper.decrementLikeCount(commentId);
            return true;
        }
        
        return false;
    }
    
    /**
     * 转换为响应对象（包含子评论）
     */
    private CommentResponse convertToResponse(ForumComment comment, Long userId) {
        // 查询用户信息
        SysUser user = sysUserMapper.selectById(comment.getUserId());
        
        // 查询回复的用户信息
        String replyToUserName = null;
        if (comment.getReplyToUserId() != null) {
            SysUser replyToUser = sysUserMapper.selectById(comment.getReplyToUserId());
            if (replyToUser != null) {
                replyToUserName = replyToUser.getUserName();
            }
        }
        
        // 查询子评论
        LambdaQueryWrapper<ForumComment> replyWrapper = new LambdaQueryWrapper<>();
        replyWrapper.eq(ForumComment::getParentId, comment.getCommentId())
                .eq(ForumComment::getStatus, 1)
                .orderByAsc(ForumComment::getCreateTime);
        
        List<ForumComment> replies = list(replyWrapper);
        List<CommentResponse> replyResponses = replies.stream()
                .map(reply -> convertToResponse(reply, userId))
                .collect(Collectors.toList());
        
        // 检查当前用户是否已点赞
        Boolean isLiked = false;
        if (userId != null) {
            LambdaQueryWrapper<ForumCommentLike> likeWrapper = new LambdaQueryWrapper<>();
            likeWrapper.eq(ForumCommentLike::getCommentId, comment.getCommentId())
                    .eq(ForumCommentLike::getUserId, userId);
            isLiked = commentLikeMapper.selectCount(likeWrapper) > 0;
        }
        
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .userName(user != null ? user.getUserName() : "")
                .userAvatar(user != null ? user.getAvatar() : "")
                .parentId(comment.getParentId())
                .replyToUserId(comment.getReplyToUserId())
                .replyToUserName(replyToUserName)
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .isLiked(isLiked)
                .createTime(comment.getCreateTime())
                .replies(replyResponses)
                .build();
    }
}
