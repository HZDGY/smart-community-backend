package org.sc.smartcommunitybackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.annotation.Logical;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.common.annotation.RequireRole;
import org.sc.smartcommunitybackend.dto.request.AdminProductListRequest;
import org.sc.smartcommunitybackend.dto.request.ProductListRequest;
import org.sc.smartcommunitybackend.dto.request.ProductRequest;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.response.*;
import org.sc.smartcommunitybackend.service.ProductCollectService;
import org.sc.smartcommunitybackend.service.ProductService;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("admin-MallController")
@RequestMapping("/admin/products")
@Tag(name = "商品接口")
@Slf4j
public class MallController {
    @Resource
    private ProductService productService;
    @Resource
    private StoreProductService storeProductService;
    @PostMapping("/list")
    @Operation(summary = "商品列表")
    public PageResult<AdminProductVO> list(@RequestBody AdminProductListRequest adminProductListRequest) {
        PageResult<AdminProductVO> productListItemVOPageResult = productService.queryList(adminProductListRequest);
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

//    @RequireRole(value = {"ROLE_SUPER_ADMIN", "ROLE_MERCHANT_ADMIN"}, logical = Logical.OR)
    @RequirePermission("mall:create")
    @Operation(summary = "新增商品")
    @PostMapping
    public void add(@RequestBody ProductRequest productRequest) {
        productService.add(productRequest);
    }
}
