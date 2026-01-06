package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 模拟支付记录实体
 */
@Data
@TableName("mock_payment_record")
public class MockPaymentRecord {
    
    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;
    
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 支付方式
     */
    @TableField("payment_method")
    private String paymentMethod;
    
    /**
     * 支付金额
     */
    @TableField("amount")
    private BigDecimal amount;
    
    /**
     * 模拟结果
     */
    @TableField("mock_result")
    private Integer mockResult;
    
    /**
     * 模拟消息
     */
    @TableField("mock_message")
    private String mockMessage;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
