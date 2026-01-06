package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.PromotionProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【promotion_product(促销商品关联表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface PromotionProductService extends IService<PromotionProduct> {

    void updateByPromotionId(Long promotionId, List<Long> productIds);
}
