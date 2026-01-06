package org.sc.smartcommunitybackend.dto.request;

import lombok.Data;

/**
 * 更新用户信息请求 DTO
 */
@Data
public class UpdateUserInfoRequest {
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 性别（0-未知 1-男 2-女）
     */
    private Integer gender;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 头像URL
     */
    private String avatar;
}
