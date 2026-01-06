package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 帖子详情响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "帖子详情响应")
public class PostDetailResponse {
    
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
    
    @Schema(description = "帖子内容")
    private String content;
    
    @Schema(description = "图片URL列表，逗号分隔")
    private String images;
    
    @Schema(description = "浏览次数")
    private Integer viewCount;
    
    @Schema(description = "点赞数")
    private Integer likeCount;
    
    @Schema(description = "评论数")
    private Integer commentCount;
    
    @Schema(description = "收藏数")
    private Integer collectCount;
    
    @Schema(description = "是否置顶 0-否 1-是")
    private Integer isTop;
    
    @Schema(description = "是否精华 0-否 1-是")
    private Integer isEssence;
    
    @Schema(description = "当前用户是否已点赞")
    private Boolean isLiked;
    
    @Schema(description = "当前用户是否已收藏")
    private Boolean isCollected;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "更新时间")
    private Date updateTime;
}
