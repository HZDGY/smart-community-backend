package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "添加购物车请求")
public class AddToCartRequest extends BaseRequest {
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "门店ID")
    private Long storeId;
    
    @Schema(description = "商品数量")
    private Integer quantity;
}