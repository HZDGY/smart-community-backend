package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "促销响应")
public class PromotionVO extends BaseResponse {
    @Schema(description = "促销ID")
    private Long promotionId;

    @Schema(description = "促销名称")
    private String promotionName;

    @Schema(description = "促销类型")
    private String promotionType;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "绑定的商品ID")
    private List<Long> productIds;
}