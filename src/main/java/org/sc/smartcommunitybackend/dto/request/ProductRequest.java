package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "商品请求")
public class ProductRequest extends BaseRequest {
    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称", required = true)
    private String productName;

    @Schema(description = "分类ID", required = true)
    private Long categoryId;

    @Schema(description = "售价", required = true)
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "库存", required = true)
    private Integer stock;

    @Schema(description = "封面图", required = true)
    private String coverImg;

    @Schema(description = "详情图")
    private List<String> detailImgs;

    @Schema(description = "商品简介")
    private String description;

    @Schema(description = "状态（ON_SALE-上架、OFF_SALE-下架）")
    private String status;
}