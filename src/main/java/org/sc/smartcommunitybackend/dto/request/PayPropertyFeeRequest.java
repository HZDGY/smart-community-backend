package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 缴纳物业费请求 DTO
 */
@Data
public class PayPropertyFeeRequest {
    
    /**
     * 账单ID
     */
    @NotNull(message = "账单ID不能为空")
    private Long billId;
    
    /**
     * 缴费金额
     */
    @NotNull(message = "缴费金额不能为空")
    @DecimalMin(value = "0.01", message = "缴费金额必须大于0")
    private BigDecimal amount;
    
    /**
     * 支付方式（WALLET-钱包 ALIPAY-支付宝 WECHAT-微信）
     */
    @NotNull(message = "支付方式不能为空")
    private String paymentMethod;
}
