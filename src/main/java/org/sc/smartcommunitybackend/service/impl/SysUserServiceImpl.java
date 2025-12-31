package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.constant.UserConstant;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.UserLoginRequest;
import org.sc.smartcommunitybackend.dto.request.UserRegisterRequest;
import org.sc.smartcommunitybackend.dto.response.UserLoginResponse;
import org.sc.smartcommunitybackend.dto.response.UserRegisterResponse;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.SysUserService;
import org.sc.smartcommunitybackend.mapper.SysUserMapper;
import org.sc.smartcommunitybackend.util.JwtUtil;
import org.sc.smartcommunitybackend.util.PasswordUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

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
        user.setUser_name(request.getUserName());
        user.setPhone(request.getPhone());
        user.setPassword(PasswordUtil.encrypt(request.getPassword())); // 密码加密
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setUser_type(UserConstant.USER_TYPE_NORMAL); // 默认为普通用户
        user.setStatus(UserConstant.STATUS_NORMAL); // 默认状态正常
        user.setCreate_time(new Date());
        user.setUpdate_time(new Date());

        // 4. 保存用户
        boolean saved = save(user);
        if (!saved) {
            throw new BusinessException("注册失败，请稍后重试");
        }

        // 5. 构建响应对象
        UserRegisterResponse response = new UserRegisterResponse();
        response.setUserId(user.getUser_id());
        response.setUserName(user.getUser_name());
        response.setPhone(user.getPhone());
        response.setGender(user.getGender());
        response.setAge(user.getAge());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUser_type());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreate_time());

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
        String token = jwtUtil.generateToken(user.getUser_id(), user.getPhone());

        // 5. 构建响应对象
        UserLoginResponse response = UserLoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUser_id())
                .userName(user.getUser_name())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .age(user.getAge())
                .userType(user.getUser_type())
                .status(user.getStatus())
                .build();

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
        user.setUpdate_time(new Date());
        return updateById(user);
    }
}




