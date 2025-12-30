package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.SysLoginLog;
import generator.service.SysLoginLogService;
import generator.mapper.SysLoginLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【sys_login_log(登录日志表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog>
    implements SysLoginLogService{

}




