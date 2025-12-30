package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sc.smartcommunitybackend.domain.ProductCategory;
import org.sc.smartcommunitybackend.service.ProductCategoryService;
import org.sc.smartcommunitybackend.mapper.ProductCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【product_category(商品分类表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory>
    implements ProductCategoryService{

}




