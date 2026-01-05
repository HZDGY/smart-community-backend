package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建权限请求 DTO
 */
@Data
public class CreatePermissionRequest {
    
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;
    
    @NotBlank(message = "权限编码不能为空")
    private String permissionCode;
    
    private String resourceType;
    
    private String description;
}
