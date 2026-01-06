package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.ForumSection;
import org.sc.smartcommunitybackend.dto.response.SectionResponse;
import org.sc.smartcommunitybackend.mapper.ForumSectionMapper;
import org.sc.smartcommunitybackend.service.ForumSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 论坛板块Service实现
 */
@Service
public class ForumSectionServiceImpl extends ServiceImpl<ForumSectionMapper, ForumSection>
        implements ForumSectionService {
    
    @Override
    public List<SectionResponse> getActiveSections() {
        LambdaQueryWrapper<ForumSection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumSection::getStatus, 1)
                .orderByAsc(ForumSection::getSortOrder);
        
        List<ForumSection> sections = list(wrapper);
        
        return sections.stream().map(section -> {
            SectionResponse response = new SectionResponse();
            BeanUtils.copyProperties(section, response);
            return response;
        }).collect(Collectors.toList());
    }
    
    @Override
    public void incrementPostCount(Long sectionId) {
        LambdaUpdateWrapper<ForumSection> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ForumSection::getSectionId, sectionId)
                .setSql("post_count = post_count + 1");
        update(wrapper);
    }
    
    @Override
    public void decrementPostCount(Long sectionId) {
        LambdaUpdateWrapper<ForumSection> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ForumSection::getSectionId, sectionId)
                .setSql("post_count = post_count - 1")
                .ge(ForumSection::getPostCount, 1);
        update(wrapper);
    }
}
