package org.sc.smartcommunitybackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum PaymentOrderStatus {
    
    PENDING(0, "待支付"),
    PAYING(1, "支付中"),
    SUCCESS(2, "支付成功"),
    FAILED(3, "支付失败"),
    CANCELLED(4, "已取消");
    
    private final Integer code;
    private final String description;
    
    public static PaymentOrderStatus fromCode(Integer code) {
        for (PaymentOrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
