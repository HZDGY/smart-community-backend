package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 分配角色请求 DTO
 */
@Data
public class AssignRolesRequest {
    
    /**
     * 角色ID列表
     */
    @NotEmpty(message = "角色列表不能为空")
    private List<Long> roleIds;
}
