package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 私信表
 * @TableName private_message
 */
@TableName(value = "private_message")
@Data
public class PrivateMessage {
    /**
     * 消息ID
     */
    @TableId(value = "message_id", type = IdType.AUTO)
    private Long messageId;

    /**
     * 发送者ID
     */
    @TableField("from_user_id")
    private Long fromUserId;

    /**
     * 接收者ID
     */
    @TableField("to_user_id")
    private Long toUserId;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 是否已读 0-未读 1-已读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 状态 0-已删除 1-正常
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 阅读时间
     */
    @TableField("read_time")
    private Date readTime;
}
