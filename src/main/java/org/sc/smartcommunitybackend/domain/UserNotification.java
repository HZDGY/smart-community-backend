package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户通知表
 * @TableName user_notification
 */
@TableName(value = "user_notification")
@Data
public class UserNotification {
    /**
     * 通知ID
     */
    @TableId(value = "notification_id", type = IdType.AUTO)
    private Long notificationId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 通知类型 1-系统通知 2-点赞通知 3-评论通知 4-好友申请 5-私信通知
     */
    @TableField("type")
    private Integer type;

    /**
     * 通知标题
     */
    @TableField("title")
    private String title;

    /**
     * 通知内容
     */
    @TableField("content")
    private String content;

    /**
     * 关联ID（如帖子ID、评论ID等）
     */
    @TableField("related_id")
    private Long relatedId;

    /**
     * 是否已读 0-未读 1-已读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
