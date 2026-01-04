package org.sc.smartcommunitybackend.controller.mall;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.ShoppingCart;
import org.sc.smartcommunitybackend.dto.request.AddToCartRequest;
import org.sc.smartcommunitybackend.dto.request.ShoppingCartItemRequest;
import org.sc.smartcommunitybackend.dto.response.ShoppingCartItemVO;
import org.sc.smartcommunitybackend.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/mall")
@RestController
@Slf4j
@Tag(name = "购物车管理接口")
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/cart/items")
    public void addToCart(@RequestBody AddToCartRequest addToCartRequest) {
        shoppingCartService.addToCart(addToCartRequest);
    }

    @Operation(summary = "移除购物车商品")
    @DeleteMapping("/cart/items/{cartItemId}")
    public void removeCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeById(cartItemId);
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/cart/items/{cartItemId}/quantity")
    public void updateCartItemQuantity(@PathVariable Long cartItemId, @RequestBody ShoppingCartItemRequest shoppingCartItemRequest) {
        shoppingCartItemRequest.setCartId(cartItemId);
        shoppingCartService.updateCartItemQuantity(shoppingCartItemRequest);
    }
    
    @Operation(summary = "获取购物车商品列表")
    @GetMapping("/cart/items")
    public List<ShoppingCartItemVO> getCartItems() {
         return shoppingCartService.getCartItems();
    }
}
