package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.ProductCollect;
import org.sc.smartcommunitybackend.domain.StoreProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.response.StoreListItemVO;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【store_product(门店商品关联表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface StoreProductService extends IService<StoreProduct> {


    List<StoreListItemVO> getAvailableStores(Long productId);
}
