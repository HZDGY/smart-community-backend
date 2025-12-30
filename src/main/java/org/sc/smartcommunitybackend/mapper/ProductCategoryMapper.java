package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【product_category(商品分类表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.ProductCategory
*/
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

}




