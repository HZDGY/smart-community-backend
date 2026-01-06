package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 板块响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "板块响应")
public class SectionResponse {
    
    @Schema(description = "板块ID")
    private Long sectionId;
    
    @Schema(description = "板块名称")
    private String sectionName;
    
    @Schema(description = "板块描述")
    private String sectionDesc;
    
    @Schema(description = "板块图标URL")
    private String iconUrl;
    
    @Schema(description = "排序序号")
    private Integer sortOrder;
    
    @Schema(description = "帖子数量")
    private Integer postCount;
    
    @Schema(description = "状态 0-禁用 1-启用")
    private Integer status;
    
    @Schema(description = "创建时间")
    private Date createTime;
}
