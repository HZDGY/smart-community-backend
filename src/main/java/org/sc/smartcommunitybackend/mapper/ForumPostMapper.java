package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.sc.smartcommunitybackend.domain.ForumPost;

/**
 * 论坛帖子Mapper
 */
@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
    
    /**
     * 增加浏览次数
     */
    @Update("UPDATE forum_post SET view_count = view_count + 1 WHERE post_id = #{postId}")
    int incrementViewCount(@Param("postId") Long postId);
    
    /**
     * 增加点赞数
     */
    @Update("UPDATE forum_post SET like_count = like_count + 1 WHERE post_id = #{postId}")
    int incrementLikeCount(@Param("postId") Long postId);
    
    /**
     * 减少点赞数
     */
    @Update("UPDATE forum_post SET like_count = like_count - 1 WHERE post_id = #{postId} AND like_count > 0")
    int decrementLikeCount(@Param("postId") Long postId);
    
    /**
     * 增加评论数
     */
    @Update("UPDATE forum_post SET comment_count = comment_count + 1 WHERE post_id = #{postId}")
    int incrementCommentCount(@Param("postId") Long postId);
    
    /**
     * 减少评论数
     */
    @Update("UPDATE forum_post SET comment_count = comment_count - 1 WHERE post_id = #{postId} AND comment_count > 0")
    int decrementCommentCount(@Param("postId") Long postId);
    
    /**
     * 增加收藏数
     */
    @Update("UPDATE forum_post SET collect_count = collect_count + 1 WHERE post_id = #{postId}")
    int incrementCollectCount(@Param("postId") Long postId);
    
    /**
     * 减少收藏数
     */
    @Update("UPDATE forum_post SET collect_count = collect_count - 1 WHERE post_id = #{postId} AND collect_count > 0")
    int decrementCollectCount(@Param("postId") Long postId);
}
