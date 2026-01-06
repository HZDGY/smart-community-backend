package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "促销商品添加请求")
public class PromotionProductAddRequest {
    @Schema(description = "商品ID列表", required = true)
    private List<Long> productIds;
}
