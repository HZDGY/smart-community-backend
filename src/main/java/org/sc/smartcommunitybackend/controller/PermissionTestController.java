package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.common.annotation.Logical;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.common.annotation.RequireRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限测试控制器
 * 用于演示和测试权限系统的各种功能
 */
@RestController
@RequestMapping("/test/permission")
@Tag(name = "权限测试", description = "权限系统测试接口")
public class PermissionTestController extends BaseController {
    
    @GetMapping("/public")
    @Operation(summary = "公开接口", description = "无需权限即可访问")
    public Result<String> publicEndpoint() {
        return success("这是一个公开接口，任何人都可以访问");
    }
    
    @GetMapping("/user-view")
    @Operation(summary = "查看用户权限测试", description = "需要 user:view 权限")
    @RequirePermission("user:view")
    public Result<String> userViewTest() {
        return success("您拥有 user:view 权限，可以查看用户信息");
    }
    
    @GetMapping("/user-create")
    @Operation(summary = "创建用户权限测试", description = "需要 user:create 权限")
    @RequirePermission("user:create")
    public Result<String> userCreateTest() {
        return success("您拥有 user:create 权限，可以创建用户");
    }
    
    @GetMapping("/user-update-and-view")
    @Operation(summary = "多权限AND测试", description = "需要同时拥有 user:view 和 user:update 权限")
    @RequirePermission(value = {"user:view", "user:update"}, logical = Logical.AND)
    public Result<String> userUpdateAndViewTest() {
        return success("您同时拥有 user:view 和 user:update 权限");
    }
    
    @GetMapping("/complaint-or-repair")
    @Operation(summary = "多权限OR测试", description = "拥有 complaint:view 或 repair:view 任意一个权限即可")
    @RequirePermission(value = {"complaint:view", "repair:view"}, logical = Logical.OR)
    public Result<String> complaintOrRepairTest() {
        return success("您拥有 complaint:view 或 repair:view 权限");
    }
    
    @GetMapping("/admin-only")
    @Operation(summary = "管理员角色测试", description = "只有超级管理员可以访问")
    @RequireRole("ROLE_SUPER_ADMIN")
    public Result<String> adminOnlyTest() {
        return success("您是超级管理员");
    }
    
    @GetMapping("/admin-or-community")
    @Operation(summary = "多角色OR测试", description = "超级管理员或社区管理员可以访问")
    @RequireRole(value = {"ROLE_SUPER_ADMIN", "ROLE_COMMUNITY_ADMIN"}, logical = Logical.OR)
    public Result<String> adminOrCommunityTest() {
        return success("您是超级管理员或社区管理员");
    }
    
    @GetMapping("/permission-delete")
    @Operation(summary = "删除权限测试", description = "需要 permission:delete 权限（默认只有超级管理员拥有）")
    @RequirePermission("permission:delete")
    public Result<String> permissionDeleteTest() {
        return success("您拥有 permission:delete 权限，这是一个高级权限");
    }
}
