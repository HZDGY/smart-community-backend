package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 购物车表
 * @TableName shopping_cart
 */
@TableName(value ="shopping_cart")
@Data
public class ShoppingCart {
    /**
     * 购物车ID
     */
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Long cart_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 商品ID
     */
    private Long product_id;

    /**
     * 门店ID
     */
    private Long store_id;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}