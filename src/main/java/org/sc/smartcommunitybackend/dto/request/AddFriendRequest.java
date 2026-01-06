package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加好友请求DTO
 */
@Data
@Schema(description = "添加好友请求")
public class AddFriendRequest {
    
    /**
     * 好友用户ID
     */
    @NotNull(message = "好友用户ID不能为空")
    @Schema(description = "好友用户ID", required = true)
    private Long friendUserId;
    
    /**
     * 好友备注
     */
    @Schema(description = "好友备注")
    private String remark;
}
