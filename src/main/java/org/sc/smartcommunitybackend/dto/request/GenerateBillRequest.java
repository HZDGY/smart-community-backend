package org.sc.smartcommunitybackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生成物业费账单请求 DTO
 */
@Data
public class GenerateBillRequest {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 账期（如：2026-01）
     */
    @NotBlank(message = "账期不能为空")
    private String billingPeriod;
    
    /**
     * 物业费
     */
    private BigDecimal propertyFee;
    
    /**
     * 水费
     */
    private BigDecimal waterFee;
    
    /**
     * 电费
     */
    private BigDecimal electricityFee;
    
    /**
     * 燃气费
     */
    private BigDecimal gasFee;
    
    /**
     * 停车费
     */
    private BigDecimal parkingFee;
    
    /**
     * 其他费用
     */
    private BigDecimal otherFee;
    
    /**
     * 缴费截止日期
     */
    private Date dueDate;
}
