package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "促销分页查询请求")
public class PromotionPageRequest extends BaseRequest {
    @Schema(description = "页码", example = "1")
    private Integer pageNum;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;

    @Schema(description = "促销名称（模糊查询）")
    private String promotionName;

    @Schema(description = "促销类型 1-明星商品 2-秒杀商品")
    private Integer promotionType;

    @Schema(description = "促销状态 0-失效 1-有效")
    private Integer status;

    @Schema(description = "开始时间（大于等于）")
    private Date startTime;

    @Schema(description = "结束时间（小于等于）")
    private Date endTime;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "是否升序")
    private Boolean isAsc;
}