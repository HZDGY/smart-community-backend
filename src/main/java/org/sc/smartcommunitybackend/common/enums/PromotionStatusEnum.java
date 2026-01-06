package org.sc.smartcommunitybackend.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 促销状态枚举
 */
@Getter
@AllArgsConstructor
public enum PromotionStatusEnum {

    /**
     * 失效
     */
    INVALID(0, "失效"),

    /**
     * 有效
     */
    VALID(1, "有效");

    /**
     * 状态编码
     */
    private final Integer code;

    /**
     * 状态名称
     */
    private final String name;

    /**
     * 根据编码获取枚举
     *
     * @param code 状态编码
     * @return 促销状态枚举
     */
    public static PromotionStatusEnum getByCode(Integer code) {
        for (PromotionStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}