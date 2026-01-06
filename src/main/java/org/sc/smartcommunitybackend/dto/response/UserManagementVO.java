package org.sc.smartcommunitybackend.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户管理视图对象
 */
@Data
public class UserManagementVO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别（0-未知 1-男 2-女）
     */
    private Integer gender;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 用户类型（1-普通用户 2-商户管理员 3-社区管理员）
     */
    private Integer userType;
    
    /**
     * 状态（0-冻结 1-正常）
     */
    private Integer status;
    
    /**
     * 角色列表
     */
    private List<String> roles;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}
