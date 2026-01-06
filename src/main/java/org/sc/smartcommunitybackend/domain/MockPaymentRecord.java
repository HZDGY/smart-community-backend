package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
    @TableId(type = IdType.AUTO)
    private Long recordId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 模拟结果
     */
    private Integer mockResult;
    
    /**
     * 模拟消息
     */
    private String mockMessage;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
