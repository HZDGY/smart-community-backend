package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 投诉表
 * @TableName complaint
 */
@TableName(value ="complaint")
@Data
public class Complaint {
    /**
     * 投诉ID
     */
    @TableId(type = IdType.AUTO)
    private Long complaint_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 投诉类型
     */
    private String complaint_type;

    /**
     * 投诉描述
     */
    private String description;

    /**
     * 处理状态 0-待处理 1-处理中 2-已完成 3-已驳回
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 处理时间
     */
    private Date handle_time;

    /**
     * 处理人ID
     */
    private Long handle_user_id;

    /**
     * 处理结果
     */
    private String handle_result;
}