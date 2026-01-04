package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 访客登记表
 * @TableName visitor_register
 */
@TableName(value ="visitor_register")
@Data
public class VisitorRegister {
    /**
     * 登记ID
     */
    @TableId(type = IdType.AUTO)
    @TableField("register_id")
    private Long registerId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 访客姓名
     */
    @TableField("visitor_name")
    private String visitorName;

    /**
     * 访客电话
     */
    @TableField("visitor_phone")
    private String visitorPhone;

    /**
     * 来访目的
     */
    @TableField("visit_purpose")
    private String visitPurpose;

    /**
     * 放行时间
     */
    @TableField("allow_time")
    private Date allowTime;

    /**
     * 有效日期
     */
    @TableField("valid_date")
    private Date validDate;

    /**
     * 审核状态 0-待审核 1-已通过 2-已拒绝
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;

    /**
     * 审核人ID
     */
    @TableField("audit_user_id")
    private Long auditUserId;

    /**
     * 拒绝原因
     */
    @TableField("reject_reason")
    private String rejectReason;
}