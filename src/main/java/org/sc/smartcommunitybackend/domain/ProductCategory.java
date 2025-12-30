package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 商品分类表
 * @TableName product_category
 */
@TableName(value ="product_category")
@Data
public class ProductCategory {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long category_id;

    /**
     * 分类名称
     */
    private String category_name;

    /**
     * 父分类ID 0-一级分类
     */
    private Long parent_id;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}