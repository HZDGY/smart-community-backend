package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品VO
 */
@Data
@Schema(description = "订单商品信息")
public class OrderProductVO {
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品图片")
    private String productImage;
    
    @Schema(description = "商品数量")
    private Integer quantity;
    
    @Schema(description = "商品单价")
    private BigDecimal price;
    
    @Schema(description = "小计金额")
    private BigDecimal subtotal;
}

