package org.sc.smartcommunitybackend.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.service.ProductCollectService;
import org.sc.smartcommunitybackend.mapper.ProductCollectMapper;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    /**
     * 收藏商品
     *
     * @param productId
     */
    @Override
    public void collect(Long productId) {
        log.info("收藏商品：{}", productId);
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Long currentUserId = UserContextUtil.getCurrentUserId();
        log.info("当前用户ID：{}", currentUserId);
        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }
        ProductCollect productCollect = new ProductCollect();
        productCollect.setUser_id(currentUserId);
        productCollect.setProduct_id(productId);
        productCollect.setCreate_time(DateTime.now());
        int insert = baseMapper.insert(productCollect);
        if (insert <= 0) {
            throw new RuntimeException("收藏失败");
        }
    }

    /**
     * 取消收藏商品
     *
     * @param productId
     */
    @Override
    public void cancelCollect(Long productId) {
        log.info("取消收藏商品：{}", productId);
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Long currentUserId = UserContextUtil.getCurrentUserId();

        if (currentUserId == null) {
            throw new RuntimeException("用户未登录");
        }
        int delete = baseMapper.delete(new LambdaQueryWrapper<ProductCollect>()
                .eq(ProductCollect::getUser_id, currentUserId)
                .eq(ProductCollect::getProduct_id, productId));
        if (delete <= 0) {
            throw new RuntimeException("取消收藏失败");
        }
    }
}




