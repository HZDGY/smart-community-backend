package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "门店列表项")
public class StoreListItemVO extends BaseResponse {
    @Schema(description = "门店ID")
    private Long storeId;
    
    @Schema(description = "门店名称")
    private String storeName;
    
    @Schema(description = "详细地址")
    private String address;
    
    @Schema(description = "营业时间")
    private String businessHours;
    
    @Schema(description = "库存数量")
    private Integer stock;
}