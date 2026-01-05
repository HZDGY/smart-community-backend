package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员门店列表请求")
public class AdminStoreListRequest extends BaseRequest {
    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "页码", required = true)
    private Integer pageNum;

    @Schema(description = "每页条数", required = true)
    private Integer pageSize;
}