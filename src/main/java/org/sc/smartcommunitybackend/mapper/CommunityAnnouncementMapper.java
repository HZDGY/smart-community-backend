package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.CommunityAnnouncement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【community_announcement(社区公告表)】的数据库操作Mapper
* @createDate 2025-12-30 10:44:15
* @Entity generator.domain.CommunityAnnouncement
*/
@Mapper
public interface CommunityAnnouncementMapper extends BaseMapper<CommunityAnnouncement> {

}




