package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 投诉表
 * @TableName complaint
 */
@TableName(value ="complaint")
@Data
public class Complaint {
    /**
     * 投诉ID
     */
    @TableId(type = IdType.AUTO)
    @TableField("complaint_id")
    private Long complaintId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 投诉类型
     */
    @TableField("complaint_type")
    private String complaintType;

    /**
     * 投诉描述
     */
    @TableField("description")
    private String description;

    /**
     * 处理状态 0-待处理 1-处理中 2-已完成 3-已驳回
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private Date handleTime;

    /**
     * 处理人ID
     */
    @TableField("handle_user_id")
    private Long handleUserId;

    /**
     * 处理结果
     */
    @TableField("handle_result")
    private String handleResult;

    /**
     * 投诉位置
     * */
    @TableField("location")
    private String location;
}