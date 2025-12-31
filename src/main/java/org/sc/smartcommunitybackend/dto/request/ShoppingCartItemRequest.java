package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购物车项操作请求")
public class ShoppingCartItemRequest extends BaseRequest {
    @Schema(description = "购物车ID")
    private Long cartId;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "更新数量")
    private Integer quantity;
    
    @Schema(description = "操作类型：update(更新数量), remove(移除)")
    private String operation;
}