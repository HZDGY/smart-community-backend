package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品关联表
 * @TableName order_product
 */
@TableName(value = "order_product")
@Data
public class OrderProduct {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID(关联payment_order.order_id)
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 商品名称（冗余）
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品图片（冗余）
     */
    @TableField("product_image")
    private String productImage;

    /**
     * 取货门店ID
     */
    @TableField("store_id")
    private Long storeId;

    /**
     * 门店名称（冗余）
     */
    @TableField("store_name")
    private String storeName;

    /**
     * 商品数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 商品单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 小计金额
     */
    @TableField("subtotal")
    private BigDecimal subtotal;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}

