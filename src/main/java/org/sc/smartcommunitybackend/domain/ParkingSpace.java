package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 车位信息表
 * @TableName parking_space
 */
@TableName(value ="parking_space")
@Data
public class ParkingSpace {
    /**
     * 车位ID
     */
    @TableId(type = IdType.AUTO)
    @TableField("space_id")
    private Long spaceId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 车位编号
     */
    @TableField("space_no")
    private String spaceNo;

    /**
     * 绑定车牌号
     */
    @TableField("car_number")
    private String carNumber;

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
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

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