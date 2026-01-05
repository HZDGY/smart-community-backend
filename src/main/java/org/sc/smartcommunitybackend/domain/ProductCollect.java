package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 商品收藏表
 * @TableName product_collect
 */
@TableName(value ="product_collect")
@Data
public class ProductCollect {
    /**
     * 收藏ID
     */
    @TableId(value = "collect_id", type = IdType.AUTO)
    private Long collect_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 商品ID
     */
    private Long product_id;

    /**
     * 收藏时间
     */
    private Date create_time;
}