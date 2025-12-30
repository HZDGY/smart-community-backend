package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 登录日志表
 * @TableName sys_login_log
 */
@TableName(value ="sys_login_log")
@Data
public class SysLoginLog {
    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    private Long log_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 登录IP
     */
    private String login_ip;

    /**
     * 登录时间
     */
    private Date login_time;

    /**
     * 登录类型 1-用户端 2-商户后台 3-社区后台
     */
    private Integer login_type;
}