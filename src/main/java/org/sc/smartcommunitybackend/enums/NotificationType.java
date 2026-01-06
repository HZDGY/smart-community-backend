package org.sc.smartcommunitybackend.enums;

import lombok.Getter;

/**
 * 通知类型枚举
 */
@Getter
public enum NotificationType {
    SYSTEM(1, "系统通知"),
    LIKE(2, "点赞通知"),
    COMMENT(3, "评论通知"),
    FRIEND_REQUEST(4, "好友申请"),
    PRIVATE_MESSAGE(5, "私信通知");

    private final Integer code;
    private final String desc;

    NotificationType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static NotificationType fromCode(Integer code) {
        for (NotificationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
