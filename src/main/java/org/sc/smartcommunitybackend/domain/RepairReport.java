package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 报事维修表
 * @TableName repair_report
 */
@TableName(value ="repair_report")
@Data
public class RepairReport {
    /**
     * 报事ID
     */
    @TableId(value = "report_id", type = IdType.AUTO)
    private Long reportId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 事项类型
     */
    @TableField("report_type")
    private String reportType;

    /**
     * 事项描述
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
     * 报修地点
     * */
    @TableField("location")
    private String location;
}