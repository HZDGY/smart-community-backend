package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.ForumPostCollect;

/**
 * 帖子收藏Mapper
 */
@Mapper
public interface ForumPostCollectMapper extends BaseMapper<ForumPostCollect> {
}
