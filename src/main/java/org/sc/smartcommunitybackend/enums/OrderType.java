package org.sc.smartcommunitybackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderType {
    
    RECHARGE("RECHARGE", "充值"),
    PROPERTY_FEE("PROPERTY_FEE", "物业费");
    
    private final String code;
    private final String description;
}
