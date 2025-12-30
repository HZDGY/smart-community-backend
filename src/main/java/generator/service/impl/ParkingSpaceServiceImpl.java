package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.ParkingSpace;
import generator.service.ParkingSpaceService;
import generator.mapper.ParkingSpaceMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【parking_space(车位信息表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ParkingSpaceServiceImpl extends ServiceImpl<ParkingSpaceMapper, ParkingSpace>
    implements ParkingSpaceService{

}




