package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.UserNotification;
import org.sc.smartcommunitybackend.dto.response.NotificationResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.UserNotificationMapper;
import org.sc.smartcommunitybackend.service.UserNotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户通知Service实现
 */
@Service
public class UserNotificationServiceImpl extends ServiceImpl<UserNotificationMapper, UserNotification>
        implements UserNotificationService {
    
    @Autowired
    private UserNotificationMapper userNotificationMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNotification(Long userId, Integer type, String title, String content, Long relatedId) {
        UserNotification notification = new UserNotification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notification.setCreateTime(new Date());
        
        save(notification);
        
        return notification.getNotificationId();
    }
    
    @Override
    public Page<NotificationResponse> getNotifications(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<UserNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserNotification::getUserId, userId)
                .orderByDesc(UserNotification::getCreateTime);
        
        Page<UserNotification> page = new Page<>(pageNum, pageSize);
        Page<UserNotification> resultPage = page(page, wrapper);
        
        Page<NotificationResponse> responsePage = new Page<>();
        responsePage.setCurrent(resultPage.getCurrent());
        responsePage.setSize(resultPage.getSize());
        responsePage.setTotal(resultPage.getTotal());
        responsePage.setPages(resultPage.getPages());
        
        List<NotificationResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long userId, Long notificationId) {
        UserNotification notification = getById(notificationId);
        if (notification == null) {
            throw new BusinessException("通知不存在");
        }
        
        // 验证是否是当前用户的通知
        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此通知");
        }
        
        if (notification.getIsRead() == 1) {
            return true;
        }
        
        notification.setIsRead(1);
        
        return updateById(notification);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markAllAsRead(Long userId) {
        // 先查询未读通知数量
        LambdaQueryWrapper<UserNotification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNotification::getUserId, userId)
                .eq(UserNotification::getIsRead, 0);
        
        int count = (int) count(queryWrapper);
        
        if (count > 0) {
            // 执行更新操作
            LambdaUpdateWrapper<UserNotification> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserNotification::getUserId, userId)
                    .eq(UserNotification::getIsRead, 0)
                    .set(UserNotification::getIsRead, 1);
            
            update(updateWrapper);
        }
        
        return count;
    }
    
    @Override
    public int getUnreadCount(Long userId) {
        LambdaQueryWrapper<UserNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserNotification::getUserId, userId)
                .eq(UserNotification::getIsRead, 0);
        
        return (int) count(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotification(Long userId, Long notificationId) {
        UserNotification notification = getById(notificationId);
        if (notification == null) {
            throw new BusinessException("通知不存在");
        }
        
        // 验证是否是当前用户的通知
        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此通知");
        }
        
        return removeById(notificationId);
    }
    
    /**
     * 转换为响应对象
     */
    private NotificationResponse convertToResponse(UserNotification notification) {
        NotificationResponse response = new NotificationResponse();
        BeanUtils.copyProperties(notification, response);
        return response;
    }
}
