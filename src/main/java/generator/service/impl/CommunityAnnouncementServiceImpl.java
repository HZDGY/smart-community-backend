package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.CommunityAnnouncement;
import generator.service.CommunityAnnouncementService;
import generator.mapper.CommunityAnnouncementMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【community_announcement(社区公告表)】的数据库操作Service实现
* @createDate 2025-12-30 10:44:15
*/
@Service
public class CommunityAnnouncementServiceImpl extends ServiceImpl<CommunityAnnouncementMapper, CommunityAnnouncement>
    implements CommunityAnnouncementService{

}




