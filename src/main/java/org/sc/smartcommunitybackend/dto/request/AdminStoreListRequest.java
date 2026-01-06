package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理端门店列表查询请求
 */
@Data
@Schema(description = "管理端门店列表查询请求")
public class AdminStoreListRequest extends BaseRequest {
    @Schema(description = "页码", example = "1")
    private Integer pageNum;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;

    @Schema(description = "门店名称（模糊查询）")
    private String storeName;

    @Schema(description = "所属区域ID")
    private Long areaId;

    @Schema(description = "状态 0-关闭 1-正常营业")
    private Integer status;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "是否升序")
    private Boolean isAsc;
}