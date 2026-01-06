package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 服务区域分页查询请求
 */
@Data
@Schema(description = "服务区域分页查询请求")
public class ServiceAreaPageRequest extends BaseRequest {
    @Schema(description = "页码", example = "1")
    private Integer pageNum;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;

    @Schema(description = "区域名称（模糊查询）")
    private String areaName;

    @Schema(description = "父区域ID")
    private Long parentId;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "是否升序")
    private Boolean isAsc;
}