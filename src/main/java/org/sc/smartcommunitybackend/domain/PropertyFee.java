package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 物业费缴费表
 * @TableName property_fee
 */
@TableName(value ="property_fee")
@Data
public class PropertyFee {
    /**
     * 缴费ID
     */
    @TableId(value = "fee_id", type = IdType.AUTO)
    private Long fee_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 缴费金额
     */
    private BigDecimal fee_amount;

    /**
     * 支付状态 0-待支付 1-已支付
     */
    private Integer pay_status;

    /**
     * 支付时间
     */
    private Date pay_time;

    /**
     * 关联订单ID
     */
    private Long order_id;

    /**
     * 创建时间
     */
    private Date create_time;
}