package org.sc.smartcommunitybackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建支付订单请求DTO
 */
@Data
@Schema(description = "创建支付订单请求")
public class CreatePaymentOrderRequest {
    
    @NotBlank(message = "订单类型不能为空")
    @Schema(description = "订单类型：RECHARGE-充值, PROPERTY_FEE-物业费", example = "RECHARGE")
    private String orderType;
    
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "订单金额", example = "100.00")
    private BigDecimal amount;
    
    @NotBlank(message = "支付方式不能为空")
    @Schema(description = "支付方式：ALIPAY-支付宝, WECHAT-微信, WALLET-钱包", example = "ALIPAY")
    private String paymentMethod;
    
    @Schema(description = "关联业务ID（如物业费账单ID）", example = "1")
    private Long relatedId;
    
    @Schema(description = "订单描述", example = "钱包充值")
    private String description;
}
