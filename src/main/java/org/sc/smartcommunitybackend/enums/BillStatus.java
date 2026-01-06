package org.sc.smartcommunitybackend.enums;

/**
 * 账单状态枚举
 */
public enum BillStatus {
    
    /**
     * 未缴
     */
    UNPAID(0, "未缴"),
    
    /**
     * 已缴
     */
    PAID(1, "已缴"),
    
    /**
     * 部分缴纳
     */
    PARTIAL(2, "部分缴纳");
    
    private final int code;
    private final String description;
    
    BillStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static BillStatus fromCode(int code) {
        for (BillStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNPAID;
    }
}
