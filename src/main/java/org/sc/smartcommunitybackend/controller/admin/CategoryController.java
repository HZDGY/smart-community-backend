package org.sc.smartcommunitybackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.annotation.RequirePermission;
import org.sc.smartcommunitybackend.dto.request.PageQueryDTO;
import org.sc.smartcommunitybackend.dto.request.ProductCategoryRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductCategoryVO;
import org.sc.smartcommunitybackend.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("AdminCategoryController")
@RequestMapping("/admin/product-categories")
@Tag(name = "管理端-商品分类管理")
@Slf4j
public class CategoryController {
    @Resource
    private ProductCategoryService productCategoryService;

    @Operation(summary = "商品分类列表")
    @PostMapping("/list")
    public PageResult<ProductCategoryVO> categoryList(@RequestBody PageQueryDTO pageQueryDTO) {
        PageResult<ProductCategoryVO> productCategoryVOPageResult = productCategoryService.categoryList(pageQueryDTO);
        return productCategoryVOPageResult;
    }

    @PostMapping()
    @Operation(summary = "添加商品分类")
    public Boolean addCategory(@RequestBody ProductCategoryRequest productCategoryRequest) {
        return productCategoryService.addCategory(productCategoryRequest);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "修改商品分类")
    public Boolean updateCategory(@PathVariable Long categoryId, @RequestBody ProductCategoryRequest productCategoryRequest) {
        return productCategoryService.updateCategory(categoryId, productCategoryRequest);
    }
    @DeleteMapping("/{categoryId}")
    @Operation(summary = "删除商品分类")
    public Boolean deleteCategory(@PathVariable Long categoryId) {
        return productCategoryService.deleteCategory(categoryId);
    }
}
