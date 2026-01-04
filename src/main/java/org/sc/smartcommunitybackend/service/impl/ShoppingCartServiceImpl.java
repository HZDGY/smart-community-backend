package org.sc.smartcommunitybackend.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.ShoppingCart;
import org.sc.smartcommunitybackend.domain.Store;
import org.sc.smartcommunitybackend.domain.StoreProduct;
import org.sc.smartcommunitybackend.dto.request.AddToCartRequest;
import org.sc.smartcommunitybackend.dto.request.ShoppingCartItemRequest;
import org.sc.smartcommunitybackend.dto.response.ShoppingCartItemVO;
import org.sc.smartcommunitybackend.service.ProductService;
import org.sc.smartcommunitybackend.service.ShoppingCartService;
import org.sc.smartcommunitybackend.mapper.ShoppingCartMapper;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.sc.smartcommunitybackend.service.StoreService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 吴展德
* @description 针对表【shopping_cart(购物车表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{
    @Resource
    private StoreProductService storeProductService;
    @Resource
    private ProductService productService;
    @Resource
    private StoreService storeService;



    /**
     * 添加商品到购物车
     *
     * @param addToCartRequest
     */
    @Override
    public void addToCart(AddToCartRequest addToCartRequest) {
        log.info("添加商品到购物车：{}", addToCartRequest);
        if (addToCartRequest == null) {
            throw new RuntimeException("参数不能为空");
        }
        Long productId = addToCartRequest.getProductId();
        Long storeId = addToCartRequest.getStoreId();
        Integer quantity = addToCartRequest.getQuantity();
        if (productId == null || storeId == null || quantity == null) {
            throw new RuntimeException("参数不能为空");
        }
        if (quantity <= 0) {
            throw new RuntimeException("商品数量不能小于0");
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        Long currentUserId = UserContextUtil.getCurrentUserId();
        shoppingCart.setUser_id(currentUserId);
        shoppingCart.setProduct_id(productId);
        shoppingCart.setStore_id(storeId);
        shoppingCart.setQuantity(quantity);
        shoppingCart.setCreate_time(DateTime.now());
        int insert = baseMapper.insert(shoppingCart);
        if (insert <= 0) {
            throw new RuntimeException("添加商品到购物车失败");
        }
    }

    /**
     * 更新购物车商品数量
     *
     * @param shoppingCartItemRequest
     */
    @Override
    public void updateCartItemQuantity(ShoppingCartItemRequest shoppingCartItemRequest) {
        log.info("更新购物车商品数量：{}", shoppingCartItemRequest);
        if (shoppingCartItemRequest == null) {
            throw new RuntimeException("参数不能为空");
        }
        Long cartId = shoppingCartItemRequest.getCartId();
        Long productId = shoppingCartItemRequest.getProductId();
        Integer quantity = shoppingCartItemRequest.getQuantity();
        String operation = shoppingCartItemRequest.getOperation();
        if (cartId == null || productId == null || quantity == null) {
            throw new RuntimeException("参数不能为空");
        }
        operation = operation == null ? "update" : operation;
        boolean update = lambdaUpdate().eq(ShoppingCart::getCart_id, cartId)
                .eq(ShoppingCart::getProduct_id, productId)
                .set(ShoppingCart::getQuantity, quantity)
                .update();
        if (!update) {
            throw new RuntimeException("更新购物车商品数量失败");
        }
    }

    /**
     * 获取购物车商品列表
     */
    @Override
    public List<ShoppingCartItemVO> getCartItems() {
        // 获取当前登录用户ID
        Long currentUserId = UserContextUtil.getCurrentUserId();
        
        // 检查用户是否已登录，如果未登录直接返回空列表
        if (currentUserId == null) {
            log.info("用户未登录，购物车为空");
            return new ArrayList<>();
        }
        
        // 只查询当前用户的购物车数据
        List<ShoppingCart> shoppingCarts = lambdaQuery()
                .eq(ShoppingCart::getUser_id, currentUserId)
                .list();

        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            return new ArrayList<>();
        }
        
        log.info("获取购物车商品列表：{}", shoppingCarts);
        
        // 转换成购物车项列表，添加空值检查
        return shoppingCarts.stream()
            .filter(shoppingCart -> shoppingCart.getProduct_id() != null && shoppingCart.getStore_id() != null) // 过滤掉关键字段为null的记录
            .map(shoppingCart -> {
                // 获取商品信息
                Product product = productService.getById(shoppingCart.getProduct_id());
                if (product == null) {
                    log.warn("商品不存在：productId={}", shoppingCart.getProduct_id());
                    return null;
                }
                
                // 获取门店信息
                Store store = storeService.getById(shoppingCart.getStore_id());
                if (store == null) {
                    log.warn("门店不存在：storeId={}", shoppingCart.getStore_id());
                    return null;
                }
                
                // 获取门店商品信息
                StoreProduct storeProduct = storeProductService.getOne(
                    new QueryWrapper<StoreProduct>()
                        .eq("store_id", store.getStore_id())
                        .eq("product_id", product.getProduct_id())
                );
                if (storeProduct == null) {
                    log.warn("门店商品不存在：storeId={}, productId={}", store.getStore_id(), product.getProduct_id());
                    return null;
                }
                
                // 构建响应对象
                ShoppingCartItemVO shoppingCartItemVO = new ShoppingCartItemVO();
                shoppingCartItemVO.setCartId(shoppingCart.getCart_id());
                shoppingCartItemVO.setProductId(shoppingCart.getProduct_id());
                shoppingCartItemVO.setStoreId(shoppingCart.getStore_id());
                shoppingCartItemVO.setQuantity(shoppingCart.getQuantity());
                shoppingCartItemVO.setProductName(product.getProduct_name());
                shoppingCartItemVO.setPrice(product.getPrice());
                shoppingCartItemVO.setCoverImg(product.getCover_img());
                shoppingCartItemVO.setStoreName(store.getStore_name());
                shoppingCartItemVO.setSubtotal(product.getPrice().multiply(new BigDecimal(shoppingCart.getQuantity())));
                shoppingCartItemVO.setStock(storeProduct.getStock());
                
                return shoppingCartItemVO;
            })
            .filter(Objects::nonNull) // 过滤掉转换失败的项
            .collect(Collectors.toList());
    }
}




