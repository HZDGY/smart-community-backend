package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 访客登记表
 * @TableName visitor_register
 */
@TableName(value ="visitor_register")
@Data
public class VisitorRegister {
    /**
     * 登记ID
     */
    @TableId(type = IdType.AUTO)
    private Long register_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 来访目的
     */
    private String visit_purpose;

    /**
     * 放行时间
     */
    private Date allow_time;

    /**
     * 有效日期
     */
    private Date valid_date;

    /**
     * 审核状态 0-待审核 1-已通过 2-已拒绝
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 审核时间
     */
    private Date audit_time;

    /**
     * 审核人ID
     */
    private Long audit_user_id;
}