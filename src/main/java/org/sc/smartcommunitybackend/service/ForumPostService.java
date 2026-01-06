package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.domain.ForumPost;
import org.sc.smartcommunitybackend.dto.request.CreatePostRequest;
import org.sc.smartcommunitybackend.dto.request.PostListRequest;
import org.sc.smartcommunitybackend.dto.request.UpdatePostRequest;
import org.sc.smartcommunitybackend.dto.response.PostDetailResponse;
import org.sc.smartcommunitybackend.dto.response.PostListItemResponse;

/**
 * 论坛帖子Service
 */
public interface ForumPostService extends IService<ForumPost> {
    
    /**
     * 创建帖子
     *
     * @param userId 用户ID
     * @param request 创建帖子请求
     * @return 帖子ID
     */
    Long createPost(Long userId, CreatePostRequest request);
    
    /**
     * 更新帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @param request 更新帖子请求
     * @return 是否成功
     */
    boolean updatePost(Long userId, Long postId, UpdatePostRequest request);
    
    /**
     * 删除帖子（软删除）
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean deletePost(Long userId, Long postId);
    
    /**
     * 获取帖子详情
     *
     * @param postId 帖子ID
     * @param userId 当前用户ID（可为null）
     * @return 帖子详情
     */
    PostDetailResponse getPostDetail(Long postId, Long userId);
    
    /**
     * 分页查询帖子列表
     *
     * @param request 查询请求
     * @return 帖子列表
     */
    Page<PostListItemResponse> getPostList(PostListRequest request);
    
    /**
     * 点赞帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean likePost(Long userId, Long postId);
    
    /**
     * 取消点赞帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean unlikePost(Long userId, Long postId);
    
    /**
     * 收藏帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean collectPost(Long userId, Long postId);
    
    /**
     * 取消收藏帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否成功
     */
    boolean uncollectPost(Long userId, Long postId);
    
    /**
     * 设置帖子置顶
     *
     * @param postId 帖子ID
     * @param isTop 是否置顶
     * @return 是否成功
     */
    boolean setTop(Long postId, Integer isTop);
    
    /**
     * 设置帖子精华
     *
     * @param postId 帖子ID
     * @param isEssence 是否精华
     * @return 是否成功
     */
    boolean setEssence(Long postId, Integer isEssence);
    
    /**
     * 获取我的帖子列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 帖子列表
     */
    Page<PostListItemResponse> getMyPosts(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取我收藏的帖子列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 帖子列表
     */
    Page<PostListItemResponse> getMyCollectedPosts(Long userId, Integer pageNum, Integer pageSize);
}
