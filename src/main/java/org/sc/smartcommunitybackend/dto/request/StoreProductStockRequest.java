package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门店商品库存分配请求")
public class StoreProductStockRequest extends BaseRequest {
    @Schema(description = "新库存数量", required = true)
    private Integer stock;
}