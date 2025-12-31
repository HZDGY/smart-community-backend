package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "购物车汇总")
public class ShoppingCartSummaryVO extends BaseResponse {
    @Schema(description = "购物车项列表")
    private List<ShoppingCartItemVO> items;
    
    @Schema(description = "商品总数量")
    private Integer totalQuantity;
    
    @Schema(description = "商品总金额")
    private BigDecimal totalAmount;
}