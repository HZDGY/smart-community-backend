package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 论坛帖子表
 * @TableName forum_post
 */
@TableName(value = "forum_post")
@Data
public class ForumPost {
    /**
     * 帖子ID
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    /**
     * 板块ID
     */
    @TableField("section_id")
    private Long sectionId;

    /**
     * 发帖用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 帖子标题
     */
    @TableField("title")
    private String title;

    /**
     * 帖子内容
     */
    @TableField("content")
    private String content;

    /**
     * 图片URL列表，逗号分隔
     */
    @TableField("images")
    private String images;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 收藏数
     */
    @TableField("collect_count")
    private Integer collectCount;

    /**
     * 是否置顶 0-否 1-是
     */
    @TableField("is_top")
    private Integer isTop;

    /**
     * 是否精华 0-否 1-是
     */
    @TableField("is_essence")
    private Integer isEssence;

    /**
     * 状态 0-已删除 1-正常 2-待审核
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
