package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 论坛板块表
 * @TableName forum_section
 */
@TableName(value = "forum_section")
@Data
public class ForumSection {
    /**
     * 板块ID
     */
    @TableId(value = "section_id", type = IdType.AUTO)
    private Long sectionId;

    /**
     * 板块名称
     */
    @TableField("section_name")
    private String sectionName;

    /**
     * 板块描述
     */
    @TableField("section_desc")
    private String sectionDesc;

    /**
     * 板块图标URL
     */
    @TableField("icon_url")
    private String iconUrl;

    /**
     * 排序序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 帖子数量
     */
    @TableField("post_count")
    private Integer postCount;

    /**
     * 状态 0-禁用 1-启用
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
