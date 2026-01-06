package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.ProductImage;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.mapper.ProductImageMapper;
import org.sc.smartcommunitybackend.mapper.ProductMapper;
import org.sc.smartcommunitybackend.service.ProductImageService;
import org.sc.smartcommunitybackend.util.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品图片服务实现类
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductImageServiceImpl.class);
    
    /**
     * 每个商品最多图片数量
     */
    private static final int MAX_IMAGES_PER_PRODUCT = 10;
    
    @Autowired
    private ProductImageMapper imageMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private FileUploadUtil fileUploadUtil;
    
    @Override
    @Transactional
    public ProductImage uploadImage(Long productId, MultipartFile file, Boolean isMain) {
        // 验证商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 检查图片数量限制
        LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductImage::getProductId, productId);
        Long count = imageMapper.selectCount(wrapper);
        if (count >= MAX_IMAGES_PER_PRODUCT) {
            throw new BusinessException("每个商品最多只能上传" + MAX_IMAGES_PER_PRODUCT + "张图片");
        }
        
        // 上传文件
        String imageUrl = fileUploadUtil.uploadFile(file, "products");
        
        // 如果是第一张图片，自动设为主图
        if (count == 0) {
            isMain = true;
        }
        
        // 如果设为主图，取消其他图片的主图状态
        if (Boolean.TRUE.equals(isMain)) {
            imageMapper.clearMainImage(productId);
        }
        
        // 创建图片记录
        ProductImage image = new ProductImage();
        image.setProductId(productId);
        image.setImageUrl(imageUrl);
        image.setIsMain(Boolean.TRUE.equals(isMain) ? 1 : 0);
        image.setSortOrder(count.intValue()); // 新图片排在最后
        image.setCreateTime(new Date());
        image.setUpdateTime(new Date());
        
        imageMapper.insert(image);
        
        // 如果是主图，更新商品表的封面图
        if (Boolean.TRUE.equals(isMain)) {
            product.setCover_img(imageUrl);
            productMapper.updateById(product);
        }
        
        logger.info("商品 {} 上传图片成功，图片ID: {}，是否主图: {}", productId, image.getImageId(), isMain);
        
        return image;
    }
    
    @Override
    public List<ProductImage> getProductImages(Long productId) {
        LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductImage::getProductId, productId);
        wrapper.orderByAsc(ProductImage::getSortOrder);
        
        return imageMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional
    public boolean setMainImage(Long imageId) {
        // 查询图片信息
        ProductImage image = imageMapper.selectById(imageId);
        if (image == null) {
            throw new BusinessException("图片不存在");
        }
        
        // 取消该商品其他图片的主图状态
        imageMapper.clearMainImage(image.getProductId());
        
        // 设置当前图片为主图
        image.setIsMain(1);
        image.setUpdateTime(new Date());
        imageMapper.updateById(image);
        
        // 更新商品表的封面图
        Product product = productMapper.selectById(image.getProductId());
        if (product != null) {
            product.setCover_img(image.getImageUrl());
            productMapper.updateById(product);
        }
        
        logger.info("设置图片 {} 为商品 {} 的主图", imageId, image.getProductId());
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean deleteImage(Long imageId) {
        // 查询图片信息
        ProductImage image = imageMapper.selectById(imageId);
        if (image == null) {
            throw new BusinessException("图片不存在");
        }
        
        Long productId = image.getProductId();
        boolean wasMain = image.getIsMain() == 1;
        
        // 删除文件
        fileUploadUtil.deleteFile(image.getImageUrl());
        
        // 删除数据库记录
        imageMapper.deleteById(imageId);
        
        // 如果删除的是主图，将第一张图片设为主图
        if (wasMain) {
            LambdaQueryWrapper<ProductImage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductImage::getProductId, productId);
            wrapper.orderByAsc(ProductImage::getSortOrder);
            wrapper.last("LIMIT 1");
            
            ProductImage firstImage = imageMapper.selectOne(wrapper);
            if (firstImage != null) {
                setMainImage(firstImage.getImageId());
            } else {
                // 没有图片了，清空商品封面
                Product product = productMapper.selectById(productId);
                if (product != null) {
                    product.setCover_img(null);
                    productMapper.updateById(product);
                }
            }
        }
        
        logger.info("删除图片 {}，商品ID: {}", imageId, productId);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean updateImageOrder(Long productId, Map<Long, Integer> imageOrders) {
        if (imageOrders == null || imageOrders.isEmpty()) {
            return false;
        }
        
        // 更新每张图片的排序
        for (Map.Entry<Long, Integer> entry : imageOrders.entrySet()) {
            Long imageId = entry.getKey();
            Integer sortOrder = entry.getValue();
            
            ProductImage image = imageMapper.selectById(imageId);
            if (image != null && image.getProductId().equals(productId)) {
                image.setSortOrder(sortOrder);
                image.setUpdateTime(new Date());
                imageMapper.updateById(image);
            }
        }
        
        logger.info("更新商品 {} 的图片排序", productId);
        
        return true;
    }
    
    @Override
    public ProductImage getImageById(Long imageId) {
        return imageMapper.selectById(imageId);
    }
}
