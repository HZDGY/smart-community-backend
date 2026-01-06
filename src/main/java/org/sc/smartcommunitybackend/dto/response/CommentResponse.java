package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 评论响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论响应")
public class CommentResponse {
    
    @Schema(description = "评论ID")
    private Long commentId;
    
    @Schema(description = "帖子ID")
    private Long postId;
    
    @Schema(description = "评论用户ID")
    private Long userId;
    
    @Schema(description = "评论用户名")
    private String userName;
    
    @Schema(description = "评论用户头像")
    private String userAvatar;
    
    @Schema(description = "父评论ID")
    private Long parentId;
    
    @Schema(description = "回复的用户ID")
    private Long replyToUserId;
    
    @Schema(description = "回复的用户名")
    private String replyToUserName;
    
    @Schema(description = "评论内容")
    private String content;
    
    @Schema(description = "点赞数")
    private Integer likeCount;
    
    @Schema(description = "当前用户是否已点赞")
    private Boolean isLiked;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "子评论列表")
    private List<CommentResponse> replies;
}
