package org.sc.smartcommunitybackend.common.annotation;

import java.lang.annotation.*;

/**
 * 权限验证注解
 * 用于方法级别的权限控制
 * 
 * 使用示例：
 * 1. 单个权限：@RequirePermission("user:view")
 * 2. 多个权限（AND）：@RequirePermission(value = {"user:view", "user:update"}, logical = Logical.AND)
 * 3. 多个权限（OR）：@RequirePermission(value = {"user:view", "user:update"}, logical = Logical.OR)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    
    /**
     * 需要的权限编码
     * 支持单个或多个权限
     */
    String[] value();
    
    /**
     * 多个权限之间的逻辑关系
     * AND - 必须拥有所有权限
     * OR - 拥有任意一个权限即可
     * 默认为 AND
     */
    Logical logical() default Logical.AND;
}
