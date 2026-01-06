package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 商品图片服务接口
 */
public interface ProductImageService {
    
    /**
     * 上传商品图片
     * 
     * @param productId 商品ID
     * @param file 图片文件
     * @param isMain 是否设为主图
     * @return 图片信息
     */
    ProductImage uploadImage(Long productId, MultipartFile file, Boolean isMain);
    
    /**
     * 获取商品的所有图片
     * 
     * @param productId 商品ID
     * @return 图片列表（按排序顺序）
     */
    List<ProductImage> getProductImages(Long productId);
    
    /**
     * 设置主图
     * 
     * @param imageId 图片ID
     * @return 是否成功
     */
    boolean setMainImage(Long imageId);
    
    /**
     * 删除图片
     * 
     * @param imageId 图片ID
     * @return 是否成功
     */
    boolean deleteImage(Long imageId);
    
    /**
     * 批量更新图片排序
     * 
     * @param productId 商品ID
     * @param imageOrders 图片ID和排序的映射
     * @return 是否成功
     */
    boolean updateImageOrder(Long productId, Map<Long, Integer> imageOrders);
    
    /**
     * 获取图片详情
     * 
     * @param imageId 图片ID
     * @return 图片信息
     */
    ProductImage getImageById(Long imageId);
}
