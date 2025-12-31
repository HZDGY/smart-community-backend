package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品搜索请求")
public class ProductSearchRequestDTO extends BaseRequest {
    @Schema(description = "搜索关键词")
    private String keyword;
    
    @Schema(description = "页码")
    private Integer pageNum;
    
    @Schema(description = "每页条数")
    private Integer pageSize;
}