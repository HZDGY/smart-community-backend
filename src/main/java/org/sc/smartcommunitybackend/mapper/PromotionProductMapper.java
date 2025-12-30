package org.sc.smartcommunitybackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sc.smartcommunitybackend.domain.PromotionProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 吴展德
* @description 针对表【promotion_product(促销商品关联表)】的数据库操作Mapper
* @createDate 2025-12-30 10:46:05
* @Entity generator.domain.PromotionProduct
*/
@Mapper
public interface PromotionProductMapper extends BaseMapper<PromotionProduct> {

}




