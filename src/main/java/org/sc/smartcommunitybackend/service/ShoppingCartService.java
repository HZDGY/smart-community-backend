package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.AddToCartRequest;
import org.sc.smartcommunitybackend.dto.request.ShoppingCartItemRequest;
import org.sc.smartcommunitybackend.dto.response.ShoppingCartItemVO;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【shopping_cart(购物车表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    void addToCart(AddToCartRequest addToCartRequest);

    void updateCartItemQuantity(ShoppingCartItemRequest shoppingCartItemRequest);

    List<ShoppingCartItemVO> getCartItems();
}
