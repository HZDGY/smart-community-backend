package org.sc.smartcommunitybackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.dto.request.UpdateUserInfoRequest;
import org.sc.smartcommunitybackend.dto.request.UserQueryRequest;
import org.sc.smartcommunitybackend.dto.response.UserDetailVO;
import org.sc.smartcommunitybackend.dto.response.UserManagementVO;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface UserManagementService {
    
    /**
     * 分页查询用户列表
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    Page<UserManagementVO> getUserList(UserQueryRequest request);
    
    /**
     * 获取用户详情
     * 
     * @param userId 用户ID
     * @return 用户详情
     */
    UserDetailVO getUserDetail(Long userId);
    
    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态（0-冻结 1-正常）
     * @return 是否成功
     */
    boolean updateUserStatus(Long userId, Integer status);
    
    /**
     * 分配角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
    
    /**
     * 更新用户信息
     * 
     * @param userId 用户ID
     * @param request 更新请求
     * @return 是否成功
     */
    boolean updateUserInfo(Long userId, UpdateUserInfoRequest request);
    
    /**
     * 删除用户（软删除）
     * 
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long userId);
}
