package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 帖子列表项响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "帖子列表项响应")
public class PostListItemResponse {
    
    @Schema(description = "帖子ID")
    private Long postId;
    
    @Schema(description = "板块ID")
    private Long sectionId;
    
    @Schema(description = "板块名称")
    private String sectionName;
    
    @Schema(description = "发帖用户ID")
    private Long userId;
    
    @Schema(description = "发帖用户名")
    private String userName;
    
    @Schema(description = "发帖用户头像")
    private String userAvatar;
    
    @Schema(description = "帖子标题")
    private String title;
    
    @Schema(description = "帖子内容摘要")
    private String contentSummary;
    
    @Schema(description = "首图URL")
    private String firstImage;
    
    @Schema(description = "浏览次数")
    private Integer viewCount;
    
    @Schema(description = "点赞数")
    private Integer likeCount;
    
    @Schema(description = "评论数")
    private Integer commentCount;
    
    @Schema(description = "是否置顶 0-否 1-是")
    private Integer isTop;
    
    @Schema(description = "是否精华 0-否 1-是")
    private Integer isEssence;
    
    @Schema(description = "创建时间")
    private Date createTime;
}
