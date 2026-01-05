package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.sc.smartcommunitybackend.domain.*;
import org.sc.smartcommunitybackend.mapper.*;
import org.sc.smartcommunitybackend.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    
    @Autowired
    private SysRoleMapper roleMapper;
    
    @Autowired
    private SysPermissionMapper permissionMapper;
    
    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;
    
    @Override
    public Set<String> getUserPermissions(Long userId) {
        List<String> permissions = userRoleMapper.selectPermissionCodesByUserId(userId);
        return new HashSet<>(permissions);
    }
    
    @Override
    public Set<String> getUserRoles(Long userId) {
        List<String> roles = userRoleMapper.selectRoleCodesByUserId(userId);
        return new HashSet<>(roles);
    }
    
    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        Set<String> permissions = getUserPermissions(userId);
        return permissions.contains(permissionCode);
    }
    
    @Override
    public boolean hasAllPermissions(Long userId, String[] permissionCodes) {
        Set<String> userPermissions = getUserPermissions(userId);
        return Arrays.stream(permissionCodes).allMatch(userPermissions::contains);
    }
    
    @Override
    public boolean hasAnyPermission(Long userId, String[] permissionCodes) {
        Set<String> userPermissions = getUserPermissions(userId);
        return Arrays.stream(permissionCodes).anyMatch(userPermissions::contains);
    }
    
    @Override
    public boolean hasRole(Long userId, String roleCode) {
        Set<String> roles = getUserRoles(userId);
        return roles.contains(roleCode);
    }
    
    @Override
    public boolean hasAllRoles(Long userId, String[] roleCodes) {
        Set<String> userRoles = getUserRoles(userId);
        return Arrays.stream(roleCodes).allMatch(userRoles::contains);
    }
    
    @Override
    public boolean hasAnyRole(Long userId, String[] roleCodes) {
        Set<String> userRoles = getUserRoles(userId);
        return Arrays.stream(roleCodes).anyMatch(userRoles::contains);
    }
    
    @Override
    public List<SysPermission> getAllPermissions() {
        return permissionMapper.selectList(null);
    }
    
    @Override
    public List<SysRole> getAllRoles() {
        return roleMapper.selectList(null);
    }
    
    @Override
    @Transactional
    public boolean createPermission(SysPermission permission) {
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        return permissionMapper.insert(permission) > 0;
    }
    
    @Override
    @Transactional
    public boolean createRole(SysRole role) {
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        return roleMapper.insert(role) > 0;
    }
    
    @Override
    @Transactional
    public boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // 先删除该角色的所有权限
        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);
        
        // 重新分配权限
        for (Long permissionId : permissionIds) {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
        
        logger.info("为角色 {} 分配了 {} 个权限", roleId, permissionIds.size());
        return true;
    }
    
    @Override
    @Transactional
    public boolean assignRolesToUser(Long userId, List<Long> roleIds) {
        // 先删除该用户的所有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);
        
        // 重新分配角色
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
        
        logger.info("为用户 {} 分配了 {} 个角色", userId, roleIds.size());
        return true;
    }
    
    @Override
    public List<SysRole> getUserRoleList(Long userId) {
        // 查询用户的角色ID列表
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);
        
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查询角色详情
        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        
        return roleMapper.selectBatchIds(roleIds);
    }
    
    @Override
    public List<SysPermission> getUserPermissionList(Long userId) {
        // 查询用户的所有权限编码
        List<String> permissionCodes = userRoleMapper.selectPermissionCodesByUserId(userId);
        
        if (permissionCodes.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查询权限详情
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysPermission::getPermissionCode, permissionCodes);
        
        return permissionMapper.selectList(wrapper);
    }
}
