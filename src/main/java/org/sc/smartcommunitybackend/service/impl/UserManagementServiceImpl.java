package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.sc.smartcommunitybackend.domain.SysRole;
import org.sc.smartcommunitybackend.domain.SysUser;
import org.sc.smartcommunitybackend.domain.SysUserRole;
import org.sc.smartcommunitybackend.domain.UserWallet;
import org.sc.smartcommunitybackend.dto.request.UpdateUserInfoRequest;
import org.sc.smartcommunitybackend.dto.request.UserQueryRequest;
import org.sc.smartcommunitybackend.dto.response.UserDetailVO;
import org.sc.smartcommunitybackend.dto.response.UserManagementVO;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.SysRoleMapper;
import org.sc.smartcommunitybackend.mapper.SysUserMapper;
import org.sc.smartcommunitybackend.mapper.SysUserRoleMapper;
import org.sc.smartcommunitybackend.mapper.UserWalletMapper;
import org.sc.smartcommunitybackend.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现类
 */
@Service
public class UserManagementServiceImpl implements UserManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    
    @Autowired
    private SysRoleMapper roleMapper;
    
    @Autowired
    private UserWalletMapper walletMapper;
    
    @Override
    public Page<UserManagementVO> getUserList(UserQueryRequest request) {
        Page<SysUser> page = new Page<>(request.getPage(), request.getSize());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                .like(SysUser::getUserName, request.getKeyword())
                .or().like(SysUser::getPhone, request.getKeyword())
                .or().like(SysUser::getEmail, request.getKeyword())
            );
        }
        
        // 状态筛选
        if (request.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, request.getStatus());
        }
        
        // 用户类型筛选
        if (request.getUserType() != null) {
            wrapper.eq(SysUser::getUserType, request.getUserType());
        }
        
        wrapper.orderByDesc(SysUser::getCreateTime);
        
        Page<SysUser> userPage = userMapper.selectPage(page, wrapper);
        
        // 转换为 VO
        Page<UserManagementVO> voPage = new Page<>();
        voPage.setCurrent(userPage.getCurrent());
        voPage.setSize(userPage.getSize());
        voPage.setTotal(userPage.getTotal());
        
        List<UserManagementVO> voList = userPage.getRecords().stream().map(user -> {
            UserManagementVO vo = new UserManagementVO();
            BeanUtils.copyProperties(user, vo);
            
            // 查询用户角色
            List<String> roles = getUserRoleCodes(user.getUserId());
            vo.setRoles(roles);
            
            return vo;
        }).collect(Collectors.toList());
        
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    public UserDetailVO getUserDetail(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        UserDetailVO vo = new UserDetailVO();
        BeanUtils.copyProperties(user, vo);
        
        // 查询角色信息
        List<UserDetailVO.RoleInfo> roles = getUserRoles(userId);
        vo.setRoles(roles);
        
        // 查询钱包信息
        UserDetailVO.WalletInfo walletInfo = getUserWallet(userId);
        vo.setWallet(walletInfo);
        
        return vo;
    }
    
    @Override
    @Transactional
    public boolean updateUserStatus(Long userId, Integer status) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查是否是超级管理员
        if (isSuperAdmin(userId)) {
            throw new BusinessException("不能冻结超级管理员账号");
        }
        
        user.setStatus(status);
        user.setUpdateTime(new Date());
        
        int result = userMapper.updateById(user);
        
        logger.info("更新用户 {} 状态为 {}", userId, status);
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 删除现有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);
        
        // 添加新角色
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
        
        logger.info("为用户 {} 分配角色：{}", userId, roleIds);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        
        user.setUpdateTime(new Date());
        
        int result = userMapper.updateById(user);
        
        logger.info("更新用户 {} 信息", userId);
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查是否是超级管理员
        if (isSuperAdmin(userId)) {
            throw new BusinessException("不能删除超级管理员账号");
        }
        
        // 软删除：设置状态为冻结
        user.setStatus(0);
        user.setUpdateTime(new Date());
        
        int result = userMapper.updateById(user);
        
        logger.info("删除用户 {}", userId);
        
        return result > 0;
    }
    
    /**
     * 获取用户角色代码列表
     */
    private List<String> getUserRoleCodes(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);
        
        List<String> roleCodes = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleMapper.selectById(userRole.getRoleId());
            if (role != null) {
                roleCodes.add(role.getRoleCode());
            }
        }
        
        return roleCodes;
    }
    
    /**
     * 获取用户角色详细信息
     */
    private List<UserDetailVO.RoleInfo> getUserRoles(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);
        
        List<UserDetailVO.RoleInfo> roles = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleMapper.selectById(userRole.getRoleId());
            if (role != null) {
                UserDetailVO.RoleInfo roleInfo = new UserDetailVO.RoleInfo();
                roleInfo.setRoleId(role.getRoleId());
                roleInfo.setRoleName(role.getRoleName());
                roleInfo.setRoleCode(role.getRoleCode());
                roles.add(roleInfo);
            }
        }
        
        return roles;
    }
    
    /**
     * 获取用户钱包信息
     */
    private UserDetailVO.WalletInfo getUserWallet(Long userId) {
        LambdaQueryWrapper<UserWallet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserWallet::getUserId, userId);
        UserWallet wallet = walletMapper.selectOne(wrapper);
        
        UserDetailVO.WalletInfo walletInfo = new UserDetailVO.WalletInfo();
        if (wallet != null) {
            walletInfo.setBalance(wallet.getBalance());
            walletInfo.setTotalRecharge(wallet.getTotalRecharge());
            walletInfo.setTotalExpense(wallet.getTotalExpense());
        }
        
        return walletInfo;
    }
    
    /**
     * 检查是否是超级管理员
     */
    private boolean isSuperAdmin(Long userId) {
        List<String> roles = getUserRoleCodes(userId);
        return roles.contains("ROLE_SUPER_ADMIN");
    }
}
