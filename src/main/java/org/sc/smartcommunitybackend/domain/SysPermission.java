package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统权限表
 * @TableName sys_permission
 */
@TableName(value = "sys_permission")
@Data
public class SysPermission {
    /**
     * 权限ID
     */
    @TableId(type = IdType.AUTO)
    private Long permission_id;

    /**
     * 权限名称
     */
    private String permission_name;

    /**
     * 权限编码（如：user:create）
     */
    private String permission_code;

    /**
     * 资源类型（menu-菜单 button-按钮 api-接口）
     */
    private String resource_type;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
}
