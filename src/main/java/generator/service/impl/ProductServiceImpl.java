package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Product;
import generator.service.ProductService;
import generator.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【product(商品表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




