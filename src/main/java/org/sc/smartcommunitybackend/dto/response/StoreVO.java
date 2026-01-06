package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门店详情")
public class StoreVO extends BaseResponse {
    @Schema(description = "门店ID")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "营业时间")
    private String businessHours;

    @Schema(description = "联系电话")
    private String contactPhone;
    @Schema(description = "状态 0-关闭 1-正常营业")
    private Integer status;
}