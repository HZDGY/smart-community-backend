package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.service.ProductCollectService;
import org.sc.smartcommunitybackend.mapper.ProductCollectMapper;
import org.springframework.stereotype.Service;

/**
* @author 吴展德
* @description 针对表【product_collect(商品收藏表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class ProductCollectServiceImpl extends ServiceImpl<ProductCollectMapper, ProductCollect>
    implements ProductCollectService{

    /**
     * 根据用户ID和商品ID查询收藏记录
     * @param currentUserId
     * @param productId
     * @return
     */
    @Override
    public ProductCollect getByUserIdAndProductId(Long currentUserId, Long productId) {
        log.info("根据用户ID和商品ID查询收藏记录currentUserId和productId：{}，{}", currentUserId, productId);
        if (currentUserId == null || productId == null){
            throw new RuntimeException("参数不能为空");
        }
        return this.getOne(new LambdaQueryWrapper<ProductCollect>()
                .eq(ProductCollect::getUser_id, currentUserId)
                .eq(ProductCollect::getProduct_id, productId));
    }
}




