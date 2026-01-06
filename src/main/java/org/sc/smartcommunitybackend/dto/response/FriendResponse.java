package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 好友信息响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "好友信息响应")
public class FriendResponse {
    
    @Schema(description = "好友关系ID")
    private Long friendId;
    
    @Schema(description = "好友用户ID")
    private Long friendUserId;
    
    @Schema(description = "好友用户名")
    private String friendUserName;
    
    @Schema(description = "好友头像")
    private String friendAvatar;
    
    @Schema(description = "好友备注")
    private String remark;
    
    @Schema(description = "状态 0-待确认 1-已同意 2-已拒绝")
    private Integer status;
    
    @Schema(description = "创建时间")
    private Date createTime;
}
