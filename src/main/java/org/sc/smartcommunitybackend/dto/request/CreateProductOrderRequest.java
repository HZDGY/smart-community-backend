package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 创建商品订单请求
 */
@Data
@Schema(description = "创建商品订单请求")
public class CreateProductOrderRequest {
    
    @NotNull(message = "门店ID不能为空")
    @Schema(description = "取货门店ID", required = true)
    private Long storeId;
    
    @NotEmpty(message = "购物车项不能为空")
    @Schema(description = "购物车项ID列表", required = true)
    private List<Long> cartItemIds;
    
    @Schema(description = "订单备注")
    private String remark;
}

