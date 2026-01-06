package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.ForumPostLike;

/**
 * 帖子点赞Mapper
 */
@Mapper
public interface ForumPostLikeMapper extends BaseMapper<ForumPostLike> {
}
