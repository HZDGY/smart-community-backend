package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "商品详情")
public class ProductDetailVO extends BaseResponse {
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "商品简介")
    private String description;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "封面图片")
    private String coverImg;
    
    @Schema(description = "是否已收藏")
    private Boolean isCollected;
    
    @Schema(description = "可选择的门店列表")
    private List<StoreListItemVO> availableStores;
}