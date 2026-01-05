package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品分类请求")
public class ProductCategoryRequest extends BaseRequest {
    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称", required = true)
    private String categoryName;

    @Schema(description = "父分类ID（顶级分类为null）")
    private Long parentId;

    @Schema(description = "排序权重（越小越靠前）")
    private Integer sortOrder;
}