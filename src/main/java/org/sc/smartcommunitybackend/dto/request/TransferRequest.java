package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 转账请求 DTO
 */
@Data
public class TransferRequest {
    
    /**
     * 收款用户ID
     */
    @NotNull(message = "收款用户ID不能为空")
    private Long toUserId;
    
    /**
     * 转账金额
     */
    @NotNull(message = "转账金额不能为空")
    @DecimalMin(value = "0.01", message = "转账金额必须大于0")
    private BigDecimal amount;
    
    /**
     * 转账描述
     */
    private String description;
}
