package org.sc.smartcommunitybackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.StoreProductStatusRequest;
import org.sc.smartcommunitybackend.dto.request.StoreProductStockRequest;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/storeP-products")
@Tag(name = "管理端-门店商品接口")
@Slf4j
public class StoreProductsController {
    @Resource
    private StoreProductService storeProductService;
    @Operation(summary = "门店商品上下架")
    @PutMapping("/{storeProductId}/status")
    public Result<Boolean> updateStoreProductStatus(@PathVariable Long storeProductId, @RequestBody StoreProductStatusRequest  status) {
        boolean success = storeProductService.updateStoreProductStatus(storeProductId, status);
        return Result.success(success);
    }
    @Operation(summary = "修改门店商品库存")
    @PutMapping("/{storeProductId}/stock")
    public Result<Boolean> updateStoreProductStock(@PathVariable Long storeProductId, @RequestBody StoreProductStockRequest  stock) {
        boolean success = storeProductService.updateStoreProductStock(storeProductId, stock);
        return Result.success(success);
    }
}
