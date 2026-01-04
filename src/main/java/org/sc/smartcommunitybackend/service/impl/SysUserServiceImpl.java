package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.constant.UserConstant;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.*;
import org.sc.smartcommunitybackend.dto.response.UserLoginResponse;
import org.sc.smartcommunitybackend.dto.response.UserProfileResponse;
import org.sc.smartcommunitybackend.dto.response.UserRegisterResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.SysUserService;
import org.sc.smartcommunitybackend.mapper.SysUserMapper;
import org.sc.smartcommunitybackend.service.VerifyCodeService;
import org.sc.smartcommunitybackend.util.EmailUtil;
import org.sc.smartcommunitybackend.util.JwtUtil;
import org.sc.smartcommunitybackend.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author 吴展德
* @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResponse register(UserRegisterRequest request) {
        // 1. 验证两次密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 2. 检查手机号是否已注册
        SysUser existUser = getByPhone(request.getPhone());
        if (existUser != null) {
            throw new BusinessException("该手机号已被注册");
        }

        // 3. 创建用户对象
        SysUser user = new SysUser();
        user.setUserName(request.getUserName());
        user.setPhone(request.getPhone());
        user.setPassword(PasswordUtil.encrypt(request.getPassword())); // 密码加密
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setUserType(UserConstant.USER_TYPE_NORMAL); // 默认为普通用户
        user.setStatus(UserConstant.STATUS_NORMAL); // 默认状态正常
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 4. 保存用户
        boolean saved = save(user);
        if (!saved) {
            throw new BusinessException("注册失败，请稍后重试");
        }

        // 5. 构建响应对象
        UserRegisterResponse response = new UserRegisterResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setPhone(user.getPhone());
        response.setGender(user.getGender());
        response.setAge(user.getAge());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());

        return response;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        // 1. 根据手机号查询用户
        SysUser user = getByPhone(request.getPhone());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 验证密码
        String encryptedPassword = PasswordUtil.encrypt(request.getPassword());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() != UserConstant.STATUS_NORMAL) {
            throw new BusinessException("账号已被冻结，请联系管理员");
        }

        // 4. 生成JWT Token
        String token = jwtUtil.generateToken(user.getUserId(), user.getPhone());

        // 5. 构建响应对象
        UserLoginResponse response = UserLoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .age(user.getAge())
                .userType(user.getUserType())
                .status(user.getStatus())
                .build();

        return response;
    }

    @Override
    public UserLoginResponse loginByEmail(EmailLoginRequest request) {
        // 1. 验证邮箱验证码
        boolean valid = verifyCodeService.verifyEmailCode(request.getEmail(), request.getVerifyCode());
        if (!valid) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 2. 根据邮箱查询用户
        SysUser user = getByEmail(request.getEmail());
        if (user == null) {
            throw new BusinessException("该邮箱未注册");
        }

        // 3. 检查用户状态
        if (user.getStatus() != UserConstant.STATUS_NORMAL) {
            throw new BusinessException("账号已被冻结，请联系管理员");
        }

        // 4. 生成JWT Token
        String token = jwtUtil.generateToken(user.getUserId(), user.getPhone());

        // 5. 构建响应对象
        UserLoginResponse response = UserLoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .age(user.getAge())
                .userType(user.getUserType())
                .status(user.getStatus())
                .build();

        logger.info("用户邮箱验证码登录成功: userId={}, email={}", user.getUserId(), user.getEmail());
        return response;
    }

    @Override
    public SysUser getByPhone(String phone) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhone, phone);
        return getOne(queryWrapper);
    }

    @Override
    public boolean updateAvatar(Long userId, String avatarUrl) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setAvatar(avatarUrl);
        user.setUpdateTime(new Date());
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean forgotPassword(ForgotPasswordRequest request) {
        // 1. 验证两次密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 2. 验证邮箱验证码
        boolean codeValid = verifyCodeService.verifyEmailCode(request.getEmail(), request.getVerifyCode());
        if (!codeValid) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 3. 查询用户是否存在
        SysUser user = getByEmail(request.getEmail());
        if (user == null) {
            throw new BusinessException("该邮箱未注册");
        }

        // 4. 更新密码
        user.setPassword(PasswordUtil.encrypt(request.getNewPassword()));
        user.setUpdateTime(new Date());
        boolean updated = updateById(user);

        // 5. 发送密码重置成功通知邮件
        if (updated) {
            try {
                emailUtil.sendPasswordResetNotification(request.getEmail());
            } catch (Exception e) {
                // 通知邮件发送失败不影响密码重置
                logger.error("发送密码重置通知邮件失败", e);
            }
        }

        return updated;
    }

    @Override
    public SysUser getByEmail(String email) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getEmail, email);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, ChangePasswordRequest request) {
        // 1. 验证两次密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 2. 查询用户
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 3. 验证旧密码
        String oldPasswordEncrypted = PasswordUtil.encrypt(request.getOldPassword());
        if (!oldPasswordEncrypted.equals(user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 4. 验证新密码不能与旧密码相同
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BusinessException("新密码不能与旧密码相同");
        }

        // 5. 更新密码
        user.setPassword(PasswordUtil.encrypt(request.getNewPassword()));
        user.setUpdateTime(new Date());
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        // 1. 查询用户
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 更新用户信息（只更新非空字段）
        boolean updated = false;
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            user.setUserName(request.getUserName());
            updated = true;
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
            updated = true;
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
            updated = true;
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
            updated = true;
        }

        if (updated) {
            user.setUpdateTime(new Date());
            if (!updateById(user)) {
                throw new BusinessException("更新个人资料失败");
            }
        }

        // 3. 返回更新后的用户资料
        return getProfile(userId);
    }

    @Override
    public UserProfileResponse getProfile(Long userId) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .age(user.getAge())
                .userType(user.getUserType())
                .status(user.getStatus())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build();
    }

    @Override
    public void logout(Long userId, String token) {
        // 记录退出日志
        logger.info("用户退出登录: userId={}", userId);
        
        // TODO: 如果使用Redis存储token黑名单，可以在这里将token加入黑名单
        // 例如: redisTemplate.opsForValue().set("blacklist:" + token, "1", jwtUtil.getExpiration(), TimeUnit.MILLISECONDS);
        
        // 当前实现：客户端删除token即可
        // 服务端可以选择将token加入黑名单（需要Redis支持）
    }
}




