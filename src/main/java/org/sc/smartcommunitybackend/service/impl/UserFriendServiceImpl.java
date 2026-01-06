package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.domain.UserFriend;
import org.sc.smartcommunitybackend.dto.request.AddFriendRequest;
import org.sc.smartcommunitybackend.dto.response.FriendResponse;
import org.sc.smartcommunitybackend.enums.FriendStatus;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.SysUserMapper;
import org.sc.smartcommunitybackend.mapper.UserFriendMapper;
import org.sc.smartcommunitybackend.service.UserFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户好友Service实现
 */
@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend>
        implements UserFriendService {
    
    @Autowired
    private UserFriendMapper userFriendMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendFriendRequest(Long userId, AddFriendRequest request) {
        // 不能添加自己为好友
        if (userId.equals(request.getFriendUserId())) {
            throw new BusinessException("不能添加自己为好友");
        }
        
        // 验证对方用户是否存在
        SysUser friendUser = sysUserMapper.selectById(request.getFriendUserId());
        if (friendUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查是否已经是好友或已发送申请
        LambdaQueryWrapper<UserFriend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFriend::getUserId, userId)
                .eq(UserFriend::getFriendUserId, request.getFriendUserId());
        
        UserFriend existing = getOne(wrapper);
        if (existing != null) {
            if (existing.getStatus().equals(FriendStatus.ACCEPTED.getCode())) {
                throw new BusinessException("已经是好友了");
            } else if (existing.getStatus().equals(FriendStatus.PENDING.getCode())) {
                throw new BusinessException("已发送好友申请，请等待对方同意");
            }
        }
        
        // 创建好友申请
        UserFriend friendRequest = new UserFriend();
        friendRequest.setUserId(userId);
        friendRequest.setFriendUserId(request.getFriendUserId());
        friendRequest.setRemark(request.getRemark());
        friendRequest.setStatus(FriendStatus.PENDING.getCode());
        friendRequest.setCreateTime(new Date());
        friendRequest.setUpdateTime(new Date());
        
        return save(friendRequest);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptFriendRequest(Long userId, Long friendId) {
        UserFriend friendRequest = getById(friendId);
        if (friendRequest == null) {
            throw new BusinessException("好友申请不存在");
        }
        
        // 验证是否是发给当前用户的申请
        if (!friendRequest.getFriendUserId().equals(userId)) {
            throw new BusinessException("无权操作此好友申请");
        }
        
        if (!friendRequest.getStatus().equals(FriendStatus.PENDING.getCode())) {
            throw new BusinessException("好友申请已处理");
        }
        
        // 更新申请状态
        friendRequest.setStatus(FriendStatus.ACCEPTED.getCode());
        friendRequest.setUpdateTime(new Date());
        updateById(friendRequest);
        
        // 创建反向好友关系
        UserFriend reverseFriend = new UserFriend();
        reverseFriend.setUserId(userId);
        reverseFriend.setFriendUserId(friendRequest.getUserId());
        reverseFriend.setStatus(FriendStatus.ACCEPTED.getCode());
        reverseFriend.setCreateTime(new Date());
        reverseFriend.setUpdateTime(new Date());
        save(reverseFriend);
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectFriendRequest(Long userId, Long friendId) {
        UserFriend friendRequest = getById(friendId);
        if (friendRequest == null) {
            throw new BusinessException("好友申请不存在");
        }
        
        // 验证是否是发给当前用户的申请
        if (!friendRequest.getFriendUserId().equals(userId)) {
            throw new BusinessException("无权操作此好友申请");
        }
        
        if (!friendRequest.getStatus().equals(FriendStatus.PENDING.getCode())) {
            throw new BusinessException("好友申请已处理");
        }
        
        // 更新申请状态
        friendRequest.setStatus(FriendStatus.REJECTED.getCode());
        friendRequest.setUpdateTime(new Date());
        
        return updateById(friendRequest);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFriend(Long userId, Long friendUserId) {
        // 删除双向好友关系
        LambdaQueryWrapper<UserFriend> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(UserFriend::getUserId, userId)
                .eq(UserFriend::getFriendUserId, friendUserId)
                .eq(UserFriend::getStatus, FriendStatus.ACCEPTED.getCode());
        
        LambdaQueryWrapper<UserFriend> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(UserFriend::getUserId, friendUserId)
                .eq(UserFriend::getFriendUserId, userId)
                .eq(UserFriend::getStatus, FriendStatus.ACCEPTED.getCode());
        
        remove(wrapper1);
        remove(wrapper2);
        
        return true;
    }
    
    @Override
    public Page<FriendResponse> getFriendList(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<UserFriend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFriend::getUserId, userId)
                .eq(UserFriend::getStatus, FriendStatus.ACCEPTED.getCode())
                .orderByDesc(UserFriend::getCreateTime);
        
        Page<UserFriend> page = new Page<>(pageNum, pageSize);
        Page<UserFriend> resultPage = page(page, wrapper);
        
        Page<FriendResponse> responsePage = new Page<>();
        responsePage.setCurrent(resultPage.getCurrent());
        responsePage.setSize(resultPage.getSize());
        responsePage.setTotal(resultPage.getTotal());
        responsePage.setPages(resultPage.getPages());
        
        List<FriendResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    public Page<FriendResponse> getFriendRequests(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<UserFriend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFriend::getFriendUserId, userId)
                .eq(UserFriend::getStatus, FriendStatus.PENDING.getCode())
                .orderByDesc(UserFriend::getCreateTime);
        
        Page<UserFriend> page = new Page<>(pageNum, pageSize);
        Page<UserFriend> resultPage = page(page, wrapper);
        
        Page<FriendResponse> responsePage = new Page<>();
        responsePage.setCurrent(resultPage.getCurrent());
        responsePage.setSize(resultPage.getSize());
        responsePage.setTotal(resultPage.getTotal());
        responsePage.setPages(resultPage.getPages());
        
        List<FriendResponse> responseList = resultPage.getRecords().stream()
                .map(friend -> {
                    FriendResponse response = convertToResponse(friend);
                    // 对于好友申请，显示的是申请人的信息
                    SysUser user = sysUserMapper.selectById(friend.getUserId());
                    if (user != null) {
                        response.setFriendUserId(user.getUserId());
                        response.setFriendUserName(user.getUserName());
                        response.setFriendAvatar(user.getAvatar());
                    }
                    return response;
                })
                .collect(Collectors.toList());
        
        responsePage.setRecords(responseList);
        
        return responsePage;
    }
    
    @Override
    public boolean isFriend(Long userId, Long friendUserId) {
        LambdaQueryWrapper<UserFriend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFriend::getUserId, userId)
                .eq(UserFriend::getFriendUserId, friendUserId)
                .eq(UserFriend::getStatus, FriendStatus.ACCEPTED.getCode());
        
        return count(wrapper) > 0;
    }
    
    /**
     * 转换为响应对象
     */
    private FriendResponse convertToResponse(UserFriend friend) {
        SysUser friendUser = sysUserMapper.selectById(friend.getFriendUserId());
        
        return FriendResponse.builder()
                .friendId(friend.getFriendId())
                .friendUserId(friend.getFriendUserId())
                .friendUserName(friendUser != null ? friendUser.getUserName() : "")
                .friendAvatar(friendUser != null ? friendUser.getAvatar() : "")
                .remark(friend.getRemark())
                .status(friend.getStatus())
                .createTime(friend.getCreateTime())
                .build();
    }
}
