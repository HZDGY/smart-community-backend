package org.sc.smartcommunitybackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.dto.request.AssignRolesRequest;
import org.sc.smartcommunitybackend.dto.request.UpdateUserInfoRequest;
import org.sc.smartcommunitybackend.dto.request.UpdateUserStatusRequest;
import org.sc.smartcommunitybackend.dto.request.UserQueryRequest;
import org.sc.smartcommunitybackend.dto.response.UserDetailVO;
import org.sc.smartcommunitybackend.dto.response.UserManagementVO;
import org.sc.smartcommunitybackend.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器（后台管理）
 */
@RestController
@RequestMapping("/admin/users")
@Tag(name = "后台用户管理", description = "后台用户（会员）管理接口")
public class UserManagementController extends BaseController {
    
    @Autowired
    private UserManagementService userManagementService;
    
    @GetMapping
    @Operation(summary = "查询用户列表", description = "分页查询用户列表，支持关键词搜索和条件筛选")
    @RequirePermission("user:list")
    public Result<Page<UserManagementVO>> getUserList(UserQueryRequest request) {
        Page<UserManagementVO> page = userManagementService.getUserList(request);
        return success(page);
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情", description = "查看用户详细信息，包括角色和钱包信息")
    @RequirePermission("user:view")
    public Result<UserDetailVO> getUserDetail(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        UserDetailVO detail = userManagementService.getUserDetail(userId);
        return success(detail);
    }
    
    @PutMapping("/{userId}/status")
    @Operation(summary = "更新用户状态", description = "启用或冻结用户账号")
    @RequirePermission("user:status")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "状态请求", required = true)
            @RequestBody @Valid UpdateUserStatusRequest request) {
        userManagementService.updateUserStatus(userId, request.getStatus());
        return success();
    }
    
    @PostMapping("/{userId}/roles")
    @Operation(summary = "分配角色", description = "为用户分配角色（覆盖现有角色）")
    @RequirePermission("user:role")
    public Result<Void> assignRoles(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "角色分配请求", required = true)
            @RequestBody @Valid AssignRolesRequest request) {
        userManagementService.assignRoles(userId, request.getRoleIds());
        return success();
    }
    
    @PutMapping("/{userId}")
    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    @RequirePermission("user:edit")
    public Result<Void> updateUserInfo(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "更新请求", required = true)
            @RequestBody UpdateUserInfoRequest request) {
        userManagementService.updateUserInfo(userId, request);
        return success();
    }
    
    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "删除用户账号（软删除，将状态设为冻结）")
    @RequirePermission("user:delete")
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        userManagementService.deleteUser(userId);
        return success();
    }
}
