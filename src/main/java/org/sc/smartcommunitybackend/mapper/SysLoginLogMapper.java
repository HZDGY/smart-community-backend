package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.SysLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【sys_login_log(登录日志表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.SysLoginLog
*/
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

}




