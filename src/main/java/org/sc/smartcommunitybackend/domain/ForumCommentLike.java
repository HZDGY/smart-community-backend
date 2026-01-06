package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 评论点赞表
 * @TableName forum_comment_like
 */
@TableName(value = "forum_comment_like")
@Data
public class ForumCommentLike {
    /**
     * 点赞ID
     */
    @TableId(value = "like_id", type = IdType.AUTO)
    private Long likeId;

    /**
     * 评论ID
     */
    @TableField("comment_id")
    private Long commentId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
