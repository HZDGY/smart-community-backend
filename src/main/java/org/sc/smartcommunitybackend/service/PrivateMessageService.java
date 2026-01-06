package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.domain.PrivateMessage;
import org.sc.smartcommunitybackend.dto.request.SendMessageRequest;
import org.sc.smartcommunitybackend.dto.response.MessageResponse;

/**
 * 私信Service
 */
public interface PrivateMessageService extends IService<PrivateMessage> {
    
    /**
     * 发送私信
     *
     * @param userId 发送者ID
     * @param request 发送私信请求
     * @return 消息ID
     */
    Long sendMessage(Long userId, SendMessageRequest request);
    
    /**
     * 获取与某人的聊天记录
     *
     * @param userId 当前用户ID
     * @param otherUserId 对方用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 聊天记录
     */
    Page<MessageResponse> getChatHistory(Long userId, Long otherUserId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取会话列表（最近联系人）
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 会话列表
     */
    Page<MessageResponse> getConversations(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 标记消息为已读
     *
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean markAsRead(Long userId, Long messageId);
    
    /**
     * 标记与某人的所有消息为已读
     *
     * @param userId 当前用户ID
     * @param otherUserId 对方用户ID
     * @return 已读数量
     */
    int markAllAsRead(Long userId, Long otherUserId);
    
    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    int getUnreadCount(Long userId);
    
    /**
     * 删除消息
     *
     * @param userId 用户ID
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean deleteMessage(Long userId, Long messageId);
}
