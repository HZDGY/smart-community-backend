package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物业费账单表
 */
@TableName(value = "property_fee_bill")
@Data
public class PropertyFeeBill {
    
    /**
     * 账单ID
     */
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Long billId;
    
    /**
     * 账单编号
     */
    @TableField("bill_no")
    private String billNo;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 账期（如：2026-01）
     */
    @TableField("billing_period")
    private String billingPeriod;
    
    /**
     * 物业费
     */
    @TableField("property_fee")
    private BigDecimal propertyFee;
    
    /**
     * 水费
     */
    @TableField("water_fee")
    private BigDecimal waterFee;
    
    /**
     * 电费
     */
    @TableField("electricity_fee")
    private BigDecimal electricityFee;
    
    /**
     * 燃气费
     */
    @TableField("gas_fee")
    private BigDecimal gasFee;
    
    /**
     * 停车费
     */
    @TableField("parking_fee")
    private BigDecimal parkingFee;
    
    /**
     * 其他费用
     */
    @TableField("other_fee")
    private BigDecimal otherFee;
    
    /**
     * 总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    /**
     * 已缴金额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;
    
    /**
     * 状态 0-未缴 1-已缴 2-部分缴纳
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 缴费截止日期
     */
    @TableField("due_date")
    private Date dueDate;
    
    /**
     * 缴费时间
     */
    @TableField("paid_time")
    private Date paidTime;
    
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
