package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包表
 * @TableName user_wallet
 */
@TableName(value = "user_wallet")
@Data
public class UserWallet {
    /**
     * 钱包ID
     */
    @TableId(value = "wallet_id", type = IdType.AUTO)
    private Long walletId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    @TableField("frozen_amount")
    private BigDecimal frozenAmount;

    /**
     * 累计充值
     */
    @TableField("total_recharge")
    private BigDecimal totalRecharge;

    /**
     * 累计支出
     */
    @TableField("total_expense")
    private BigDecimal totalExpense;

    /**
     * 状态 0-冻结 1-正常
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
}
