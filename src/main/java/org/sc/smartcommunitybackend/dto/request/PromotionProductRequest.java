package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "促销商品绑定请求")
public class PromotionProductRequest extends BaseRequest {
    @Schema(description = "商品ID列表", required = true)
    private List<Long> productIds;
}