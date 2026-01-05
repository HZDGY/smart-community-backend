package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门店商品上下架请求")
public class StoreProductStatusRequest extends BaseRequest {
    @Schema(description = "状态", required = true)
    private String status;
}