package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.dto.request.UserRegisterRequest;
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
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    SysUser getByPhone(String phone);
}
