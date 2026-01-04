package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.CommunityAnnouncement;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.AnnouncementQueryRequest;
import org.sc.smartcommunitybackend.dto.response.AnnouncementResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.CommunityAnnouncementService;
import org.sc.smartcommunitybackend.mapper.CommunityAnnouncementMapper;
import org.sc.smartcommunitybackend.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 吴展德
* @description 针对表【community_announcement(社区公告表)】的数据库操作Service实现
* @createDate 2025-12-30 10:44:15
*/
@Service
public class CommunityAnnouncementServiceImpl extends ServiceImpl<CommunityAnnouncementMapper, CommunityAnnouncement>
    implements CommunityAnnouncementService{

    private static final Logger logger = LoggerFactory.getLogger(CommunityAnnouncementServiceImpl.class);

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Page<AnnouncementResponse> queryAnnouncementList(AnnouncementQueryRequest request) {
        logger.info("查询公告列表, keyword: {}, searchScope: {}, timeRange: {}, sortType: {}, pageNum: {}, pageSize: {}", 
                request.getKeyword(), request.getSearchScope(), request.getTimeRange(), 
                request.getSortType(), request.getPageNum(), request.getPageSize());
        
        // 1. 构建查询条件
        LambdaQueryWrapper<CommunityAnnouncement> queryWrapper = new LambdaQueryWrapper<>();
        
        // 1.1 关键词搜索
        if (StringUtils.hasText(request.getKeyword())) {
            String keyword = request.getKeyword().trim();
            String searchScope = request.getSearchScope() != null ? request.getSearchScope() : "ALL";
            
            if ("TITLE".equals(searchScope)) {
                // 仅搜索标题
                queryWrapper.like(CommunityAnnouncement::getTitle, keyword);
            } else {
                // 全文搜索(标题或内容)
                queryWrapper.and(wrapper -> 
                    wrapper.like(CommunityAnnouncement::getTitle, keyword)
                           .or()
                           .like(CommunityAnnouncement::getContent, keyword)
                );
            }
        }
        
        // 1.2 时间范围筛选
        String timeRange = request.getTimeRange() != null ? request.getTimeRange() : "ALL";
        if (!"ALL".equals(timeRange)) {
            Date startTime = calculateStartTime(timeRange);
            if (startTime != null) {
                queryWrapper.ge(CommunityAnnouncement::getPublishTime, startTime);
            }
        }
        
        // 1.3 排序方式
        String sortType = request.getSortType() != null ? request.getSortType() : "TIME";
        if ("RELEVANCE".equals(sortType) && StringUtils.hasText(request.getKeyword())) {
            // 按相关度排序(标题匹配优先,然后按阅读量,最后按时间)
            // 注意: 这里简化处理,实际相关度排序可能需要使用全文检索引擎如Elasticsearch
            queryWrapper.orderByDesc(CommunityAnnouncement::getReadCount)
                       .orderByDesc(CommunityAnnouncement::getPublishTime);
        } else {
            // 按发布时间倒序排列
            queryWrapper.orderByDesc(CommunityAnnouncement::getPublishTime);
        }

        // 2. 分页查询
        Page<CommunityAnnouncement> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<CommunityAnnouncement> announcementPage = page(page, queryWrapper);

        // 3. 转换为响应DTO
        List<CommunityAnnouncement> records = announcementPage.getRecords();
        
        // 获取所有发布人ID
        List<Long> publishUserIds = records.stream()
                .map(CommunityAnnouncement::getPublishUserId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询发布人信息
        Map<Long, String> userNameMap = null;
        if (!publishUserIds.isEmpty()) {
            List<SysUser> users = sysUserService.listByIds(publishUserIds);
            userNameMap = users.stream()
                    .collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        }
        
        // 转换为响应对象
        final Map<Long, String> finalUserNameMap = userNameMap;
        List<AnnouncementResponse> responseList = records.stream()
                .map(announcement -> convertToResponse(announcement, finalUserNameMap))
                .collect(Collectors.toList());

        // 4. 构建分页响应
        Page<AnnouncementResponse> responsePage = new Page<>(
                announcementPage.getCurrent(),
                announcementPage.getSize(),
                announcementPage.getTotal()
        );
        responsePage.setRecords(responseList);

        return responsePage;
    }

    @Override
    public AnnouncementResponse getAnnouncementDetail(Long announceId) {
        // 1. 查询公告
        CommunityAnnouncement announcement = getById(announceId);
        if (announcement == null) {
            throw new BusinessException("公告不存在");
        }

        // 2. 增加阅读次数
        LambdaUpdateWrapper<CommunityAnnouncement> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CommunityAnnouncement::getAnnounceId, announceId)
                .setSql("read_count = read_count + 1");
        update(updateWrapper);
        
        // 更新内存中的阅读次数
        announcement.setReadCount(announcement.getReadCount() + 1);

        // 3. 查询发布人信息
        SysUser publishUser = sysUserService.getById(announcement.getPublishUserId());
        String publishUserName = publishUser != null ? publishUser.getUserName() : "未知";

        // 4. 转换为响应对象
        return AnnouncementResponse.builder()
                .announceId(announcement.getAnnounceId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .publishUserId(announcement.getPublishUserId())
                .publishUserName(publishUserName)
                .publishTime(announcement.getPublishTime())
                .readCount(announcement.getReadCount())
                .build();
    }

    /**
     * 转换为响应对象
     */
    private AnnouncementResponse convertToResponse(CommunityAnnouncement announcement, Map<Long, String> userNameMap) {
        String publishUserName = "未知";
        if (userNameMap != null && announcement.getPublishUserId() != null) {
            publishUserName = userNameMap.getOrDefault(announcement.getPublishUserId(), "未知");
        }

        return AnnouncementResponse.builder()
                .announceId(announcement.getAnnounceId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .publishUserId(announcement.getPublishUserId())
                .publishUserName(publishUserName)
                .publishTime(announcement.getPublishTime())
                .readCount(announcement.getReadCount())
                .build();
    }

    /**
     * 计算时间范围的起始时间
     *
     * @param timeRange 时间范围类型
     * @return 起始时间
     */
    private Date calculateStartTime(String timeRange) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        
        switch (timeRange) {
            case "WEEK":
                // 最近一周
                calendar.add(java.util.Calendar.DAY_OF_MONTH, -7);
                break;
            case "MONTH":
                // 最近一月
                calendar.add(java.util.Calendar.MONTH, -1);
                break;
            case "YEAR":
                // 最近一年
                calendar.add(java.util.Calendar.YEAR, -1);
                break;
            default:
                return null;
        }
        
        return calendar.getTime();
    }
}




