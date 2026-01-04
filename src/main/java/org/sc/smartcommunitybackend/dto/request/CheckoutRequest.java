package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "结算请求")
public class CheckoutRequest extends BaseRequest {
    @Schema(description = "购物车项ID列表")
    private List<Long> cartIds;
    
    @Schema(description = "选择的门店ID")
    private Long storeId;
}