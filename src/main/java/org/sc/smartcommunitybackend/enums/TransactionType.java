package org.sc.smartcommunitybackend.enums;

/**
 * 交易类型枚举
 */
public enum TransactionType {
    
    /**
     * 充值
     */
    RECHARGE("充值"),
    
    /**
     * 转出
     */
    TRANSFER_OUT("转出"),
    
    /**
     * 转入
     */
    TRANSFER_IN("转入"),
    
    /**
     * 支付
     */
    PAYMENT("支付"),
    
    /**
     * 退款
     */
    REFUND("退款");
    
    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
