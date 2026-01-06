package org.sc.smartcommunitybackend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付响应DTO
 */
@Data
@Builder
@Schema(description = "支付响应")
public class PaymentResponse {
    
    @Schema(description = "订单号")
    private String orderNo;
    
    @Schema(description = "支付方式")
    private String paymentMethod;
    
    @Schema(description = "支付金额")
    private BigDecimal amount;
    
    @Schema(description = "支付状态：0-待支付, 1-支付中, 2-支付成功, 3-支付失败")
    private Integer status;
    
    @Schema(description = "支付URL（用于跳转支付页面）")
    private String paymentUrl;
    
    @Schema(description = "第三方订单号")
    private String thirdPartyOrderNo;
    
    @Schema(description = "提示信息")
    private String message;
}
