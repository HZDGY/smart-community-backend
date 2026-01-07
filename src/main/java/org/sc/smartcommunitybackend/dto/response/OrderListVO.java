package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单列表VO
 */
@Data
@Schema(description = "订单列表项")
public class OrderListVO {
    
    @Schema(description = "订单ID")
    private Long orderId;
    
    @Schema(description = "订单号")
    private String orderNo;
    
    @Schema(description = "订单类型")
    private String orderType;
    
    @Schema(description = "订单类型描述")
    private String orderTypeDesc;
    
    @Schema(description = "订单金额")
    private BigDecimal amount;
    
    @Schema(description = "订单状态(0-待支付,1-支付中,2-支付成功/待取货,3-已完成,4-支付失败,5-已取消,6-已退款)")
    private Integer status;
    
    @Schema(description = "订单状态描述")
    private String statusDesc;
    
    @Schema(description = "支付方式")
    private String paymentMethod;
    
    @Schema(description = "支付方式描述")
    private String paymentMethodDesc;
    
    @Schema(description = "取货门店名称(商品订单)")
    private String storeName;
    
    @Schema(description = "订单描述")
    private String description;
    
    @Schema(description = "商品数量(商品订单)")
    private Integer productCount;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
}

