package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包交易记录表
 */
@TableName(value = "wallet_transaction")
@Data
public class WalletTransaction {
    
    /**
     * 交易ID
     */
    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Long transactionId;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 交易流水号
     */
    @TableField("transaction_no")
    private String transactionNo;
    
    /**
     * 交易类型
     */
    @TableField("transaction_type")
    private String transactionType;
    
    /**
     * 交易金额
     */
    @TableField("amount")
    private BigDecimal amount;
    
    /**
     * 交易前余额
     */
    @TableField("balance_before")
    private BigDecimal balanceBefore;
    
    /**
     * 交易后余额
     */
    @TableField("balance_after")
    private BigDecimal balanceAfter;
    
    /**
     * 关联用户ID（转账时使用）
     */
    @TableField("related_user_id")
    private Long relatedUserId;
    
    /**
     * 关联订单号
     */
    @TableField("related_order_no")
    private String relatedOrderNo;
    
    /**
     * 交易描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 交易时间
     */
    @TableField("create_time")
    private Date createTime;
}
