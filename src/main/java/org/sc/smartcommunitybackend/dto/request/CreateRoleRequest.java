package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建角色请求 DTO
 */
@Data
public class CreateRoleRequest {
    
    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;
    
    private String description;
}
