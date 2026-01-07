package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.domain.StoreProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.request.StoreProductStatusRequest;
import org.sc.smartcommunitybackend.dto.request.StoreProductStockRequest;
import org.sc.smartcommunitybackend.dto.response.StoreListItemVO;
import org.sc.smartcommunitybackend.dto.response.StoreVO;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【store_product(门店商品关联表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface StoreProductService extends IService<StoreProduct> {


    List<StoreVO> queryList(StoreListRequest storeListRequest);
    List<StoreListItemVO> getAvailableStores(Long productId);

    boolean updateStoreProductStatus(Long storeProductId, StoreProductStatusRequest status);

    boolean updateStoreProductStock(Long storeProductId, StoreProductStockRequest stock);

    StoreProduct getStoreProductById(Long productId, Long storeId);
}
