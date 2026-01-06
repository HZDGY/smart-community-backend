package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 通知响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知响应")
public class NotificationResponse {
    
    @Schema(description = "通知ID")
    private Long notificationId;
    
    @Schema(description = "通知类型 1-系统通知 2-点赞通知 3-评论通知 4-好友申请 5-私信通知")
    private Integer type;
    
    @Schema(description = "通知标题")
    private String title;
    
    @Schema(description = "通知内容")
    private String content;
    
    @Schema(description = "关联ID")
    private Long relatedId;
    
    @Schema(description = "是否已读 0-未读 1-已读")
    private Integer isRead;
    
    @Schema(description = "创建时间")
    private Date createTime;
}
