package org.sc.smartcommunitybackend.controller.mall;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.dto.request.ProductListRequest;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductDetailVO;
import org.sc.smartcommunitybackend.dto.response.ProductListItemVO;
import org.sc.smartcommunitybackend.dto.response.StoreVO;
import org.sc.smartcommunitybackend.service.ProductCollectService;
import org.sc.smartcommunitybackend.service.ProductService;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.sc.smartcommunitybackend.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mall")
@Tag(name = "商品接口")
@Slf4j
public class MallController {
    @Resource
    private ProductService productService;
    @Resource
    private StoreProductService storeProductService;
    @Resource
    private ProductCollectService productCollectService;
    @PostMapping("/list")
    @Operation(summary = "商品列表")
    public PageResult<ProductListItemVO> list(@RequestBody ProductListRequest  productListRequest) {
        PageResult<ProductListItemVO> productListItemVOPageResult = productService.queryList(productListRequest);
        return productListItemVOPageResult;
    }

    @Operation(summary = "查询商品详情")
    @GetMapping("/products/{productId}")
    public ProductDetailVO detail(@PathVariable Long productId) {
        ProductDetailVO productListItemVO = productService.detail(productId);
        return productListItemVO;
    }

    @Operation(summary = "查询商品可自提门店列表")
    @PostMapping("/stores")
    public List<StoreVO> stores(@RequestBody StoreListRequest storeListRequest) {
        return storeProductService.queryList(storeListRequest);
    }

    @Operation(summary = "收藏商品")
    @PostMapping("/products/{productId}/collect")
    public void collect(@PathVariable Long productId) {
        productCollectService.collect(productId);
    }
    @Operation(summary = "取消收藏商品")
    @DeleteMapping("/products/{productId}/collect")
    public void cancelCollect(@PathVariable Long productId) {
        productCollectService.cancelCollect(productId);
    }
}
