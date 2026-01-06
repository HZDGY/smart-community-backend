package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 私信响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "私信响应")
public class MessageResponse {
    
    @Schema(description = "消息ID")
    private Long messageId;
    
    @Schema(description = "发送者ID")
    private Long fromUserId;
    
    @Schema(description = "发送者用户名")
    private String fromUserName;
    
    @Schema(description = "发送者头像")
    private String fromUserAvatar;
    
    @Schema(description = "接收者ID")
    private Long toUserId;
    
    @Schema(description = "接收者用户名")
    private String toUserName;
    
    @Schema(description = "接收者头像")
    private String toUserAvatar;
    
    @Schema(description = "消息内容")
    private String content;
    
    @Schema(description = "是否已读 0-未读 1-已读")
    private Integer isRead;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "阅读时间")
    private Date readTime;
}
