package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.ParkingSpace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【parking_space(车位信息表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.ParkingSpace
*/
@Mapper
public interface ParkingSpaceMapper extends BaseMapper<ParkingSpace> {

}




