package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.CommunityAnnouncement;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.AnnouncementQueryRequest;
import org.sc.smartcommunitybackend.dto.response.AnnouncementResponse;

/**
* @author 吴展德
* @description 针对表【community_announcement(社区公告表)】的数据库操作Service
* @createDate 2025-12-30 10:44:15
*/
public interface CommunityAnnouncementService extends IService<CommunityAnnouncement> {

    /**
     * 分页查询公告列表
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Page<AnnouncementResponse> queryAnnouncementList(AnnouncementQueryRequest request);

    /**
     * 获取公告详情(并增加阅读次数)
     *
     * @param announceId 公告ID
     * @return 公告详情
     */
    AnnouncementResponse getAnnouncementDetail(Long announceId);
}
