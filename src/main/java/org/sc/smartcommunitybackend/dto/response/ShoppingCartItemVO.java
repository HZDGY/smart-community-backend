package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "购物车项")
public class ShoppingCartItemVO extends BaseResponse {
    @Schema(description = "购物车ID")
    private Long cartId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "封面图片")
    private String coverImg;
    
    @Schema(description = "门店ID")
    private Long storeId;
    
    @Schema(description = "门店名称")
    private String storeName;
    
    @Schema(description = "商品数量")
    private Integer quantity;
    
    @Schema(description = "小计金额")
    private BigDecimal subtotal;
}