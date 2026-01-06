package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建评论请求DTO
 */
@Data
@Schema(description = "创建评论请求")
public class CreateCommentRequest {
    
    /**
     * 帖子ID
     */
    @NotNull(message = "帖子ID不能为空")
    @Schema(description = "帖子ID", required = true)
    private Long postId;
    
    /**
     * 父评论ID，0表示一级评论
     */
    @Schema(description = "父评论ID，0表示一级评论", example = "0")
    private Long parentId = 0L;
    
    /**
     * 回复的用户ID（回复别人的评论时需要）
     */
    @Schema(description = "回复的用户ID")
    private Long replyToUserId;
    
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Schema(description = "评论内容", required = true)
    private String content;
}
