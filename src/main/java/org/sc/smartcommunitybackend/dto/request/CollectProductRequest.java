package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收藏商品请求")
public class CollectProductRequest extends BaseRequest {
    @Schema(description = "商品ID")
    private Long productId;
}