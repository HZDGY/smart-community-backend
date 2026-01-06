package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.PrivateMessage;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.SendMessageRequest;
import org.sc.smartcommunitybackend.dto.response.MessageResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.PrivateMessageMapper;
import org.sc.smartcommunitybackend.mapper.SysUserMapper;
import org.sc.smartcommunitybackend.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 私信Service实现
 */
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage>
        implements PrivateMessageService {
    
    @Autowired
    private PrivateMessageMapper privateMessageMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMessage(Long userId, SendMessageRequest request) {
        // 不能给自己发消息
        if (userId.equals(request.getToUserId())) {
            throw new BusinessException("不能给自己发消息");
        }
        
        // 验证接收者是否存在
        SysUser toUser = sysUserMapper.selectById(request.getToUserId());
        if (toUser == null) {
            throw new BusinessException("接收者不存在");
        }
        
        // 创建私信
        PrivateMessage message = new PrivateMessage();
        message.setFromUserId(userId);
        message.setToUserId(request.getToUserId());
        message.setContent(request.getContent());
        message.setIsRead(0);
        message.setStatus(1);
        message.setCreateTime(new Date());
        
        save(message);
        
        return message.getMessageId();
    }
    
    @Override
    public Page<MessageResponse> getChatHistory(Long userId, Long otherUserId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PrivateMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .and(w1 -> w1.eq(PrivateMessage::getFromUserId, userId).eq(PrivateMessage::getToUserId, otherUserId))
                .or(w2 -> w2.eq(PrivateMessage::getFromUserId, otherUserId).eq(PrivateMessage::getToUserId, userId))
        ).eq(PrivateMessage::getStatus, 1)
                .orderByDesc(PrivateMessage::getCreateTime);
        
        Page<PrivateMessage> page = new Page<>(pageNum, pageSize);
        Page<PrivateMessage> resultPage = page(page, wrapper);
        
        Page<MessageResponse> responsePage = new Page<>();
        responsePage.setCurrent(resultPage.getCurrent());
        responsePage.setSize(resultPage.getSize());
        responsePage.setTotal(resultPage.getTotal());
        responsePage.setPages(resultPage.getPages());
        
        List<MessageResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    public Page<MessageResponse> getConversations(Long userId, Integer pageNum, Integer pageSize) {
        // 简化实现：查询最近的消息
        LambdaQueryWrapper<PrivateMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .eq(PrivateMessage::getFromUserId, userId)
                .or()
                .eq(PrivateMessage::getToUserId, userId)
        ).eq(PrivateMessage::getStatus, 1)
                .orderByDesc(PrivateMessage::getCreateTime);
        
        Page<PrivateMessage> page = new Page<>(pageNum, pageSize);
        Page<PrivateMessage> resultPage = page(page, wrapper);
        
        Page<MessageResponse> responsePage = new Page<>();
        responsePage.setCurrent(resultPage.getCurrent());
        responsePage.setSize(resultPage.getSize());
        responsePage.setTotal(resultPage.getTotal());
        responsePage.setPages(resultPage.getPages());
        
        List<MessageResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long userId, Long messageId) {
        PrivateMessage message = getById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        
        // 只有接收者可以标记为已读
        if (!message.getToUserId().equals(userId)) {
            throw new BusinessException("无权操作此消息");
        }
        
        if (message.getIsRead() == 1) {
            return true;
        }
        
        message.setIsRead(1);
        message.setReadTime(new Date());
        
        return updateById(message);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markAllAsRead(Long userId, Long otherUserId) {
        // 先查询未读消息数量
        LambdaQueryWrapper<PrivateMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrivateMessage::getFromUserId, otherUserId)
                .eq(PrivateMessage::getToUserId, userId)
                .eq(PrivateMessage::getIsRead, 0);
        
        int count = (int) count(queryWrapper);
        
        if (count > 0) {
            // 执行更新操作
            LambdaUpdateWrapper<PrivateMessage> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PrivateMessage::getFromUserId, otherUserId)
                    .eq(PrivateMessage::getToUserId, userId)
                    .eq(PrivateMessage::getIsRead, 0)
                    .set(PrivateMessage::getIsRead, 1)
                    .set(PrivateMessage::getReadTime, new Date());
            
            update(updateWrapper);
        }
        
        return count;
    }
    
    @Override
    public int getUnreadCount(Long userId) {
        LambdaQueryWrapper<PrivateMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrivateMessage::getToUserId, userId)
                .eq(PrivateMessage::getIsRead, 0)
                .eq(PrivateMessage::getStatus, 1);
        
        return (int) count(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMessage(Long userId, Long messageId) {
        PrivateMessage message = getById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        
        // 只有发送者或接收者可以删除
        if (!message.getFromUserId().equals(userId) && !message.getToUserId().equals(userId)) {
            throw new BusinessException("无权删除此消息");
        }
        
        message.setStatus(0);
        
        return updateById(message);
    }
    
    /**
     * 转换为响应对象
     */
    private MessageResponse convertToResponse(PrivateMessage message) {
        SysUser fromUser = sysUserMapper.selectById(message.getFromUserId());
        SysUser toUser = sysUserMapper.selectById(message.getToUserId());
        
        return MessageResponse.builder()
                .messageId(message.getMessageId())
                .fromUserId(message.getFromUserId())
                .fromUserName(fromUser != null ? fromUser.getUserName() : "")
                .fromUserAvatar(fromUser != null ? fromUser.getAvatar() : "")
                .toUserId(message.getToUserId())
                .toUserName(toUser != null ? toUser.getUserName() : "")
                .toUserAvatar(toUser != null ? toUser.getAvatar() : "")
                .content(message.getContent())
                .isRead(message.getIsRead())
                .createTime(message.getCreateTime())
                .readTime(message.getReadTime())
                .build();
    }
}
