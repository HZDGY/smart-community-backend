package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.*;
import org.sc.smartcommunitybackend.dto.request.CreatePostRequest;
import org.sc.smartcommunitybackend.dto.request.PostListRequest;
import org.sc.smartcommunitybackend.dto.request.UpdatePostRequest;
import org.sc.smartcommunitybackend.dto.response.PostDetailResponse;
import org.sc.smartcommunitybackend.dto.response.PostListItemResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.*;
import org.sc.smartcommunitybackend.service.ForumPostService;
import org.sc.smartcommunitybackend.service.ForumSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 论坛帖子Service实现
 */
@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost>
        implements ForumPostService {
    
    @Autowired
    private ForumPostMapper forumPostMapper;
    
    @Autowired
    private ForumSectionMapper forumSectionMapper;
    
    @Autowired
    private ForumPostLikeMapper postLikeMapper;
    
    @Autowired
    private ForumPostCollectMapper postCollectMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private ForumSectionService forumSectionService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPost(Long userId, CreatePostRequest request) {
        // 验证板块是否存在
        ForumSection section = forumSectionMapper.selectById(request.getSectionId());
        if (section == null || section.getStatus() == 0) {
            throw new BusinessException("板块不存在或已禁用");
        }
        
        // 创建帖子
        ForumPost post = new ForumPost();
        post.setSectionId(request.getSectionId());
        post.setUserId(userId);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImages(request.getImages());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCollectCount(0);
        post.setIsTop(0);
        post.setIsEssence(0);
        post.setStatus(1); // 正常状态
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());
        
        save(post);
        
        // 增加板块帖子数量
        forumSectionService.incrementPostCount(request.getSectionId());
        
        return post.getPostId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePost(Long userId, Long postId, UpdatePostRequest request) {
        ForumPost post = getById(postId);
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在或已删除");
        }
        
        // 验证是否是帖子作者
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权修改此帖子");
        }
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImages(request.getImages());
        post.setUpdateTime(new Date());
        
        return updateById(post);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePost(Long userId, Long postId) {
        ForumPost post = getById(postId);
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在或已删除");
        }
        
        // 验证是否是帖子作者
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此帖子");
        }
        
        // 软删除
        post.setStatus(0);
        post.setUpdateTime(new Date());
        boolean result = updateById(post);
        
        if (result) {
            // 减少板块帖子数量
            forumSectionService.decrementPostCount(post.getSectionId());
        }
        
        return result;
    }
    
    @Override
    public PostDetailResponse getPostDetail(Long postId, Long userId) {
        ForumPost post = getById(postId);
        if (post == null || post.getStatus() == 0) {
            throw new BusinessException("帖子不存在或已删除");
        }
        
        // 增加浏览次数
        forumPostMapper.incrementViewCount(postId);
        
        // 查询板块信息
        ForumSection section = forumSectionMapper.selectById(post.getSectionId());
        
        // 查询用户信息
        SysUser user = sysUserMapper.selectById(post.getUserId());
        
        // 构建响应
        PostDetailResponse response = PostDetailResponse.builder()
                .postId(post.getPostId())
                .sectionId(post.getSectionId())
                .sectionName(section != null ? section.getSectionName() : "")
                .userId(post.getUserId())
                .userName(user != null ? user.getUserName() : "")
                .userAvatar(user != null ? user.getAvatar() : "")
                .title(post.getTitle())
                .content(post.getContent())
                .images(post.getImages())
                .viewCount(post.getViewCount() + 1) // 加上刚才增加的1次
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .collectCount(post.getCollectCount())
                .isTop(post.getIsTop())
                .isEssence(post.getIsEssence())
                .createTime(post.getCreateTime())
                .updateTime(post.getUpdateTime())
                .build();
        
        // 如果用户已登录，查询是否已点赞和收藏
        if (userId != null) {
            LambdaQueryWrapper<ForumPostLike> likeWrapper = new LambdaQueryWrapper<>();
            likeWrapper.eq(ForumPostLike::getPostId, postId)
                    .eq(ForumPostLike::getUserId, userId);
            response.setIsLiked(postLikeMapper.selectCount(likeWrapper) > 0);
            
            LambdaQueryWrapper<ForumPostCollect> collectWrapper = new LambdaQueryWrapper<>();
            collectWrapper.eq(ForumPostCollect::getPostId, postId)
                    .eq(ForumPostCollect::getUserId, userId);
            response.setIsCollected(postCollectMapper.selectCount(collectWrapper) > 0);
        } else {
            response.setIsLiked(false);
            response.setIsCollected(false);
        }
        
        return response;
    }
    
    @Override
    public Page<PostListItemResponse> getPostList(PostListRequest request) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getStatus, 1); // 只查询正常状态的帖子
        
        // 板块筛选
        if (request.getSectionId() != null) {
            wrapper.eq(ForumPost::getSectionId, request.getSectionId());
        }
        
        // 关键词搜索
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(ForumPost::getTitle, request.getKeyword())
                    .or()
                    .like(ForumPost::getContent, request.getKeyword()));
        }
        
        // 精华筛选
        if (request.getIsEssence() != null && request.getIsEssence() == 1) {
            wrapper.eq(ForumPost::getIsEssence, 1);
        }
        
        // 排序
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "latest";
        switch (sortBy) {
            case "hot":
                // 热度排序：综合点赞数、评论数、浏览数
                wrapper.orderByDesc(ForumPost::getIsTop)
                        .orderByDesc(post -> post.getLikeCount() + post.getCommentCount() + post.getViewCount() / 10);
                break;
            case "essence":
                wrapper.orderByDesc(ForumPost::getIsEssence)
                        .orderByDesc(ForumPost::getCreateTime);
                break;
            case "latest":
            default:
                wrapper.orderByDesc(ForumPost::getIsTop)
                        .orderByDesc(ForumPost::getCreateTime);
                break;
        }
        
        Page<ForumPost> page = new Page<>(request.getPageNo(), request.getPageSize());
        Page<ForumPost> resultPage = page(page, wrapper);
        
        // 转换为响应对象
        Page<PostListItemResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(resultPage, responsePage, "records");
        
        List<PostListItemResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likePost(Long userId, Long postId) {
        // 检查是否已点赞
        LambdaQueryWrapper<ForumPostLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPostLike::getPostId, postId)
                .eq(ForumPostLike::getUserId, userId);
        
        if (postLikeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经点赞过了");
        }
        
        // 添加点赞记录
        ForumPostLike like = new ForumPostLike();
        like.setPostId(postId);
        like.setUserId(userId);
        like.setCreateTime(new Date());
        postLikeMapper.insert(like);
        
        // 增加帖子点赞数
        forumPostMapper.incrementLikeCount(postId);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikePost(Long userId, Long postId) {
        // 删除点赞记录
        LambdaQueryWrapper<ForumPostLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPostLike::getPostId, postId)
                .eq(ForumPostLike::getUserId, userId);
        
        int deleted = postLikeMapper.delete(wrapper);
        if (deleted > 0) {
            // 减少帖子点赞数
            forumPostMapper.decrementLikeCount(postId);
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean collectPost(Long userId, Long postId) {
        // 检查是否已收藏
        LambdaQueryWrapper<ForumPostCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPostCollect::getPostId, postId)
                .eq(ForumPostCollect::getUserId, userId);
        
        if (postCollectMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经收藏过了");
        }
        
        // 添加收藏记录
        ForumPostCollect collect = new ForumPostCollect();
        collect.setPostId(postId);
        collect.setUserId(userId);
        collect.setCreateTime(new Date());
        postCollectMapper.insert(collect);
        
        // 增加帖子收藏数
        forumPostMapper.incrementCollectCount(postId);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uncollectPost(Long userId, Long postId) {
        // 删除收藏记录
        LambdaQueryWrapper<ForumPostCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPostCollect::getPostId, postId)
                .eq(ForumPostCollect::getUserId, userId);
        
        int deleted = postCollectMapper.delete(wrapper);
        if (deleted > 0) {
            // 减少帖子收藏数
            forumPostMapper.decrementCollectCount(postId);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean setTop(Long postId, Integer isTop) {
        LambdaUpdateWrapper<ForumPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ForumPost::getPostId, postId)
                .set(ForumPost::getIsTop, isTop)
                .set(ForumPost::getUpdateTime, new Date());
        return update(wrapper);
    }
    
    @Override
    public boolean setEssence(Long postId, Integer isEssence) {
        LambdaUpdateWrapper<ForumPost> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ForumPost::getPostId, postId)
                .set(ForumPost::getIsEssence, isEssence)
                .set(ForumPost::getUpdateTime, new Date());
        return update(wrapper);
    }
    
    @Override
    public Page<PostListItemResponse> getMyPosts(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumPost::getUserId, userId)
                .eq(ForumPost::getStatus, 1)
                .orderByDesc(ForumPost::getCreateTime);
        
        Page<ForumPost> page = new Page<>(pageNum, pageSize);
        Page<ForumPost> resultPage = page(page, wrapper);
        
        Page<PostListItemResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(resultPage, responsePage, "records");
        
        List<PostListItemResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    public Page<PostListItemResponse> getMyCollectedPosts(Long userId, Integer pageNum, Integer pageSize) {
        // 查询收藏的帖子ID列表
        LambdaQueryWrapper<ForumPostCollect> collectWrapper = new LambdaQueryWrapper<>();
        collectWrapper.eq(ForumPostCollect::getUserId, userId)
                .orderByDesc(ForumPostCollect::getCreateTime);
        
        Page<ForumPostCollect> collectPage = new Page<>(pageNum, pageSize);
        Page<ForumPostCollect> collectResultPage = postCollectMapper.selectPage(collectPage, collectWrapper);
        
        if (collectResultPage.getRecords().isEmpty()) {
            return new Page<>(pageNum, pageSize);
        }
        
        List<Long> postIds = collectResultPage.getRecords().stream()
                .map(ForumPostCollect::getPostId)
                .collect(Collectors.toList());
        
        // 查询帖子详情
        LambdaQueryWrapper<ForumPost> postWrapper = new LambdaQueryWrapper<>();
        postWrapper.in(ForumPost::getPostId, postIds)
                .eq(ForumPost::getStatus, 1);
        
        List<ForumPost> posts = list(postWrapper);
        
        Page<PostListItemResponse> responsePage = new Page<>();
        responsePage.setCurrent(collectResultPage.getCurrent());
        responsePage.setSize(collectResultPage.getSize());
        responsePage.setTotal(collectResultPage.getTotal());
        responsePage.setPages(collectResultPage.getPages());
        
        List<PostListItemResponse> responseList = posts.stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    /**
     * 转换为列表项响应
     */
    private PostListItemResponse convertToListItem(ForumPost post) {
        // 查询板块信息
        ForumSection section = forumSectionMapper.selectById(post.getSectionId());
        
        // 查询用户信息
        SysUser user = sysUserMapper.selectById(post.getUserId());
        
        // 提取首图
        String firstImage = null;
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            String[] images = post.getImages().split(",");
            if (images.length > 0) {
                firstImage = images[0];
            }
        }
        
        // 生成内容摘要（取前100个字符）
        String contentSummary = post.getContent();
        if (contentSummary != null && contentSummary.length() > 100) {
            contentSummary = contentSummary.substring(0, 100) + "...";
        }
        
        return PostListItemResponse.builder()
                .postId(post.getPostId())
                .sectionId(post.getSectionId())
                .sectionName(section != null ? section.getSectionName() : "")
                .userId(post.getUserId())
                .userName(user != null ? user.getUserName() : "")
                .userAvatar(user != null ? user.getAvatar() : "")
                .title(post.getTitle())
                .contentSummary(contentSummary)
                .firstImage(firstImage)
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .isTop(post.getIsTop())
                .isEssence(post.getIsEssence())
                .createTime(post.getCreateTime())
                .build();
    }
}
