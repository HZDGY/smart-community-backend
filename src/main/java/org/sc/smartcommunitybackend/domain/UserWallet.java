package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户钱包表
 * @TableName user_wallet
 */
@TableName(value ="user_wallet")
@Data
public class UserWallet {
    /**
     * 钱包ID
     */
    @TableId(type = IdType.AUTO)
    private Long wallet_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}