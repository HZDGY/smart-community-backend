package org.sc.smartcommunitybackend.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.ProductCategory;
import org.sc.smartcommunitybackend.dto.request.PageQueryDTO;
import org.sc.smartcommunitybackend.dto.request.ProductCategoryRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.ProductCategoryVO;
import org.sc.smartcommunitybackend.dto.response.ProductListItemVO;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.ProductCategoryService;
import org.sc.smartcommunitybackend.mapper.ProductCategoryMapper;
import org.sc.smartcommunitybackend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【product_category(商品分类表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory>
    implements ProductCategoryService{

    @Resource
    private ProductService productService;
    /**
     * 商品分类列表
     *
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult<ProductCategoryVO> categoryList(PageQueryDTO pageQueryDTO) {
        log.info("商品分类列表查询参数：{}", pageQueryDTO);
        Long pageNo = pageQueryDTO.getPageNo();
        Long pageSize = pageQueryDTO.getPageSize();

        // 创建查询条件 "sortOrder":  // 排序权重（越小越靠前）
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ProductCategory::getSort);
        // 设置默认分页参数
        pageNo = (pageNo != null && pageNo > 0) ? pageNo : 1;
        pageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;

        // 创建分页对象
        Page<ProductCategory> page = new Page<>(pageNo, pageSize);

        // 执行分页查询
        Page<ProductCategory> resultPage = this.page(page, queryWrapper);
        log.info("分页查询结果：{}", resultPage);
        //结果转换
        List<ProductCategory> records = resultPage.getRecords();
        List<ProductCategoryVO> list = records.stream().map(item -> {
            ProductCategoryVO vo = new ProductCategoryVO();
            vo.setCategoryId(item.getCategory_id());
            vo.setCategoryName(item.getCategory_name());
            vo.setParentId(item.getParent_id());
            vo.setSortOrder(item.getSort());
            return vo;
        }).toList();
        // 创建分页结果对象
        PageResult<ProductCategoryVO> pageResult = new PageResult<>();
        pageResult.setList(list);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPages(resultPage.getPages());
        return pageResult;
    }

    /**
     * 添加商品分类
     *
     * @param productCategoryRequest
     * @return
     */
    @Override
    public Boolean addCategory(ProductCategoryRequest productCategoryRequest) {
        log.info("添加商品分类参数：{}", productCategoryRequest);
        String categoryName = productCategoryRequest.getCategoryName();
        Integer sortOrder = productCategoryRequest.getSortOrder();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            log.error("添加商品分类失败：分类名称不能为空");
            throw new BusinessException("分类名称不能为空");
        }
        sortOrder = sortOrder != null ? sortOrder : 1;
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategory_name(categoryName);
        productCategory.setSort(sortOrder);
        productCategory.setCreate_time(DateTime.now());
        boolean b = this.save(productCategory);
        if (!b) {
            throw new BusinessException("添加商品分类失败");
        }
        return b;
    }

    /**
     * 修改商品分类
     *
     * @param categoryId
     * @param productCategoryRequest
     * @return
     */
    @Override
    public Boolean updateCategory(Long categoryId, ProductCategoryRequest productCategoryRequest) {
        log.info("修改商品分类参数：{}", productCategoryRequest);
        String categoryName = productCategoryRequest.getCategoryName();
        Integer sortOrder = productCategoryRequest.getSortOrder();
        if (categoryName == null || categoryName.trim().isEmpty()){
            log.error("修改商品分类失败：分类名称不能为空");
        }
        if (categoryId == null){
            log.error("修改商品分类失败：分类ID不能为空");
        }
        sortOrder = sortOrder != null ? sortOrder : 1;
        ProductCategory productCategory = baseMapper.selectById(categoryId);
        if (productCategory == null){
            log.error("修改商品分类失败：分类不存在");
            throw new BusinessException("分类不存在");
        }
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setCategory_id(categoryId);
        productCategory1.setCategory_name(categoryName);
        productCategory1.setSort(sortOrder);
        productCategory1.setParent_id(productCategory.getParent_id());
        productCategory1.setUpdate_time(DateTime.now());
        boolean b = this.updateById(productCategory1);
        if (!b){
            throw new BusinessException("修改商品分类失败");
        }
        return b;
    }

    /**
     * 删除商品分类
     *
     * @param categoryId
     * @return
     */
    @Override
    public Boolean deleteCategory(Long categoryId) {
        log.info("删除商品分类参数：{}", categoryId);
        if (categoryId == null){
            log.error("删除商品分类失败：分类ID不能为空");
            throw new BusinessException("分类ID不能为空");
        }
        ProductCategory productCategory = baseMapper.selectById(categoryId);
        if (productCategory == null){
            log.error("删除商品分类失败：分类不存在");
            throw new BusinessException("分类不存在");
        }
        // 判断是否有关联商品
        List<Product> list = productService.list(new LambdaQueryWrapper<Product>().eq(Product::getCategory_id, categoryId));
        if (list != null && list.size() > 0){
            log.error("删除商品分类失败：该分类下有商品");
            throw new BusinessException("该分类下有商品");
        }
        // 判断是否有子分类
        List<ProductCategory> list1 = baseMapper.selectList(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getParent_id, categoryId));
        if (list1 != null && list1.size() > 0){
            log.error("删除商品分类失败：该分类下有子分类");
            throw new BusinessException("该分类下有子分类");
        }
        boolean b = this.removeById(categoryId);
        if (!b){
            throw new BusinessException("删除商品分类失败");
        }
        return b;
    }
}




