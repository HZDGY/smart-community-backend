package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 角色表
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class SysRole {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long role_id;

    /**
     * 角色名称
     */
    private String role_name;

    /**
     * 角色描述
     */
    private String role_desc;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}