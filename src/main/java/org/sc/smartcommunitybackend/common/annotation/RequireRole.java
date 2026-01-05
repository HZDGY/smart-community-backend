package org.sc.smartcommunitybackend.common.annotation;

import java.lang.annotation.*;

/**
 * 角色验证注解
 * 用于方法级别的角色控制
 * 
 * 使用示例：
 * 1. 单个角色：@RequireRole("ROLE_ADMIN")
 * 2. 多个角色（AND）：@RequireRole(value = {"ROLE_ADMIN", "ROLE_USER"}, logical = Logical.AND)
 * 3. 多个角色（OR）：@RequireRole(value = {"ROLE_ADMIN", "ROLE_USER"}, logical = Logical.OR)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    
    /**
     * 需要的角色编码
     * 支持单个或多个角色
     */
    String[] value();
    
    /**
     * 多个角色之间的逻辑关系
     * AND - 必须拥有所有角色
     * OR - 拥有任意一个角色即可
     * 默认为 OR
     */
    Logical logical() default Logical.OR;
}
