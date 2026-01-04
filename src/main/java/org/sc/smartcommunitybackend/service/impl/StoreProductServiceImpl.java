package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.Product;
import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.domain.Store;
import org.sc.smartcommunitybackend.domain.StoreProduct;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.response.StoreListItemVO;
import org.sc.smartcommunitybackend.dto.response.StoreVO;
import org.sc.smartcommunitybackend.service.StoreProductService;
import org.sc.smartcommunitybackend.mapper.StoreProductMapper;
import org.sc.smartcommunitybackend.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 吴展德
* @description 针对表【store_product(门店商品关联表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class StoreProductServiceImpl extends ServiceImpl<StoreProductMapper, StoreProduct>
    implements StoreProductService{
    @Resource
    private StoreService storeService;


    /**
     * 查询商品可自提门店列表
     *
     * @param storeListRequest
     * @return
     */
    @Override
    public List<StoreVO> queryList(StoreListRequest storeListRequest) {
        log.info("门店列表查询参数：{}", storeListRequest);
        Long productId = storeListRequest.getProductId();
        Integer pageNum = storeListRequest.getPageNum();
        Integer pageSize = storeListRequest.getPageSize();
        // 创建查询条件
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(productId != null && productId > 0, StoreProduct::getProduct_id, productId);
        // 设置默认分页参数
        pageNum = (pageNum != null && pageNum > 0) ? pageNum : 1;
        pageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        // 创建分页对象
        Page<StoreProduct> page = new Page<>(pageNum, pageSize);
        // 执行分页查询
        List<StoreProduct> storeProducts = this.page(page, queryWrapper).getRecords();
        log.info("分页查询门店列表：{}", storeProducts);
        List<StoreVO> storeVOS = new ArrayList<>();
        // 转换结果
        for (StoreProduct storeProduct : storeProducts) {
            Store store = storeService.getById(storeProduct.getStore_id());
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId(storeProduct.getStore_id());
            storeVO.setAddress(store.getStore_name());
            storeVO.setBusinessHours(store.getBusiness_hours());
            storeVO.setContactPhone(store.getContact_phone());
            storeVO.setStoreName(store.getStore_name());
            storeVOS.add(storeVO);
        }
        return storeVOS;
    }

    /**
     * 获取可用门店列表
     * @param productId
     * @return
     */
    @Override
    public List<StoreListItemVO> getAvailableStores(Long productId) {
        log.info("获取可用门店列表productId:{}", productId);
        if (productId == null){
            throw new RuntimeException("商品ID不能为空");
        }
        List<StoreProduct> storeProducts = this.list(new LambdaQueryWrapper<StoreProduct>().eq(StoreProduct::getProduct_id, productId));
        if (storeProducts == null || storeProducts.isEmpty()){
            return new ArrayList<>();
        }
        List<StoreListItemVO> storeListItemVOS = new ArrayList<>();
        // 转换成门店列表项
        for (StoreProduct storeProduct : storeProducts) {
            StoreListItemVO vo = new StoreListItemVO();
            vo.setStoreId(storeProduct.getStore_id());
            //根据门店id查询门店
            Store store = storeService.getById(storeProduct.getStore_id());
            log.info("门店：{}", store);
            // 添加空值检查
            if (store != null) {
                vo.setStoreName(store.getStore_name());
                vo.setAddress(store.getAddress());
                vo.setBusinessHours(store.getBusiness_hours());
            } else {
                // 设置默认值或跳过该记录
                vo.setStoreName("未知门店");
                vo.setAddress("地址未知");
                vo.setBusinessHours("营业时间未知");
            }
            vo.setStock(storeProduct.getStock());
            storeListItemVOS.add(vo);
        }
        return storeListItemVOS;
    }
}




