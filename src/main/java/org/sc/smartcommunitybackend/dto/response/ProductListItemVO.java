package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品列表项")
public class ProductListItemVO extends BaseResponse {
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品简介")
    private String description;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "封面图片")
    private String coverImg;
    
    @Schema(description = "销量")
    private Integer sales;
    
    @Schema(description = "是否已收藏")
    private Boolean isCollected;
}