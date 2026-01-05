package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.SysPermission;
import org.sc.smartcommunitybackend.domain.SysRole;

import java.util.List;
import java.util.Set;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    /**
     * 获取用户的所有权限编码
     */
    Set<String> getUserPermissions(Long userId);
    
    /**
     * 获取用户的所有角色编码
     */
    Set<String> getUserRoles(Long userId);
    
    /**
     * 检查用户是否拥有指定权限
     */
    boolean hasPermission(Long userId, String permissionCode);
    
    /**
     * 检查用户是否拥有所有指定权限（AND）
     */
    boolean hasAllPermissions(Long userId, String[] permissionCodes);
    
    /**
     * 检查用户是否拥有任意一个指定权限（OR）
     */
    boolean hasAnyPermission(Long userId, String[] permissionCodes);
    
    /**
     * 检查用户是否拥有指定角色
     */
    boolean hasRole(Long userId, String roleCode);
    
    /**
     * 检查用户是否拥有所有指定角色（AND）
     */
    boolean hasAllRoles(Long userId, String[] roleCodes);
    
    /**
     * 检查用户是否拥有任意一个指定角色（OR）
     */
    boolean hasAnyRole(Long userId, String[] roleCodes);
    
    /**
     * 获取所有权限
     */
    List<SysPermission> getAllPermissions();
    
    /**
     * 获取所有角色
     */
    List<SysRole> getAllRoles();
    
    /**
     * 创建权限
     */
    boolean createPermission(SysPermission permission);
    
    /**
     * 创建角色
     */
    boolean createRole(SysRole role);
    
    /**
     * 为角色分配权限
     */
    boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    
    /**
     * 为用户分配角色
     */
    boolean assignRolesToUser(Long userId, List<Long> roleIds);
    
    /**
     * 获取用户的所有角色
     */
    List<SysRole> getUserRoleList(Long userId);
    
    /**
     * 获取用户的所有权限
     */
    List<SysPermission> getUserPermissionList(Long userId);
}
