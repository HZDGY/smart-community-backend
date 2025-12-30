package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 促销商品关联表
 * @TableName promotion_product
 */
@TableName(value ="promotion_product")
@Data
public class PromotionProduct {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 促销ID
     */
    private Long promotion_id;

    /**
     * 商品ID
     */
    private Long product_id;
}