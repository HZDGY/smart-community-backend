package org.sc.smartcommunitybackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.sc.smartcommunitybackend.domain.ProductImage;

/**
 * 商品图片 Mapper 接口
 */
@Mapper
public interface ProductImageMapper extends BaseMapper<ProductImage> {
    
    /**
     * 取消指定商品的所有主图标识
     * 
     * @param productId 商品ID
     * @return 影响行数
     */
    @Update("UPDATE product_image SET is_main = 0 WHERE product_id = #{productId}")
    int clearMainImage(@Param("productId") Long productId);
}
