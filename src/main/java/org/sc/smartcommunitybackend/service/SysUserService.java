package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.*;
import org.sc.smartcommunitybackend.dto.response.UserLoginResponse;
import org.sc.smartcommunitybackend.dto.response.UserProfileResponse;
import org.sc.smartcommunitybackend.dto.response.UserRegisterResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吴展德
* @description 针对表【sys_user(系统用户表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册响应
     */
    UserRegisterResponse register(UserRegisterRequest request);

    /**
     * 用户登录（手机号+密码）
     *
     * @param request 登录请求
     * @return 登录响应(包含用户信息和token)
     */
    UserLoginResponse login(UserLoginRequest request);

    /**
     * 邮箱验证码登录
     *
     * @param request 邮箱验证码登录请求
     * @return 登录响应(包含用户信息和token)
     */
    UserLoginResponse loginByEmail(EmailLoginRequest request);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    SysUser getByPhone(String phone);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 是否更新成功
     */
    boolean updateAvatar(Long userId, String avatarUrl);

    /**
     * 忘记密码（通过邮箱验证码重置密码）
     *
     * @param request 忘记密码请求
     * @return 是否重置成功
     */
    boolean forgotPassword(ForgotPasswordRequest request);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    SysUser getByEmail(String email);

    /**
     * 修改密码（需要验证旧密码）
     *
     * @param userId 用户ID
     * @param request 修改密码请求
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, ChangePasswordRequest request);

    /**
     * 更新个人资料
     *
     * @param userId 用户ID
     * @param request 更新资料请求
     * @return 用户资料
     */
    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    /**
     * 获取用户资料
     *
     * @param userId 用户ID
     * @return 用户资料
     */
    UserProfileResponse getProfile(Long userId);

    /**
     * 用户退出登录
     *
     * @param userId 用户ID
     * @param token 当前token
     */
    void logout(Long userId, String token);
}
