package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新用户状态请求 DTO
 */
@Data
public class UpdateUserStatusRequest {
    
    /**
     * 状态（0-冻结 1-正常）
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
