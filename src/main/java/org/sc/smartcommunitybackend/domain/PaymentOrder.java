package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;
    
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 订单类型
     */
    @TableField("order_type")
    private String orderType;
    
    /**
     * 订单金额
     */
    @TableField("amount")
    private BigDecimal amount;
    
    /**
     * 支付方式
     */
    @TableField("payment_method")
    private String paymentMethod;
    
    /**
     * 订单状态
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 关联业务ID
     */
    @TableField("related_id")
    private Long relatedId;
    
    /**
     * 第三方订单号
     */
    @TableField("third_party_order_no")
    private String thirdPartyOrderNo;
    
    /**
     * 回调时间
     */
    @TableField("callback_time")
    private Date callbackTime;
    
    /**
     * 过期时间
     */
    @TableField("expire_time")
    private Date expireTime;
    
    /**
     * 订单描述
     */
    @TableField("description")
    private String description;
    
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
}
