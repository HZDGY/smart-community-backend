package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 论坛评论表
 * @TableName forum_comment
 */
@TableName(value = "forum_comment")
@Data
public class ForumComment {
    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 帖子ID
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 评论用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 父评论ID，0表示一级评论
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 回复的用户ID
     */
    @TableField("reply_to_user_id")
    private Long replyToUserId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

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
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
