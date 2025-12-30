package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName order
 */
@TableName(value ="order")
@Data
public class Order {
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long order_id;

    /**
     * 订单编号
     */
    private String order_no;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 订单总金额
     */
    private BigDecimal total_amount;

    /**
     * 订单状态 1-待付款 2-已付款 3-待取货 4-已完成 5-已取消
     */
    private Integer order_status;

    /**
     * 取货门店ID
     */
    private Long store_id;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 支付时间
     */
    private Date pay_time;

    /**
     * 完成时间
     */
    private Date finish_time;
}