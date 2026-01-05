package org.sc.smartcommunitybackend.dto.response;

import lombok.Data;

/**
 * 角色响应 DTO
 */
@Data
public class RoleDTO {
    private Long roleId;
    private String roleName;
    private String roleCode;
    private String description;
    private Integer status;
}
