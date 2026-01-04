package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门店列表查询请求")
public class StoreListRequest extends BaseRequest {
    @Schema(description = "商品ID（可选，筛选支持该商品的门店）")
    private Long productId;

    @Schema(description = "页码")
    private Integer pageNum;

    @Schema(description = "每页条数")
    private Integer pageSize;
}