package org.sc.smartcommunitybackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情VO
 */
@Data
@Schema(description = "订单详情")
public class OrderDetailVO {
    
    @Schema(description = "订单ID")
    private Long orderId;
    
    @Schema(description = "订单号")
    private String orderNo;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "订单类型(PRODUCT-商品订单,RECHARGE-充值,PROPERTY_FEE-物业费等)")
    private String orderType;
    
    @Schema(description = "订单类型描述")
    private String orderTypeDesc;
    
    @Schema(description = "订单金额")
    private BigDecimal amount;
    
    @Schema(description = "支付方式(ALIPAY-支付宝,WECHAT-微信,WALLET-钱包)")
    private String paymentMethod;
    
    @Schema(description = "支付方式描述")
    private String paymentMethodDesc;
    
    @Schema(description = "订单状态(0-待支付,1-支付中,2-支付成功/待取货,3-已完成,4-支付失败,5-已取消,6-已退款)")
    private Integer status;
    
    @Schema(description = "订单状态描述")
    private String statusDesc;
    
    @Schema(description = "取货门店ID")
    private Long storeId;
    
    @Schema(description = "取货门店名称")
    private String storeName;
    
    @Schema(description = "订单描述")
    private String description;
    
    @Schema(description = "关联业务ID")
    private Long relatedId;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date callbackTime;
    
    @Schema(description = "取货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pickUpTime;
    
    @Schema(description = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;
    
    @Schema(description = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
    
    @Schema(description = "订单商品列表(仅商品订单有)")
    private List<OrderProductVO> products;
}

