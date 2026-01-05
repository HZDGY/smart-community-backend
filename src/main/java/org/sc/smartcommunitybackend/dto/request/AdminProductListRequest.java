package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员商品列表请求")
public class AdminProductListRequest extends BaseRequest {
    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "搜索关键词")
    private String keyword;

    @Schema(description = "页码", required = true)
    private Integer pageNum;

    @Schema(description = "每页条数", required = true)
    private Integer pageSize;
}