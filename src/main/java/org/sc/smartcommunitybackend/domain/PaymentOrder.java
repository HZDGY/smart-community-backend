package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单实体
 */
@Data
@TableName("payment_order")
public class PaymentOrder {
    
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long orderId;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单类型
     */
    private String orderType;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 订单状态
     */
    private Integer status;
    
    /**
     * 关联业务ID
     */
    private Long relatedId;
    
    /**
     * 第三方订单号
     */
    private String thirdPartyOrderNo;
    
    /**
     * 回调时间
     */
    private Date callbackTime;
    
    /**
     * 过期时间
     */
    private Date expireTime;
    
    /**
     * 订单描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}
