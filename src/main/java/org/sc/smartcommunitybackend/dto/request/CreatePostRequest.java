package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建帖子请求DTO
 */
@Data
@Schema(description = "创建帖子请求")
public class CreatePostRequest {
    
    /**
     * 板块ID
     */
    @NotNull(message = "板块ID不能为空")
    @Schema(description = "板块ID", required = true)
    private Long sectionId;
    
    /**
     * 帖子标题
     */
    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    @Schema(description = "帖子标题", required = true, maxLength = 200)
    private String title;
    
    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    @Schema(description = "帖子内容", required = true)
    private String content;
    
    /**
     * 图片URL列表，逗号分隔
     */
    @Schema(description = "图片URL列表，逗号分隔", example = "http://example.com/1.jpg,http://example.com/2.jpg")
    private String images;
}
