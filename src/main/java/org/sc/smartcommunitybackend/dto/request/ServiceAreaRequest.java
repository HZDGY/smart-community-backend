package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "服务区域请求")
public class ServiceAreaRequest extends BaseRequest {
    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "区域名称", required = true)
    private String areaName;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}