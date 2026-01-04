package org.sc.smartcommunitybackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.dto.request.ProductListRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductDetailVO;
import org.sc.smartcommunitybackend.dto.response.ProductListItemVO;
import org.sc.smartcommunitybackend.dto.response.StoreListItemVO;
import org.sc.smartcommunitybackend.service.ProductCollectService;
import org.sc.smartcommunitybackend.service.ProductService;
import org.sc.smartcommunitybackend.mapper.ProductMapper;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 吴展德
* @description 针对表【product(商品表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{
    @Resource
    private StoreProductService storeProductService;
    @Resource
    private ProductCollectService productCollectService;

    /**
     * 商品列表
     * @param productListRequest
     * @return
     */
    @Override
    public PageResult<ProductListItemVO> queryList(ProductListRequest productListRequest) {
        log.info("商品列表查询参数：{}", productListRequest);
        Long categoryId = productListRequest.getCategoryId();
        String sortBy = productListRequest.getSortBy();
        BigDecimal minPrice = productListRequest.getMinPrice();
        BigDecimal maxPrice = productListRequest.getMaxPrice();
        Integer pageNum = productListRequest.getPageNum();
        Integer pageSize = productListRequest.getPageSize();

        // 创建查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        // 只有当categoryId大于0时才添加分类查询条件
        queryWrapper.eq(categoryId != null && categoryId > 0, Product::getCategory_id, categoryId);
        // 只有当minPrice大于0时才添加最低价格查询条件
        queryWrapper.ge(minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0, Product::getPrice, minPrice);
        // 只有当maxPrice大于0时才添加最高价格查询条件
        queryWrapper.le(maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0, Product::getPrice, maxPrice);
        // 排序
        // 排序
        if (StringUtils.hasText(sortBy)) {
            if ("price_asc".equals(sortBy)) {
                queryWrapper.orderByAsc(Product::getPrice);
            } else if ("price_desc".equals(sortBy)) {
                queryWrapper.orderByDesc(Product::getPrice);
            } else {
                queryWrapper.orderByDesc(Product::getCreate_time); // 默认按创建时间倒序
            }
        } else {
            queryWrapper.orderByDesc(Product::getCreate_time);
        }
        // 设置默认分页参数
        pageNum = (pageNum != null && pageNum > 0) ? pageNum : 1;
        pageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;

        // 创建分页对象
        Page<Product> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<Product> resultPage = this.page(page, queryWrapper);

        log.info("分页查询结果：{}", resultPage);

        // 转换结果：将 Product 转换为 ProductListItemVO
        List<Product> records = resultPage.getRecords();
        List<ProductListItemVO> voList = records.stream().map(product -> {
            ProductListItemVO vo = new ProductListItemVO();
            vo.setProductId(product.getProduct_id());
            vo.setProductName(product.getProduct_name());
            vo.setDescription(product.getDescription());
            vo.setPrice(product.getPrice());
            vo.setCoverImg(product.getCover_img());
            // 设置是否已收藏（根据用户ID和商品ID查询收藏状态）
            Long currentUserId = UserContextUtil.getCurrentUserId();
            if (currentUserId != null) {
                // 查询用户收藏状态
                ProductCollect byUserIdAndProductId = productCollectService.getByUserIdAndProductId(currentUserId, product.getProduct_id());
                vo.setIsCollected(byUserIdAndProductId != null);
            }
            return vo;
        }).collect(Collectors.toList());

        // 创建分页结果对象
        PageResult<ProductListItemVO> pageResult = new PageResult<>();
        pageResult.setList(voList);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPages(resultPage.getPages());

        return pageResult;
    }

    @Override
    public ProductDetailVO detail(Long productId) {
        if (productId == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Product product = this.getById(productId);
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setProductId(product.getProduct_id());
        productDetailVO.setProductName(product.getProduct_name());
        productDetailVO.setCategoryId(product.getCategory_id());
        productDetailVO.setCategoryName(productDetailVO.getCategoryName());
        productDetailVO.setDescription(product.getDescription());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setCoverImg(product.getCover_img());
        // 设置是否已收藏
        Long currentUserId = UserContextUtil.getCurrentUserId();
        if (currentUserId != null) {
            // 获取用户收藏状态
            ProductCollect byUserIdAndProductId = productCollectService.getByUserIdAndProductId(currentUserId, productId);
            productDetailVO.setIsCollected(byUserIdAndProductId != null);
        }
        // 获取可售门店列表
        List<StoreListItemVO> availableStores = storeProductService.getAvailableStores(productId);
        productDetailVO.setAvailableStores(availableStores);
        return productDetailVO;
    }

}




