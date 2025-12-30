package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.ServiceArea;
import generator.service.ServiceAreaService;
import generator.mapper.ServiceAreaMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【service_area(服务区域表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ServiceAreaServiceImpl extends ServiceImpl<ServiceAreaMapper, ServiceArea>
    implements ServiceAreaService{

}




