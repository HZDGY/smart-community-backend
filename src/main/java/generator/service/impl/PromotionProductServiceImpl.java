package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.PromotionProduct;
import generator.service.PromotionProductService;
import generator.mapper.PromotionProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【promotion_product(促销商品关联表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class PromotionProductServiceImpl extends ServiceImpl<PromotionProductMapper, PromotionProduct>
    implements PromotionProductService{

}




