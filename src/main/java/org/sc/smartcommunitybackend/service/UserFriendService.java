package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.domain.UserFriend;
import org.sc.smartcommunitybackend.dto.request.AddFriendRequest;
import org.sc.smartcommunitybackend.dto.response.FriendResponse;

/**
 * 用户好友Service
 */
public interface UserFriendService extends IService<UserFriend> {
    
    /**
     * 发送好友申请
     *
     * @param userId 用户ID
     * @param request 添加好友请求
     * @return 是否成功
     */
    boolean sendFriendRequest(Long userId, AddFriendRequest request);
    
    /**
     * 接受好友申请
     *
     * @param userId 用户ID
     * @param friendId 好友关系ID
     * @return 是否成功
     */
    boolean acceptFriendRequest(Long userId, Long friendId);
    
    /**
     * 拒绝好友申请
     *
     * @param userId 用户ID
     * @param friendId 好友关系ID
     * @return 是否成功
     */
    boolean rejectFriendRequest(Long userId, Long friendId);
    
    /**
     * 删除好友
     *
     * @param userId 用户ID
     * @param friendUserId 好友用户ID
     * @return 是否成功
     */
    boolean deleteFriend(Long userId, Long friendUserId);
    
    /**
     * 获取好友列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 好友列表
     */
    Page<FriendResponse> getFriendList(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取好友申请列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 好友申请列表
     */
    Page<FriendResponse> getFriendRequests(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 检查是否是好友关系
     *
     * @param userId 用户ID
     * @param friendUserId 好友用户ID
     * @return 是否是好友
     */
    boolean isFriend(Long userId, Long friendUserId);
}
