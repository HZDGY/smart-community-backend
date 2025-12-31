package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.dto.request.ProductListRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductListItemVO;
import org.sc.smartcommunitybackend.service.ProductService;
import org.sc.smartcommunitybackend.mapper.ProductMapper;
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
        queryWrapper.eq(categoryId != null, Product::getCategory_id, categoryId);
        queryWrapper.ge(minPrice != null, Product::getPrice, minPrice);
        queryWrapper.le(maxPrice != null, Product::getPrice, maxPrice);
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
            vo.setIsCollected(false); // 暂时设置为未收藏
            return vo;
        }).collect(Collectors.toList());

        // 创建分页结果对象
        PageResult<ProductListItemVO> pageResult = new PageResult<>();
        pageResult.setList(voList);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPages(resultPage.getPages());

        return pageResult;
    }

}




