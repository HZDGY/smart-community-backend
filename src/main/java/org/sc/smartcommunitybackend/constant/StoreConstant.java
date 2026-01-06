package org.sc.smartcommunitybackend.constant;

import org.sc.smartcommunitybackend.common.enums.StoreStatusEnum;

/**
 * 门店相关常量
 */
public class StoreConstant {

    /**
     * 门店状态 - 关闭
     */
    public static final Integer STATUS_CLOSED = StoreStatusEnum.CLOSED.getCode();

    /**
     * 门店状态 - 正常
     */
    public static final Integer STATUS_NORMAL = StoreStatusEnum.NORMAL.getCode();

    /**
     * 门店状态字符串 - 关闭
     */
    public static final String STATUS_STR_CLOSED = "CLOSED";

    /**
     * 门店状态字符串 - 正常
     */
    public static final String STATUS_STR_NORMAL = "NORMAL";
}