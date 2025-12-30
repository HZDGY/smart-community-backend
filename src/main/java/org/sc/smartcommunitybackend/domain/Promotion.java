package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 促销活动表
 * @TableName promotion
 */
@TableName(value ="promotion")
@Data
public class Promotion {
    /**
     * 促销ID
     */
    @TableId(type = IdType.AUTO)
    private Long promotion_id;

    /**
     * 促销名称
     */
    private String promotion_name;

    /**
     * 促销类型 1-明星商品 2-秒杀商品
     */
    private Integer promotion_type;

    /**
     * 开始时间
     */
    private Date start_time;

    /**
     * 结束时间
     */
    private Date end_time;

    /**
     * 状态 0-失效 1-有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}