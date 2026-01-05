package org.sc.smartcommunitybackend.common.annotation;

/**
 * 逻辑枚举
 * 用于权限注解中的逻辑判断
 */
public enum Logical {
    /**
     * 与（AND）- 必须满足所有条件
     */
    AND,
    
    /**
     * 或（OR）- 满足任意一个条件即可
     */
    OR
}
