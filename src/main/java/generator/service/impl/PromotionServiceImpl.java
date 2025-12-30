package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Promotion;
import generator.service.PromotionService;
import generator.mapper.PromotionMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【promotion(促销活动表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion>
    implements PromotionService{

}




