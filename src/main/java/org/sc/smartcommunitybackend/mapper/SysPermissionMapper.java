package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.SysPermission;

/**
 * 权限 Mapper 接口
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
}
