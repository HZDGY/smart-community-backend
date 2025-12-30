package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.PropertyFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【property_fee(物业费缴费表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.PropertyFee
*/
@Mapper
public interface PropertyFeeMapper extends BaseMapper<PropertyFee> {

}




