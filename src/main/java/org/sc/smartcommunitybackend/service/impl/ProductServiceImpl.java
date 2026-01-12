package org.sc.smartcommunitybackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.constant.ProductConstant;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.ProductCategory;
import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.dto.request.AdminProductListRequest;
import org.sc.smartcommunitybackend.dto.request.ProductListRequest;
import org.sc.smartcommunitybackend.dto.request.ProductRequest;
import org.sc.smartcommunitybackend.dto.response.*;
import org.sc.smartcommunitybackend.service.ProductCategoryService;
import org.sc.smartcommunitybackend.service.ProductCollectService;
import org.sc.smartcommunitybackend.service.ProductService;
import org.sc.smartcommunitybackend.mapper.ProductMapper;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.sc.smartcommunitybackend.util.RedisUtil;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.sc.smartcommunitybackend.constant.RedisKeyConstant.PRODUCT;
import static org.sc.smartcommunitybackend.constant.RedisKeyConstant.PRODUCT_INFO;

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
    private RedisTemplate redisTemplate;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private StoreProductService storeProductService;
    @Resource
    private ProductCollectService productCollectService;
    @Resource
    private ProductCategoryService productCategoryService;

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
            if (currentUserId != null && product.getProduct_id() != null) {
                // 查询用户收藏状态
                ProductCollect byUserIdAndProductId = productCollectService.getByUserIdAndProductId(currentUserId, product.getProduct_id());
                vo.setIsCollected(byUserIdAndProductId != null);
            } else {
                vo.setIsCollected(false);
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

    /**
     * 商品详情
     * @param productId
     * @return
     */
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
        if (currentUserId != null && productId != null) {
            // 获取用户收藏状态
            ProductCollect byUserIdAndProductId = productCollectService.getByUserIdAndProductId(currentUserId, productId);
            productDetailVO.setIsCollected(byUserIdAndProductId != null);
        } else {
            productDetailVO.setIsCollected(false);
        }
        // 获取可售门店列表
        List<StoreListItemVO> availableStores = storeProductService.getAvailableStores(productId);
        productDetailVO.setAvailableStores(availableStores);
        return productDetailVO;
    }

    /**
     * 商品列表（管理员）
     *
     * @param adminProductListRequest
     * @return
     */
    @Override
    public PageResult<AdminProductVO> queryList(AdminProductListRequest adminProductListRequest) {
        log.info("商品列表查询参数：{}", adminProductListRequest);
        Long categoryId = adminProductListRequest.getCategoryId();
        String keyword = adminProductListRequest.getKeyword();
        Integer pageNum = adminProductListRequest.getPageNum();
        Integer pageSize = adminProductListRequest.getPageSize();
        // 创建查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && categoryId > 0, Product::getCategory_id, categoryId);
        queryWrapper.like(StringUtils.hasText(keyword), Product::getProduct_name, keyword);
        queryWrapper.orderByDesc(Product::getCreate_time);
        // 设置默认分页参数
        pageNum = (pageNum != null && pageNum > 0) ? pageNum : 1;
        pageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;

        // 创建分页对象
        Page<Product> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<Product> resultPage = this.page(page, queryWrapper);

        log.info("分页查询结果：{}", resultPage);
        // 转换结果：将 Product 转换为 AdminProductVO
        List<Product> records = resultPage.getRecords();
        List<AdminProductVO> voList = records.stream().map(product -> {
            AdminProductVO vo = new AdminProductVO();
            vo.setProductId(product.getProduct_id());
            vo.setProductName(product.getProduct_name());
            vo.setCategoryId(product.getCategory_id());
            ProductCategory category = productCategoryService.getById(product.getCategory_id());
            vo.setCategoryName(category.getCategory_name());
            vo.setDescription(product.getDescription());
            vo.setPrice(product.getPrice());
            vo.setStock(product.getStock());
            vo.setCoverImg(product.getCover_img());
            vo.setStatus(String.valueOf(product.getStatus()));
            vo.setCreateTime(product.getCreate_time());
            vo.setUpdateTime(product.getUpdate_time());
            return vo;
        }).collect(Collectors.toList());
        // 创建分页结果对象
        PageResult<AdminProductVO> pageResult = new PageResult<>();
        pageResult.setList(voList);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPages(resultPage.getPages());
        return pageResult;
    }
    /**
     * 商品列表2
     *
     * @param adminProductListRequest
     * @return
     */
    @Override
    public PageResult<ProductListItemVO> queryList2(AdminProductListRequest adminProductListRequest) {
        log.info("商品列表查询参数：{}", adminProductListRequest);
        Long categoryId = adminProductListRequest.getCategoryId();
        String keyword = adminProductListRequest.getKeyword();
        Integer pageNum = adminProductListRequest.getPageNum();
        Integer pageSize = adminProductListRequest.getPageSize();
        // 创建查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && categoryId > 0, Product::getCategory_id, categoryId);
        queryWrapper.like(StringUtils.hasText(keyword), Product::getProduct_name, keyword);
        queryWrapper.orderByDesc(Product::getCreate_time);
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
            if (currentUserId != null && product.getProduct_id() != null) {
                // 查询用户收藏状态
                ProductCollect byUserIdAndProductId = productCollectService.getByUserIdAndProductId(currentUserId, product.getProduct_id());
                vo.setIsCollected(byUserIdAndProductId != null);
            } else {
                vo.setIsCollected(false);
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

    /**
     * 添加商品
     *
     * @param productRequest
     */
    @Override
    public void add(ProductRequest productRequest) {
        log.info("添加商品：{}", productRequest);
        String productName = productRequest.getProductName();
        Long categoryId = productRequest.getCategoryId();
        BigDecimal price = productRequest.getPrice();
        Integer stock = productRequest.getStock();
        String coverImg = productRequest.getCoverImg();
        String status = productRequest.getStatus();
        if(productName == null || productName.trim().isEmpty()){
            throw new RuntimeException("商品名称不能为空");
        }
        if(categoryId == null || categoryId <= 0){
            throw new RuntimeException("商品分类ID不能为空");
        }
        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("商品价格不能为空");
        }
        if(stock == null || stock < 0){
            throw new RuntimeException("商品库存不能为空");
        }
        if(coverImg == null || coverImg.trim().isEmpty()){
            throw new RuntimeException("商品封面图不能为空");
        }
        status = status == null ? ProductConstant.STATUS_STR_OFF_SALE : status;

        if(!ProductConstant.STATUS_STR_OFF_SALE.equals(status) && !ProductConstant.STATUS_STR_ON_SALE.equals(status)){
            throw new RuntimeException("商品状态错误");
        }
        Product product = new Product();
        product.setProduct_name(productName);
        product.setCategory_id(categoryId);
        product.setPrice(price);
        product.setStock(stock);
        product.setCover_img(coverImg);
        product.setStatus( ProductConstant.STATUS_STR_OFF_SALE.equals(status) ? ProductConstant.STATUS_OFF_SALE : ProductConstant.STATUS_ON_SALE);
        boolean b = this.save(product);
        if(!b){
            throw new RuntimeException("添加商品失败");
        }
//        // 保存商品到Redis
//        String redisKey =PRODUCT;
//        String key = String.format(PRODUCT_INFO, product.getProduct_id()%10);
//        redisTemplate.opsForHash().put(redisKey, key, product);
//        redisTemplate.expire(redisKey, 36000, TimeUnit.SECONDS); // 设置36000秒过期时间,10天
//        redisUtil.hset(redisKey, key, product, 36000);
//        log.info("商品保存到Redis成功");
    }

    /**
     * 修改商品
     *
     * @param productId
     * @param productRequest
     */
    @Override
    public void updateProduct(Long productId, ProductRequest productRequest) {
        log.info("修改商品：{}", productRequest);
        if (productId == null || productId <= 0){
            throw new RuntimeException("商品ID不能为空");
        }
        String productName = productRequest.getProductName();
        Long categoryId = productRequest.getCategoryId();
        BigDecimal price = productRequest.getPrice();
        BigDecimal originalPrice = productRequest.getOriginalPrice();
        Integer stock = productRequest.getStock();
        String coverImg = productRequest.getCoverImg();
        List<String> detailImgs = productRequest.getDetailImgs();
        String description = productRequest.getDescription();
        String status = productRequest.getStatus();
        boolean update = lambdaUpdate().eq(Product::getProduct_id, productId)
                .set(productName != null && !productName.trim().isEmpty(), Product::getProduct_name, productName)
                .set(categoryId != null && categoryId > 0, Product::getCategory_id, categoryId)
                .set(price != null && price.compareTo(BigDecimal.ZERO) > 0, Product::getPrice, price)
                .set(stock != null && stock > 0, Product::getStock, stock)
                .set(coverImg != null && !coverImg.trim().isEmpty(), Product::getCover_img, coverImg)
                .set(description != null && !description.trim().isEmpty(), Product::getDescription, description)
                .set(status != null && !status.trim().isEmpty(), Product::getStatus, status)
                .update();
        if(!update){
            throw new RuntimeException("修改商品失败");
        }
    }

    /**
     * 删除商品
     *
     * @param productId
     * @return
     */
    @Override
    public Long delete(Long productId) {
       log.info("删除商品：{}", productId);
       if (productId == null || productId <= 0){
           throw new RuntimeException("商品ID不能为空");
       }
        Product product = baseMapper.selectById(productId);
       if (product == null){
           throw new RuntimeException("商品不存在");
       }
       if (product.getStatus() == ProductConstant.STATUS_ON_SALE){
           throw new RuntimeException("商品正在销售中，请先下架");
       }
       boolean delete = this.removeById(productId);
       if(!delete){
           throw new RuntimeException("删除商品失败");
       }
       return productId;
    }


}




