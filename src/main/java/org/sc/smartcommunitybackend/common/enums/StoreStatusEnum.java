package org.sc.smartcommunitybackend.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 门店状态枚举
 */
@Getter
@AllArgsConstructor
public enum StoreStatusEnum {

    /**
     * 关闭
     */
    CLOSED(0, "关闭"),

    /**
     * 正常营业
     */
    NORMAL(1, "正常营业");

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
     * @return 门店状态枚举
     */
    public static StoreStatusEnum getByCode(Integer code) {
        for (StoreStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}