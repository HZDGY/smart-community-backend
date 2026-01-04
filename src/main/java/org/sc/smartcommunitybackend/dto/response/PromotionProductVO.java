package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "促销商品")
public class PromotionProductVO extends BaseResponse {
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "促销价格")
    private BigDecimal promotionPrice;
    
    @Schema(description = "封面图片")
    private String coverImg;
    
    @Schema(description = "促销类型")
    private String promotionType;
    
    @Schema(description = "促销描述")
    private String promotionDesc;
}