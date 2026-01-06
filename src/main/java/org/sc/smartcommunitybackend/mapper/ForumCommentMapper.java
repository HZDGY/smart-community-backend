package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.sc.smartcommunitybackend.domain.ForumComment;

/**
 * 论坛评论Mapper
 */
@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {
    
    /**
     * 增加点赞数
     */
    @Update("UPDATE forum_comment SET like_count = like_count + 1 WHERE comment_id = #{commentId}")
    int incrementLikeCount(@Param("commentId") Long commentId);
    
    /**
     * 减少点赞数
     */
    @Update("UPDATE forum_comment SET like_count = like_count - 1 WHERE comment_id = #{commentId} AND like_count > 0")
    int decrementLikeCount(@Param("commentId") Long commentId);
}
