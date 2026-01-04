package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 社区公告表
 * @TableName community_announcement
 */
@TableName(value ="community_announcement")
@Data
public class CommunityAnnouncement {
    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
    @TableField("announce_id")
    private Long announceId;

    /**
     * 公告标题
     */
    @TableField("title")
    private String title;

    /**
     * 公告内容
     */
    @TableField("content")
    private String content;

    /**
     * 发布人ID
     */
    @TableField("publish_user_id")
    private Long publishUserId;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 阅读次数
     */
    @TableField("read_count")
    private Integer readCount;
}