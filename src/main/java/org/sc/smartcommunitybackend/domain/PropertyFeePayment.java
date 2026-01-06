package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物业费缴纳记录表
 */
@TableName(value = "property_fee_payment")
@Data
public class PropertyFeePayment {
    
    /**
     * 缴费ID
     */
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Long paymentId;
    
    /**
     * 缴费流水号
     */
    @TableField("payment_no")
    private String paymentNo;
    
    /**
     * 账单ID
     */
    @TableField("bill_id")
    private Long billId;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 缴费金额
     */
    @TableField("amount")
    private BigDecimal amount;
    
    /**
     * 支付方式
     */
    @TableField("payment_method")
    private String paymentMethod;
    
    /**
     * 关联钱包交易ID
     */
    @TableField("transaction_id")
    private Long transactionId;
    
    /**
     * 状态 0-失败 1-成功 2-退款
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 缴费时间
     */
    @TableField("create_time")
    private Date createTime;
}
