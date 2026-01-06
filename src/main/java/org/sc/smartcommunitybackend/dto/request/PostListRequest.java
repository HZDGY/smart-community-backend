package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 帖子列表查询请求DTO
 */
@Data
@Schema(description = "帖子列表查询请求")
public class PostListRequest extends PageQueryDTO {
    
    /**
     * 板块ID（可选）
     */
    @Schema(description = "板块ID，不传则查询所有板块")
    private Long sectionId;
    
    /**
     * 关键词搜索（标题或内容）
     */
    @Schema(description = "关键词搜索")
    private String keyword;
    
    /**
     * 是否只看精华 0-否 1-是
     */
    @Schema(description = "是否只看精华 0-否 1-是")
    private Integer isEssence;
    
    /**
     * 排序方式：latest-最新, hot-最热, essence-精华
     */
    @Schema(description = "排序方式：latest-最新, hot-最热, essence-精华", example = "latest")
    private String sortBy = "latest";
}
