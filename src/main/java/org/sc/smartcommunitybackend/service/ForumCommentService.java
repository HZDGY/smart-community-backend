package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.domain.ForumComment;
import org.sc.smartcommunitybackend.dto.request.CreateCommentRequest;
import org.sc.smartcommunitybackend.dto.response.CommentResponse;

import java.util.List;

/**
 * 论坛评论Service
 */
public interface ForumCommentService extends IService<ForumComment> {
    
    /**
     * 创建评论
     *
     * @param userId 用户ID
     * @param request 创建评论请求
     * @return 评论ID
     */
    Long createComment(Long userId, CreateCommentRequest request);
    
    /**
     * 删除评论
     *
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否成功
     */
    boolean deleteComment(Long userId, Long commentId);
    
    /**
     * 获取帖子的评论列表（树形结构）
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID（可为null）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 评论列表
     */
    Page<CommentResponse> getCommentsByPostId(Long postId, Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 点赞评论
     *
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否成功
     */
    boolean likeComment(Long userId, Long commentId);
    
    /**
     * 取消点赞评论
     *
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否成功
     */
    boolean unlikeComment(Long userId, Long commentId);
}
