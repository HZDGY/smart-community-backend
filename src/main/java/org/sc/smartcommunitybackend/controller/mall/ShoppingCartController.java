package org.sc.smartcommunitybackend.controller.mall;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.domain.ShoppingCart;
import org.sc.smartcommunitybackend.dto.request.AddToCartRequest;
import org.sc.smartcommunitybackend.dto.request.ShoppingCartItemRequest;
import org.sc.smartcommunitybackend.dto.response.ShoppingCartItemVO;
import org.sc.smartcommunitybackend.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.sc.smartcommunitybackend.common.Result.success;

@RequestMapping("/mall")
@RestController
@Slf4j
@Tag(name = "购物车管理接口")
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/cart/items")
    public Result addToCart(@RequestBody AddToCartRequest addToCartRequest) {
        shoppingCartService.addToCart(addToCartRequest);
        return success();
    }

    @Operation(summary = "移除购物车商品")
    @DeleteMapping("/cart/items/{cartItemId}")
    public Result removeCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeById(cartItemId);
        return success();
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/cart/items/{cartItemId}/quantity")
    public Result updateCartItemQuantity(@PathVariable Long cartItemId, @RequestBody ShoppingCartItemRequest shoppingCartItemRequest) {
        shoppingCartItemRequest.setCartId(cartItemId);
        shoppingCartService.updateCartItemQuantity(shoppingCartItemRequest);
        return success();
    }
    
    @Operation(summary = "获取购物车商品列表")
    @GetMapping("/cart/items")
    public Result< List<ShoppingCartItemVO>> getCartItems() {
         return success(shoppingCartService.getCartItems());
    }
}
