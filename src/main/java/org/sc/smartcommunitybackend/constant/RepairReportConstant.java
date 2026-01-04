package org.sc.smartcommunitybackend.constant;

/**
 * 报事维修常量
 */
public class RepairReportConstant {
    
    /**
     * 处理状态
     */
    public static final int STATUS_PENDING = 0;      // 待处理
    public static final int STATUS_PROCESSING = 1;   // 处理中
    public static final int STATUS_COMPLETED = 2;    // 已完成
    public static final int STATUS_REJECTED = 3;     // 已驳回
    
    /**
     * 事项类型
     */
    public static final String TYPE_WATER = "水电维修";
    public static final String TYPE_ELEVATOR = "电梯故障";
    public static final String TYPE_DOOR = "门窗维修";
    public static final String TYPE_NETWORK = "网络故障";
    public static final String TYPE_HEATING = "供暖问题";
    public static final String TYPE_CLEANING = "环境卫生";
    public static final String TYPE_OTHER = "其他";
}

