package org.sc.smartcommunitybackend.controller.mall;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.dto.request.PageQueryDTO;
import org.sc.smartcommunitybackend.dto.request.ProductCategoryRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductCategoryVO;
import org.sc.smartcommunitybackend.service.ProductCategoryService;
import org.springframework.web.bind.annotation.*;

@RestController("CategoryController")
@RequestMapping("/product-categories")
@Tag(name = "用户端-商品分类")
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

}
