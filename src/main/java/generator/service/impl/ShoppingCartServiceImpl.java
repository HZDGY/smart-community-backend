package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.ShoppingCart;
import generator.service.ShoppingCartService;
import generator.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【shopping_cart(购物车表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




