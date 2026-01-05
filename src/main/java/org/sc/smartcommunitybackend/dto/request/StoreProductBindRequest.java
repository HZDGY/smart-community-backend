package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "门店商品绑定请求")
public class StoreProductBindRequest extends BaseRequest {
    @Schema(description = "商品列表", required = true)
    private List<StoreProductItemRequest> products;

    @NoArgsConstructor
    @Data
    public static class StoreProductItemRequest {
        @Schema(description = "商品ID", required = true)
        private Long productId;

        @Schema(description = "分配给该门店的库存", required = true)
        private Integer stock;

        @Schema(description = "上架状态", required = true)
        private String status;
    }
}