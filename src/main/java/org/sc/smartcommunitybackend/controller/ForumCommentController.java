package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.CreateCommentRequest;
import org.sc.smartcommunitybackend.dto.response.CommentResponse;
import org.sc.smartcommunitybackend.service.ForumCommentService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 论坛评论Controller
 */
@RestController
@RequestMapping("/forum/comment")
@Tag(name = "论坛评论管理", description = "论坛评论相关接口")
public class ForumCommentController extends BaseController {
    
    @Autowired
    private ForumCommentService forumCommentService;
    
    @PostMapping("/create")
    @Operation(summary = "发表评论", description = "发表评论或回复")
    public Result<Long> createComment(@RequestBody @Valid CreateCommentRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        Long commentId = forumCommentService.createComment(userId, request);
        return success("评论成功", commentId);
    }
    
    @DeleteMapping("/{commentId}")
    @Operation(summary = "删除评论", description = "删除评论")
    public Result<Void> deleteComment(@Parameter(description = "评论ID") @PathVariable Long commentId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumCommentService.deleteComment(userId, commentId);
        return success("删除成功", null);
    }
    
    @GetMapping("/post/{postId}")
    @Operation(summary = "获取帖子评论", description = "获取帖子的评论列表（树形结构）")
    public Result<Page<CommentResponse>> getCommentsByPostId(
            @Parameter(description = "帖子ID") @PathVariable Long postId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserIdOrNull();
        Page<CommentResponse> page = forumCommentService.getCommentsByPostId(postId, userId, pageNum, pageSize);
        return success(page);
    }
    
    @PostMapping("/{commentId}/like")
    @Operation(summary = "点赞评论", description = "给评论点赞")
    public Result<Void> likeComment(@Parameter(description = "评论ID") @PathVariable Long commentId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumCommentService.likeComment(userId, commentId);
        return success("点赞成功", null);
    }
    
    @DeleteMapping("/{commentId}/like")
    @Operation(summary = "取消点赞", description = "取消评论点赞")
    public Result<Void> unlikeComment(@Parameter(description = "评论ID") @PathVariable Long commentId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumCommentService.unlikeComment(userId, commentId);
        return success("取消点赞成功", null);
    }
}
