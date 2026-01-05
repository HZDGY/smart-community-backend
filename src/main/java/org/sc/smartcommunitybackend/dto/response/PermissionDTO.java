package org.sc.smartcommunitybackend.dto.response;

import lombok.Data;

/**
 * 权限响应 DTO
 */
@Data
public class PermissionDTO {
    private Long permissionId;
    private String permissionName;
    private String permissionCode;
    private String resourceType;
    private String description;
    private Integer status;
}
