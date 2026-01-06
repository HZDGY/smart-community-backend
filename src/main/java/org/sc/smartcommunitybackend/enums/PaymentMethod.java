package org.sc.smartcommunitybackend.enums;

/**
 * 支付方式枚举
 */
public enum PaymentMethod {
    
    /**
     * 钱包支付
     */
    WALLET("钱包"),
    
    /**
     * 支付宝
     */
    ALIPAY("支付宝"),
    
    /**
     * 微信支付
     */
    WECHAT("微信");
    
    private final String description;
    
    PaymentMethod(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
