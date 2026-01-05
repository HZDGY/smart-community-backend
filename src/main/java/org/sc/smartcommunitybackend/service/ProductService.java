package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.AdminProductListRequest;
import org.sc.smartcommunitybackend.dto.request.ProductListRequest;
import org.sc.smartcommunitybackend.dto.request.ProductRequest;
import org.sc.smartcommunitybackend.dto.response.AdminProductVO;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductDetailVO;
import org.sc.smartcommunitybackend.dto.response.ProductListItemVO;

/**
* @author 吴展德
* @description 针对表【product(商品表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface ProductService extends IService<Product> {

    PageResult<ProductListItemVO> queryList(ProductListRequest productListRequest);

    ProductDetailVO detail(Long productId);


    PageResult<AdminProductVO> queryList(AdminProductListRequest adminProductListRequest);

    void add(ProductRequest productRequest);
}
