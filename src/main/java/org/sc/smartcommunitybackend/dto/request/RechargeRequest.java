package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值请求 DTO
 */
@Data
public class RechargeRequest {
    
    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0")
    private BigDecimal amount;
    
    /**
     * 支付方式（ALIPAY-支付宝 WECHAT-微信）
     */
    @NotNull(message = "支付方式不能为空")
    private String paymentMethod;
}
