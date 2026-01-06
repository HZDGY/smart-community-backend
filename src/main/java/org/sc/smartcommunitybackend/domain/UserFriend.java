package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户好友表
 * @TableName user_friend
 */
@TableName(value = "user_friend")
@Data
public class UserFriend {
    /**
     * 好友关系ID
     */
    @TableId(value = "friend_id", type = IdType.AUTO)
    private Long friendId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 好友用户ID
     */
    @TableField("friend_user_id")
    private Long friendUserId;

    /**
     * 好友备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态 0-待确认 1-已同意 2-已拒绝
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
