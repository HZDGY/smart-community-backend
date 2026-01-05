package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 钱包流水表
 * @TableName wallet_flow
 */
@TableName(value ="wallet_flow")
@Data
public class WalletFlow {
    /**
     * 流水ID
     */
    @TableId(value = "flow_id", type = IdType.AUTO)
    private Long flow_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 金额（正数-收入 负数-支出）
     */
    private BigDecimal amount;

    /**
     * 流水类型 1-充值 2-转账 3-支付 4-退款
     */
    private Integer flow_type;

    /**
     * 关联ID（订单ID/转账ID）
     */
    private Long related_id;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date create_time;
}