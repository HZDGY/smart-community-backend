package org.sc.smartcommunitybackend.service;

import org.sc.smartcommunitybackend.domain.Store;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sc.smartcommunitybackend.dto.request.AdminStoreListRequest;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.request.StoreRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.StoreVO;

import java.util.List;

/**
* @author 吴展德
* @description 针对表【store(门店表)】的数据库操作Service
* @createDate 2025-12-30 10:46:05
*/
public interface StoreService extends IService<Store> {

    /**
     * 管理端门店列表查询
     * @param adminStoreListRequest 查询参数
     * @return 分页结果
     */
    PageResult<StoreVO> adminStoreList(AdminStoreListRequest adminStoreListRequest);
    
    /**
     * 新增门店
     * @param storeRequest 门店请求参数
     * @return 是否成功
     */
    boolean addStore(StoreRequest storeRequest);
    
    /**
     * 修改门店
     * @param storeRequest 门店请求参数
     * @return 是否成功
     */
    boolean updateStore(StoreRequest storeRequest);
    
    /**
     * 删除门店
     * @param storeId 门店ID
     * @return 是否成功
     */
    boolean deleteStore(Long storeId);
}
