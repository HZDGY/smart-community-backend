package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.domain.UserNotification;
import org.sc.smartcommunitybackend.dto.response.NotificationResponse;

/**
 * 用户通知Service
 */
public interface UserNotificationService extends IService<UserNotification> {
    
    /**
     * 创建通知
     *
     * @param userId 用户ID
     * @param type 通知类型
     * @param title 通知标题
     * @param content 通知内容
     * @param relatedId 关联ID
     * @return 通知ID
     */
    Long createNotification(Long userId, Integer type, String title, String content, Long relatedId);
    
    /**
     * 获取通知列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 通知列表
     */
    Page<NotificationResponse> getNotifications(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 标记通知为已读
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long userId, Long notificationId);
    
    /**
     * 标记所有通知为已读
     *
     * @param userId 用户ID
     * @return 已读数量
     */
    int markAllAsRead(Long userId);
    
    /**
     * 获取未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    int getUnreadCount(Long userId);
    
    /**
     * 删除通知
     *
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean deleteNotification(Long userId, Long notificationId);
}
