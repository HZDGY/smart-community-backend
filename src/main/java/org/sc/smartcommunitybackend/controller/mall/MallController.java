package org.sc.smartcommunitybackend.controller.mall;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.Result;
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

import static org.sc.smartcommunitybackend.common.Result.success;

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
    public Result<PageResult<ProductListItemVO>> list(@RequestBody ProductListRequest  productListRequest) {
        PageResult<ProductListItemVO> productListItemVOPageResult = productService.queryList(productListRequest);
        return success(productListItemVOPageResult);
    }

    @Operation(summary = "查询商品详情")
    @GetMapping("/products/{productId}")
    public Result<ProductDetailVO> detail(@PathVariable Long productId) {
        ProductDetailVO productListItemVO = productService.detail(productId);
        return success(productListItemVO);
    }

    @Operation(summary = "查询商品可自提门店列表")
    @PostMapping("/stores")
    public Result<List<StoreVO>> stores(@RequestBody StoreListRequest storeListRequest) {
        return success(storeProductService.queryList(storeListRequest));
    }

    @Operation(summary = "收藏商品")
    @PostMapping("/products/{productId}/collect")
    public Result collect(@PathVariable Long productId) {
        productCollectService.collect(productId);
        return success();
    }
    @Operation(summary = "取消收藏商品")
    @DeleteMapping("/products/{productId}/collect")
    public Result cancelCollect(@PathVariable Long productId) {
        productCollectService.cancelCollect(productId);
        return success();
    }
}
