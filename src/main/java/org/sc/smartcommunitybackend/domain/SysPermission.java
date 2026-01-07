package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Long permissionId;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限编码（如：user:create）
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 资源类型（menu-菜单 button-按钮 api-接口）
     */
    @TableField("resource_type")
    private String resourceType;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态 0-禁用 1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
