package org.sc.smartcommunitybackend.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 促销类型枚举
 */
@Getter
@AllArgsConstructor
public enum PromotionTypeEnum {

    /**
     * 明星商品
     */
    STAR(1, "明星商品"),

    /**
     * 秒杀商品
     */
    SECKILL(2, "秒杀商品");

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型名称
     */
    private final String name;

    /**
     * 根据编码获取枚举
     * @param code 类型编码
     * @return 促销类型枚举
     */
    public static PromotionTypeEnum getByCode(Integer code) {
        for (PromotionTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}