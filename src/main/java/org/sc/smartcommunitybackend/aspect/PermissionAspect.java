package org.sc.smartcommunitybackend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.sc.smartcommunitybackend.common.annotation.Logical;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.common.annotation.RequireRole;
import org.sc.smartcommunitybackend.exception.PermissionDeniedException;
import org.sc.smartcommunitybackend.service.PermissionService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限验证切面
 * 拦截带有 @RequirePermission 和 @RequireRole 注解的方法
 */
@Aspect
@Component
@Order(2) // 在 JWT 拦截器之后执行
public class PermissionAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionAspect.class);
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * 权限验证切点
     */
    @Before("@annotation(org.sc.smartcommunitybackend.common.annotation.RequirePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        // 获取当前用户ID
        Long userId = UserContextUtil.getCurrentUserId();
        if (userId == null) {
            throw new PermissionDeniedException("用户未登录");
        }
        
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);
        
        if (annotation == null) {
            return;
        }
        
        String[] requiredPermissions = annotation.value();
        Logical logical = annotation.logical();
        
        boolean hasPermission;
        if (logical == Logical.AND) {
            // 必须拥有所有权限
            hasPermission = permissionService.hasAllPermissions(userId, requiredPermissions);
            if (!hasPermission) {
                logger.warn("用户 {} 缺少权限：需要所有权限 {}", userId, String.join(", ", requiredPermissions));
                throw new PermissionDeniedException("权限不足：需要所有权限 [" + String.join(", ", requiredPermissions) + "]");
            }
        } else {
            // 拥有任意一个权限即可
            hasPermission = permissionService.hasAnyPermission(userId, requiredPermissions);
            if (!hasPermission) {
                logger.warn("用户 {} 缺少权限：需要任意一个权限 {}", userId, String.join(", ", requiredPermissions));
                throw new PermissionDeniedException("权限不足：需要任意一个权限 [" + String.join(", ", requiredPermissions) + "]");
            }
        }
        
        logger.debug("用户 {} 权限验证通过：{}", userId, String.join(", ", requiredPermissions));
    }
    
    /**
     * 角色验证切点
     */
    @Before("@annotation(org.sc.smartcommunitybackend.common.annotation.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        // 获取当前用户ID
        Long userId = UserContextUtil.getCurrentUserId();
        if (userId == null) {
            throw new PermissionDeniedException("用户未登录");
        }
        
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole annotation = method.getAnnotation(RequireRole.class);
        
        if (annotation == null) {
            return;
        }
        
        String[] requiredRoles = annotation.value();
        Logical logical = annotation.logical();
        
        boolean hasRole;
        if (logical == Logical.AND) {
            // 必须拥有所有角色
            hasRole = permissionService.hasAllRoles(userId, requiredRoles);
            if (!hasRole) {
                logger.warn("用户 {} 缺少角色：需要所有角色 {}", userId, String.join(", ", requiredRoles));
                throw new PermissionDeniedException("权限不足：需要所有角色 [" + String.join(", ", requiredRoles) + "]");
            }
        } else {
            // 拥有任意一个角色即可
            hasRole = permissionService.hasAnyRole(userId, requiredRoles);
            if (!hasRole) {
                logger.warn("用户 {} 缺少角色：需要任意一个角色 {}", userId, String.join(", ", requiredRoles));
                throw new PermissionDeniedException("权限不足：需要任意一个角色 [" + String.join(", ", requiredRoles) + "]");
            }
        }
        
        logger.debug("用户 {} 角色验证通过：{}", userId, String.join(", ", requiredRoles));
    }
}
