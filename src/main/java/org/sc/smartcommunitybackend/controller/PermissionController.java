package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.common.annotation.RequireRole;
import org.sc.smartcommunitybackend.domain.SysPermission;
import org.sc.smartcommunitybackend.domain.SysRole;
import org.sc.smartcommunitybackend.dto.request.AssignPermissionRequest;
import org.sc.smartcommunitybackend.dto.request.AssignRoleRequest;
import org.sc.smartcommunitybackend.dto.request.CreatePermissionRequest;
import org.sc.smartcommunitybackend.dto.request.CreateRoleRequest;
import org.sc.smartcommunitybackend.dto.response.PermissionDTO;
import org.sc.smartcommunitybackend.dto.response.PermissionScanResult;
import org.sc.smartcommunitybackend.dto.response.RoleDTO;
import org.sc.smartcommunitybackend.dto.response.UserPermissionResponse;
import org.sc.smartcommunitybackend.service.PermissionScanService;
import org.sc.smartcommunitybackend.service.PermissionService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permission")
@Tag(name = "权限管理", description = "权限和角色管理相关接口")
public class PermissionController extends BaseController {
    
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private PermissionScanService permissionScanService;
    
    @GetMapping("/list")
    @Operation(summary = "获取所有权限", description = "查询系统中所有的权限列表")
    @RequirePermission("permission:view")
    public Result<List<PermissionDTO>> getAllPermissions() {
        List<SysPermission> permissions = permissionService.getAllPermissions();
        List<PermissionDTO> dtoList = permissions.stream().map(permission -> {
            PermissionDTO dto = new PermissionDTO();
            BeanUtils.copyProperties(permission, dto);
            return dto;
        }).collect(Collectors.toList());
        return success(dtoList);
    }
    
    @PostMapping("/create")
    @Operation(summary = "创建权限", description = "创建新的权限")
    @RequirePermission("permission:create")
    public Result<Void> createPermission(
            @Parameter(description = "创建权限请求", required = true)
            @RequestBody @Valid CreatePermissionRequest request) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(request, permission);
        permission.setStatus(1); // 默认启用
        
        boolean success = permissionService.createPermission(permission);
        return success ? success() : error("创建权限失败");
    }
    
    @GetMapping("/role/list")
    @Operation(summary = "获取所有角色", description = "查询系统中所有的角色列表")
    @RequirePermission("role:view")
    public Result<List<RoleDTO>> getAllRoles() {
        List<SysRole> roles = permissionService.getAllRoles();
        List<RoleDTO> dtoList = roles.stream().map(role -> {
            RoleDTO dto = new RoleDTO();
            BeanUtils.copyProperties(role, dto);
            return dto;
        }).collect(Collectors.toList());
        return success(dtoList);
    }
    
    @PostMapping("/role/create")
    @Operation(summary = "创建角色", description = "创建新的角色")
    @RequirePermission("role:create")
    public Result<Void> createRole(
            @Parameter(description = "创建角色请求", required = true)
            @RequestBody @Valid CreateRoleRequest request) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(request, role);
        role.setStatus(1); // 默认启用
        
        boolean success = permissionService.createRole(role);
        return success ? success() : error("创建角色失败");
    }
    
    @PostMapping("/role/assign-permissions")
    @Operation(summary = "为角色分配权限", description = "为指定角色分配一组权限")
    @RequirePermission("role:assign-permission")
    public Result<Void> assignPermissionsToRole(
            @Parameter(description = "分配权限请求", required = true)
            @RequestBody @Valid AssignPermissionRequest request) {
        boolean success = permissionService.assignPermissionsToRole(
                request.getRoleId(), 
                request.getPermissionIds()
        );
        return success ? success() : error("分配权限失败");
    }
    
    @PostMapping("/user/assign-roles")
    @Operation(summary = "为用户分配角色", description = "为指定用户分配一组角色")
    @RequirePermission("user:assign-role")
    public Result<Void> assignRolesToUser(
            @Parameter(description = "分配角色请求", required = true)
            @RequestBody @Valid AssignRoleRequest request) {
        boolean success = permissionService.assignRolesToUser(
                request.getUserId(), 
                request.getRoleIds()
        );
        return success ? success() : error("分配角色失败");
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户权限信息", description = "查询指定用户的所有角色和权限")
    @RequirePermission("user:view-permission")
    public Result<UserPermissionResponse> getUserPermissions(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        // 查询用户的角色和权限
        List<SysRole> roles = permissionService.getUserRoleList(userId);
        List<SysPermission> permissions = permissionService.getUserPermissionList(userId);
        
        // 转换为 DTO
        List<RoleDTO> roleDTOs = roles.stream().map(role -> {
            RoleDTO dto = new RoleDTO();
            BeanUtils.copyProperties(role, dto);
            return dto;
        }).collect(Collectors.toList());
        
        List<PermissionDTO> permissionDTOs = permissions.stream().map(permission -> {
            PermissionDTO dto = new PermissionDTO();
            BeanUtils.copyProperties(permission, dto);
            return dto;
        }).collect(Collectors.toList());
        
        UserPermissionResponse response = new UserPermissionResponse();
        response.setUserId(userId);
        response.setRoles(roleDTOs);
        response.setPermissions(permissionDTOs);
        
        return success(response);
    }
    
    @GetMapping("/user/current")
    @Operation(summary = "获取当前用户权限信息", description = "查询当前登录用户的所有角色和权限")
    public Result<UserPermissionResponse> getCurrentUserPermissions() {
        Long userId = UserContextUtil.getCurrentUserId();
        return getUserPermissions(userId);
    }
    
    @PostMapping("/scan")
    @Operation(summary = "扫描并同步权限", description = "扫描所有Controller中的权限注解并自动存入数据库（仅超级管理员可用）")
    @RequireRole("ROLE_SUPER_ADMIN")
    public Result<PermissionScanResult> scanPermissions() {
        PermissionScanResult result = permissionScanService.scanAndSavePermissions();
        return success("权限扫描完成", result);
    }
}
