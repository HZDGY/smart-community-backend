package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.CreatePostRequest;
import org.sc.smartcommunitybackend.dto.request.PostListRequest;
import org.sc.smartcommunitybackend.dto.request.UpdatePostRequest;
import org.sc.smartcommunitybackend.dto.response.PostDetailResponse;
import org.sc.smartcommunitybackend.dto.response.PostListItemResponse;
import org.sc.smartcommunitybackend.service.ForumPostService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 论坛帖子Controller
 */
@RestController
@RequestMapping("/forum/post")
@Tag(name = "论坛帖子管理", description = "论坛帖子相关接口")
public class ForumPostController extends BaseController {
    
    @Autowired
    private ForumPostService forumPostService;
    
    @PostMapping("/create")
    @Operation(summary = "发布帖子", description = "用户发布新帖子")
    public Result<Long> createPost(@RequestBody @Valid CreatePostRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        Long postId = forumPostService.createPost(userId, request);
        return success("发布成功", postId);
    }
    
    @PutMapping("/{postId}")
    @Operation(summary = "更新帖子", description = "更新帖子内容")
    public Result<Void> updatePost(
            @Parameter(description = "帖子ID") @PathVariable Long postId,
            @RequestBody @Valid UpdatePostRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumPostService.updatePost(userId, postId, request);
        return success("更新成功", null);
    }
    
    @DeleteMapping("/{postId}")
    @Operation(summary = "删除帖子", description = "删除帖子（软删除）")
    public Result<Void> deletePost(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumPostService.deletePost(userId, postId);
        return success("删除成功", null);
    }
    
    @GetMapping("/{postId}")
    @Operation(summary = "获取帖子详情", description = "获取帖子详细信息")
    public Result<PostDetailResponse> getPostDetail(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = UserContextUtil.getCurrentUserIdOrNull();
        PostDetailResponse detail = forumPostService.getPostDetail(postId, userId);
        return success(detail);
    }
    
    @PostMapping("/list")
    @Operation(summary = "获取帖子列表", description = "分页查询帖子列表")
    public Result<Page<PostListItemResponse>> getPostList(@RequestBody PostListRequest request) {
        Page<PostListItemResponse> page = forumPostService.getPostList(request);
        return success(page);
    }
    
    @PostMapping("/{postId}/like")
    @Operation(summary = "点赞帖子", description = "给帖子点赞")
    public Result<Void> likePost(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumPostService.likePost(userId, postId);
        return success("点赞成功", null);
    }
    
    @DeleteMapping("/{postId}/like")
    @Operation(summary = "取消点赞", description = "取消帖子点赞")
    public Result<Void> unlikePost(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumPostService.unlikePost(userId, postId);
        return success("取消点赞成功", null);
    }
    
    @PostMapping("/{postId}/collect")
    @Operation(summary = "收藏帖子", description = "收藏帖子")
    public Result<Void> collectPost(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumPostService.collectPost(userId, postId);
        return success("收藏成功", null);
    }
    
    @DeleteMapping("/{postId}/collect")
    @Operation(summary = "取消收藏", description = "取消收藏帖子")
    public Result<Void> uncollectPost(@Parameter(description = "帖子ID") @PathVariable Long postId) {
        Long userId = UserContextUtil.getCurrentUserId();
        forumPostService.uncollectPost(userId, postId);
        return success("取消收藏成功", null);
    }
    
    @GetMapping("/my")
    @Operation(summary = "我的帖子", description = "获取我发布的帖子列表")
    public Result<Page<PostListItemResponse>> getMyPosts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<PostListItemResponse> page = forumPostService.getMyPosts(userId, pageNum, pageSize);
        return success(page);
    }
    
    @GetMapping("/my/collected")
    @Operation(summary = "我收藏的帖子", description = "获取我收藏的帖子列表")
    public Result<Page<PostListItemResponse>> getMyCollectedPosts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getCurrentUserId();
        Page<PostListItemResponse> page = forumPostService.getMyCollectedPosts(userId, pageNum, pageSize);
        return success(page);
    }
}
