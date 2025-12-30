package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【sys_user(系统用户表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}




