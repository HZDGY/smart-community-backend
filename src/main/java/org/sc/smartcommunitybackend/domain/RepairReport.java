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
    @TableId(type = IdType.AUTO)
    private Long report_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 事项类型
     */
    private String report_type;

    /**
     * 事项描述
     */
    private String description;

    /**
     * 处理状态 0-待处理 1-处理中 2-已完成 3-已驳回
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 处理时间
     */
    private Date handle_time;

    /**
     * 处理人ID
     */
    private Long handle_user_id;

    /**
     * 处理结果
     */
    private String handle_result;
}