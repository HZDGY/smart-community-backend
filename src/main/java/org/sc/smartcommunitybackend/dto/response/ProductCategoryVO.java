package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品分类响应")
public class ProductCategoryVO extends BaseResponse {
    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "父分类ID（顶级分类为null）")
    private Long parentId;

    @Schema(description = "排序权重（越小越靠前）")
    private Integer sortOrder;
}