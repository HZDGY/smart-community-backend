package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "商品列表查询请求")
public class ProductListRequest extends BaseRequest {
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "排序方式：sales(销量), price_asc(价格升序), price_desc(价格降序)")
    private String sortBy;
    
    @Schema(description = "价格最小值")
    private BigDecimal minPrice;
    
    @Schema(description = "价格最大值")
    private BigDecimal maxPrice;
    
    @Schema(description = "页码")
    private Integer pageNum;
    
    @Schema(description = "每页条数")
    private Integer pageSize;
}