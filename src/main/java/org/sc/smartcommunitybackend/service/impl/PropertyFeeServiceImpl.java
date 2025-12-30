package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.PropertyFee;
import org.sc.smartcommunitybackend.service.PropertyFeeService;
import org.sc.smartcommunitybackend.mapper.PropertyFeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【property_fee(物业费缴费表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class PropertyFeeServiceImpl extends ServiceImpl<PropertyFeeMapper, PropertyFee>
    implements PropertyFeeService{

}




