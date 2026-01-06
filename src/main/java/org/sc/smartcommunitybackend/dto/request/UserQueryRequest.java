package org.sc.smartcommunitybackend.dto.request;

import lombok.Data;

/**
 * 用户查询请求 DTO
 */
@Data
public class UserQueryRequest {
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页数量
     */
    private Integer size = 10;
    
    /**
     * 关键词（搜索用户名、手机号、邮箱）
     */
    private String keyword;
    
    /**
     * 状态筛选（0-冻结 1-正常）
     */
    private Integer status;
    
    /**
     * 用户类型筛选（1-普通用户 2-商户管理员 3-社区管理员）
     */
    private Integer userType;
}
