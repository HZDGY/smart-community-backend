package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.ForumCommentLike;

/**
 * 评论点赞Mapper
 */
@Mapper
public interface ForumCommentLikeMapper extends BaseMapper<ForumCommentLike> {
}
