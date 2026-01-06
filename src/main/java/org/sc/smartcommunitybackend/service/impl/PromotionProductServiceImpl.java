package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.PromotionProduct;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.PromotionProductService;
import org.sc.smartcommunitybackend.mapper.PromotionProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author 吴展德
* @description 针对表【promotion_product(促销商品关联表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class PromotionProductServiceImpl extends ServiceImpl<PromotionProductMapper, PromotionProduct>
    implements PromotionProductService{

    @Transactional
    @Override
    public void updateByPromotionId(Long promotionId, List<Long> productIds) {
        log.info("更新促销商品关联参数：{},{}", promotionId, productIds);
        if (promotionId == null || promotionId <= 0){
            throw new RuntimeException("参数错误");
        }
        if (productIds != null && productIds.size() > 0) {
            // 先删除该促销下的所有商品关联
            lambdaUpdate()
                    .eq(PromotionProduct::getPromotion_id, promotionId)
                    .remove();
            // 如果有商品ID列表，则重新插入关联记录
            List<PromotionProduct> promotionProducts = new ArrayList<>();
            for (Long productId : productIds) {
                if (productId != null && productId > 0) {
                    PromotionProduct pp = new PromotionProduct();
                    pp.setPromotion_id(promotionId);
                    pp.setProduct_id(productId);
                    promotionProducts.add(pp);
                }
            }
            if (!promotionProducts.isEmpty()) {
                saveBatch(promotionProducts);
            }
        }else {
            log.info("无商品关联");
        }

    }

    /**
     * 绑定促销商品
     *
     * @param promotionId
     * @param productIds
     * @return
     */
    @Transactional
    @Override
    public void bindProducts(Long promotionId, List<Long> productIds) {
        log.info("绑定促销商品参数：{},{}", promotionId, productIds);
        if (promotionId == null || promotionId <= 0){
            throw new BusinessException("促销ID不能为空");
        }
        if (productIds == null || productIds.isEmpty() || productIds.size() <= 0){
            throw new BusinessException("商品ID不能为空");
        }
        for (Long productId : productIds) {
            PromotionProduct promotionProduct = new PromotionProduct();
            promotionProduct.setPromotion_id(promotionId);
            promotionProduct.setProduct_id(productId);
            boolean b = save(promotionProduct);
            if (!b){
                throw new BusinessException("绑定促销商品失败");
            }
        }

    }
}




