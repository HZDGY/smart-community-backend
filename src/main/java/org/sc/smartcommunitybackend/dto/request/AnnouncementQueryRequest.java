package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 公告查询请求
 */
@Data
@Schema(description = "公告查询请求")
public class AnnouncementQueryRequest {

    @Schema(description = "关键词(搜索标题和内容)", example = "停水通知")
    private String keyword;

    @Schema(description = "检索范围: ALL-全文检索, TITLE-仅标题", example = "ALL", allowableValues = {"ALL", "TITLE"})
    private String searchScope = "ALL";

    @Schema(description = "时间范围: ALL-全部, WEEK-最近一周, MONTH-最近一月, YEAR-最近一年", 
            example = "ALL", 
            allowableValues = {"ALL", "WEEK", "MONTH", "YEAR"})
    private String timeRange = "ALL";

    @Schema(description = "排序方式: TIME-按时间排序, RELEVANCE-按相关度排序(仅在有关键词时生效)", 
            example = "TIME", 
            allowableValues = {"TIME", "RELEVANCE"})
    private String sortType = "TIME";

    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", example = "10")
    @Min(value = 1, message = "每页数量必须大于0")
    private Integer pageSize = 10;
}

