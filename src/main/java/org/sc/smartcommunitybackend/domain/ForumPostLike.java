package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 帖子点赞表
 * @TableName forum_post_like
 */
@TableName(value = "forum_post_like")
@Data
public class ForumPostLike {
    /**
     * 点赞ID
     */
    @TableId(value = "like_id", type = IdType.AUTO)
    private Long likeId;

    /**
     * 帖子ID
     */
    @TableField("post_id")
    private Long postId;

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
