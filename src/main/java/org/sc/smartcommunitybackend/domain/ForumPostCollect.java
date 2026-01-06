package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 帖子收藏表
 * @TableName forum_post_collect
 */
@TableName(value = "forum_post_collect")
@Data
public class ForumPostCollect {
    /**
     * 收藏ID
     */
    @TableId(value = "collect_id", type = IdType.AUTO)
    private Long collectId;

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
