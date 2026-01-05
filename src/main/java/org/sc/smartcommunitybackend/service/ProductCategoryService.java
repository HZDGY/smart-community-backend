package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.PageQueryDTO;
import org.sc.smartcommunitybackend.dto.request.ProductCategoryRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductCategoryVO;

/**
* @author 吴展德
* @description 针对表【product_category(商品分类表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface ProductCategoryService extends IService<ProductCategory> {

    PageResult<ProductCategoryVO> categoryList(PageQueryDTO pageQueryDTO);

    Boolean addCategory(ProductCategoryRequest productCategoryRequest);

    Boolean updateCategory(Long categoryId, ProductCategoryRequest productCategoryRequest);

    Boolean deleteCategory(Long categoryId);
}
