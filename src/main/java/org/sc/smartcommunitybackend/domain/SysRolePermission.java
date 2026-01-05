package org.sc.smartcommunitybackend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关联表
 * @TableName sys_role_permission
 */
@TableName(value = "sys_role_permission")
@Data
public class SysRolePermission {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    private Long role_id;

    /**
     * 权限ID
     */
    private Long permission_id;
}
