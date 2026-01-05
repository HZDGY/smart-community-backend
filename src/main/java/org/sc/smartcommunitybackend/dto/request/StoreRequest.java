package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门店请求")
public class StoreRequest extends BaseRequest {
    @Schema(description = "门店ID")
    private Long storeId;

    @Schema(description = "门店名称", required = true)
    private String storeName;

    @Schema(description = "区域ID", required = true)
    private Long areaId;

    @Schema(description = "详细地址", required = true)
    private String address;

    @Schema(description = "营业时间", required = true)
    private String businessHours;

    @Schema(description = "联系电话", required = true)
    private String contactPhone;

    @Schema(description = "状态（NORMAL-正常、CLOSED-关闭）")
    private String status;
}