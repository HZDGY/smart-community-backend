package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.StoreProduct;
import generator.service.StoreProductService;
import generator.mapper.StoreProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【store_product(门店商品关联表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class StoreProductServiceImpl extends ServiceImpl<StoreProductMapper, StoreProduct>
    implements StoreProductService{

}




