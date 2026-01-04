package org.sc.smartcommunitybackend.constant;

/**
 * 投诉常量
 */
public class ComplaintConstant {
    
    /**
     * 处理状态
     */
    public static final int STATUS_PENDING = 0;      // 待处理
    public static final int STATUS_PROCESSING = 1;   // 处理中
    public static final int STATUS_COMPLETED = 2;    // 已完成
    public static final int STATUS_REJECTED = 3;     // 已驳回
    
    /**
     * 投诉类型
     */
    public static final String TYPE_NOISE = "噪音扰民";
    public static final String TYPE_PARKING = "违规停车";
    public static final String TYPE_GARBAGE = "垃圾处理";
    public static final String TYPE_PROPERTY = "物业服务";
    public static final String TYPE_SECURITY = "安全问题";
    public static final String TYPE_FACILITY = "设施损坏";
    public static final String TYPE_OTHER = "其他";
}

