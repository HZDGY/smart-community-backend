package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品表
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product {
    /**
     * 商品ID
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long product_id;

    /**
     * 商品名称
     */
    private String product_name;

    /**
     * 分类ID
     */
    private Long category_id;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 封面图片
     */
    private String cover_img;

    /**
     * 状态 0-下架 1-上架
     */
    private Integer status;
    /**
     * 库存
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}