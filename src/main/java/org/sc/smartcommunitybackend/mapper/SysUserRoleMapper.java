package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sc.smartcommunitybackend.domain.SysUserRole;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.SysUserRole
*/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    
    /**
     * 根据用户ID查询所有权限编码
     */
    @Select("SELECT DISTINCT p.permission_code FROM sys_permission p " +
            "INNER JOIN sys_role_permission rp ON p.permission_id = rp.permission_id " +
            "INNER JOIN sys_role r ON rp.role_id = r.role_id " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.status = 1 AND r.status = 1")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询所有角色编码
     */
    @Select("SELECT DISTINCT r.role_code FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
}





