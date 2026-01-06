package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.domain.ForumSection;
import org.sc.smartcommunitybackend.dto.response.SectionResponse;

import java.util.List;

/**
 * 论坛板块Service
 */
public interface ForumSectionService extends IService<ForumSection> {
    
    /**
     * 获取所有启用的板块列表
     *
     * @return 板块列表
     */
    List<SectionResponse> getActiveSections();
    
    /**
     * 增加板块帖子数量
     *
     * @param sectionId 板块ID
     */
    void incrementPostCount(Long sectionId);
    
    /**
     * 减少板块帖子数量
     *
     * @param sectionId 板块ID
     */
    void decrementPostCount(Long sectionId);
}
