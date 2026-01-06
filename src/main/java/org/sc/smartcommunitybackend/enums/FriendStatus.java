package org.sc.smartcommunitybackend.enums;

import lombok.Getter;

/**
 * 好友状态枚举
 */
@Getter
public enum FriendStatus {
    PENDING(0, "待确认"),
    ACCEPTED(1, "已同意"),
    REJECTED(2, "已拒绝");

    private final Integer code;
    private final String desc;

    FriendStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FriendStatus fromCode(Integer code) {
        for (FriendStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
