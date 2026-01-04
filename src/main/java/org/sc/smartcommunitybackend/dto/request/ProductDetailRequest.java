package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品详情请求")
public class ProductDetailRequest extends BaseRequest {
    @Schema(description = "商品ID")
    private Long productId;
}