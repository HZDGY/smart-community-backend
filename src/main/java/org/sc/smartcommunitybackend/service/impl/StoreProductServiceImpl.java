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
import org.sc.smartcommunitybackend.dto.request.StoreProductStatusRequest;
import org.sc.smartcommunitybackend.dto.request.StoreProductStockRequest;
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
     * 根据商品ID、门店ID查询门店商品
     */
    @Override
    public StoreProduct getStoreProductById(Long productId, Long storeId) {
        log.info("根据商品ID、门店ID查询门店商品参数：{}", productId, storeId);
        if(productId == null && productId<=0){
            throw new RuntimeException("商品ID不能为空");
        }
        if(storeId == null && storeId<=0){
            throw new RuntimeException("门店ID不能为空");
        }
        List<StoreProduct> list = lambdaQuery().eq(StoreProduct::getProduct_id, productId)
                .eq(StoreProduct::getStore_id, storeId)
                .list();
        if (list == null || list.isEmpty() || list.size()<=0){
            throw new RuntimeException("门店商品不存在");
        }
        StoreProduct storeProduct = list.get(0);
        return storeProduct;
    }


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

    /**
     * 门店商品上下架
     * @param storeProductId
     * @param status
     * @return
     */
    @Override
    public boolean updateStoreProductStatus(Long storeProductId, StoreProductStatusRequest status) {
        log.info("门店商品上下架参数：{}", status);
        if(storeProductId == null && storeProductId<=0){
            throw new RuntimeException("门店商品ID不能为空");
        }
        if ( status == null){
            throw new RuntimeException("门店商品状态不能为空");
        }
        StoreProduct storeProduct = this.getById(storeProductId);
        if (storeProduct == null){
            throw new RuntimeException("门店商品不存在");
        }
        boolean update = lambdaUpdate().eq(StoreProduct::getId, storeProductId)
                .set(StoreProduct::getStatus, status.getStatus())
                .update();
        if (!update){
            throw new RuntimeException("修改门店商品状态失败");
        }
        return update;
    }

    /**
     * 门店商品库存分配
     * @param storeProductId
     * @param stock
     * @return
     */
    @Override
    public boolean updateStoreProductStock(Long storeProductId, StoreProductStockRequest stock) {
        log.info("门店商品库存分配参数：{}", stock);
        if(storeProductId == null && storeProductId<=0){
            throw new RuntimeException("门店商品ID不能为空");
        }
        if ( stock == null || stock.getStock()==0){
            throw new RuntimeException("门店商品库存不能为空或者小于0");
        }
        StoreProduct storeProduct = this.getById(storeProductId);
        if (storeProduct == null){
            throw new RuntimeException("门店商品不存在");
        }
        boolean update = lambdaUpdate().eq(StoreProduct::getId, storeProductId)
                .set(StoreProduct::getStock, stock.getStock())
                .update();
        if (!update){
            throw new RuntimeException("修改门店商品库存失败");
        }
        return update;
    }


}




