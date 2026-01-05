package org.sc.smartcommunitybackend.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 用户权限响应 DTO
 */
@Data
public class UserPermissionResponse {
    private Long userId;
    private List<RoleDTO> roles;
    private List<PermissionDTO> permissions;
}
