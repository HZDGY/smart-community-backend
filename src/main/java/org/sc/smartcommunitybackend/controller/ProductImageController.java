package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.domain.ProductImage;
import org.sc.smartcommunitybackend.dto.request.UpdateImageOrderRequest;
import org.sc.smartcommunitybackend.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品图片管理控制器
 */
@RestController
@RequestMapping("/product")
@Tag(name = "商品图片管理", description = "商品图片上传、删除、排序等接口")
public class ProductImageController extends BaseController {
    
    @Autowired
    private ProductImageService productImageService;
    
    @PostMapping("/{productId}/images")
    @Operation(summary = "上传商品图片", description = "上传商品图片，支持设置为主图")
    public Result<ProductImage> uploadImage(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Long productId,
            @Parameter(description = "图片文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "是否设为主图")
            @RequestParam(value = "isMain", required = false, defaultValue = "false") Boolean isMain) {
        
        ProductImage image = productImageService.uploadImage(productId, file, isMain);
        return success("上传成功", image);
    }
    
    @GetMapping("/{productId}/images")
    @Operation(summary = "获取商品图片列表", description = "查询指定商品的所有图片，按排序顺序返回")
    public Result<List<ProductImage>> getProductImages(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Long productId) {
        
        List<ProductImage> images = productImageService.getProductImages(productId);
        return success(images);
    }
    
    @PutMapping("/images/{imageId}/set-main")
    @Operation(summary = "设置主图", description = "将指定图片设置为商品主图（封面）")
    public Result<Void> setMainImage(
            @Parameter(description = "图片ID", required = true)
            @PathVariable Long imageId) {
        
        productImageService.setMainImage(imageId);
        return success();
    }
    
    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "删除图片", description = "删除指定的商品图片")
    public Result<Void> deleteImage(
            @Parameter(description = "图片ID", required = true)
            @PathVariable Long imageId) {
        
        productImageService.deleteImage(imageId);
        return success();
    }
    
    @PutMapping("/{productId}/images/sort")
    @Operation(summary = "调整图片排序", description = "批量更新商品图片的显示顺序")
    public Result<Void> updateImageOrder(
            @Parameter(description = "商品ID", required = true)
            @PathVariable Long productId,
            @Parameter(description = "图片排序数据", required = true)
            @RequestBody UpdateImageOrderRequest request) {
        
        // 转换为 Map
        Map<Long, Integer> imageOrders = new HashMap<>();
        for (UpdateImageOrderRequest.ImageOrder order : request.getImageOrders()) {
            imageOrders.put(order.getImageId(), order.getSortOrder());
        }
        
        productImageService.updateImageOrder(productId, imageOrders);
        return success();
    }
    
    @GetMapping("/images/{imageId}")
    @Operation(summary = "获取图片详情", description = "查询指定图片的详细信息")
    public Result<ProductImage> getImageDetail(
            @Parameter(description = "图片ID", required = true)
            @PathVariable Long imageId) {
        
        ProductImage image = productImageService.getImageById(imageId);
        return success(image);
    }
}
