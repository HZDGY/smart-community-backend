package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发送私信请求DTO
 */
@Data
@Schema(description = "发送私信请求")
public class SendMessageRequest {
    
    /**
     * 接收者ID
     */
    @NotNull(message = "接收者ID不能为空")
    @Schema(description = "接收者ID", required = true)
    private Long toUserId;
    
    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "消息内容", required = true)
    private String content;
}
