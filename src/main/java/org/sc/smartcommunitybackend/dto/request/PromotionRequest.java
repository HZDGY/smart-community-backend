package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "促销请求")
public class PromotionRequest extends BaseRequest {
    @Schema(description = "促销ID")
    private Long promotionId;

    @Schema(description = "促销名称", required = true)
    private String promotionName;

    @Schema(description = "促销类型", required = true)
    private String promotionType;

    @Schema(description = "开始时间", required = true)
    private Date startTime;

    @Schema(description = "结束时间", required = true)
    private Date endTime;

    @Schema(description = "绑定商品ID")
    private List<Long> productIds;
}