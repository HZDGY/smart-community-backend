package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.PropertyFee;
import generator.service.PropertyFeeService;
import generator.mapper.PropertyFeeMapper;
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




