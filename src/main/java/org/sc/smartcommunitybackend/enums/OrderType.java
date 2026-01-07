package org.sc.smartcommunitybackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 */
@Getter
@AllArgsConstructor
public enum OrderType {
    
    PRODUCT("PRODUCT", "商品订单"),
    RECHARGE("RECHARGE", "充值"),
    PROPERTY_FEE("PROPERTY_FEE", "物业费"),
    PARKING_FEE("PARKING_FEE", "停车费"),
    SERVICE_FEE("SERVICE_FEE", "服务费"),
    OTHER("OTHER", "其他");
    
    private final String code;
    private final String description;
}
