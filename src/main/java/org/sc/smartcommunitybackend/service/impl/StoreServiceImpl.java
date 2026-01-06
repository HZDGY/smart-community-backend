package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.ServiceArea;
import org.sc.smartcommunitybackend.domain.Store;
import org.sc.smartcommunitybackend.dto.request.AdminStoreListRequest;
import org.sc.smartcommunitybackend.dto.request.StoreListRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.dto.response.StoreVO;
import org.sc.smartcommunitybackend.service.ServiceAreaService;
import org.sc.smartcommunitybackend.service.StoreService;
import org.sc.smartcommunitybackend.mapper.StoreMapper;
import org.sc.smartcommunitybackend.common.enums.StoreStatusEnum;
import org.sc.smartcommunitybackend.constant.StoreConstant;
import org.sc.smartcommunitybackend.dto.request.StoreRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 吴展德
* @description 针对表【store(门店表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>
    implements StoreService{
    @Resource
    private ServiceAreaService serviceAreaService;

    /**
     * 管理端门店列表查询
     *
     * @param adminStoreListRequest 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult<StoreVO> adminStoreList(AdminStoreListRequest adminStoreListRequest) {
        log.info("管理端门店列表查询参数：{}", adminStoreListRequest);
        
        // 设置默认分页参数
        Integer pageNum = adminStoreListRequest.getPageNum() != null && adminStoreListRequest.getPageNum() > 0 
            ? adminStoreListRequest.getPageNum() : 1;
        Integer pageSize = adminStoreListRequest.getPageSize() != null && adminStoreListRequest.getPageSize() > 0 
            ? adminStoreListRequest.getPageSize() : 10;
        
        // 获取筛选参数
        String storeName = adminStoreListRequest.getStoreName();
        Long areaId = adminStoreListRequest.getAreaId();
        Integer status = adminStoreListRequest.getStatus();
        String orderBy = adminStoreListRequest.getOrderBy();
        Boolean isAsc = adminStoreListRequest.getIsAsc();
        
        // 创建查询条件
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加筛选条件
        if (storeName != null && !storeName.isEmpty()) {
            queryWrapper.like(Store::getStore_name, storeName);
        }
        if (areaId != null) {
            queryWrapper.eq(Store::getArea_id, areaId);
        }
        if (status != null) {
            queryWrapper.eq(Store::getStatus, status);
        }
        
        // 添加排序条件
        if (orderBy != null && !orderBy.isEmpty()) {
            if ("storeName".equals(orderBy)) {
                queryWrapper.orderBy(true, isAsc != null && isAsc, Store::getStore_name);
            } else if ("status".equals(orderBy)) {
                queryWrapper.orderBy(true, isAsc != null && isAsc, Store::getStatus);
            } else if ("createTime".equals(orderBy)) {
                queryWrapper.orderBy(true, isAsc != null && isAsc, Store::getCreate_time);
            } else if ("updateTime".equals(orderBy)) {
                queryWrapper.orderBy(true, isAsc != null && isAsc, Store::getUpdate_time);
            }
        } else {
            // 默认按创建时间降序排序
            queryWrapper.orderByDesc(Store::getCreate_time);
        }
        
        // 创建分页对象
        Page<Store> page = new Page<>(pageNum, pageSize);
        
        // 执行分页查询
        Page<Store> storePage = this.page(page, queryWrapper);
        
        // 转换为VO
        List<StoreVO> storeVOList = new ArrayList<>();
        for (Store store : storePage.getRecords()) {
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId(store.getStore_id());
            storeVO.setStoreName(store.getStore_name());
            storeVO.setAddress(store.getAddress());
            storeVO.setBusinessHours(store.getBusiness_hours());
            storeVO.setContactPhone(store.getContact_phone());
            storeVO.setStatus(store.getStatus());
            storeVOList.add(storeVO);
        }
        
        // 构建分页结果
        PageResult<StoreVO> pageResult = new PageResult<>();
        pageResult.setTotal(storePage.getTotal());
        pageResult.setPages(storePage.getPages());
        pageResult.setList(storeVOList);
        
        return pageResult;
    }
    
    @Override
    public boolean addStore(StoreRequest storeRequest) {
        log.info("新增门店参数：{}", storeRequest);
        Store store = new Store();
        store.setStore_name(storeRequest.getStoreName());
        store.setArea_id(storeRequest.getAreaId());
        store.setAddress(storeRequest.getAddress());
        store.setBusiness_hours(storeRequest.getBusinessHours());
        store.setContact_phone(storeRequest.getContactPhone());
        
        // 设置默认状态
        String status = storeRequest.getStatus();
        if (StoreConstant.STATUS_STR_CLOSED.equals(status)) {
            store.setStatus(StoreStatusEnum.CLOSED.getCode());
        } else {
            store.setStatus(StoreStatusEnum.NORMAL.getCode());
        }
        
        return this.save(store);
    }
    
    @Override
    public boolean updateStore(StoreRequest storeRequest) {
        log.info("修改门店参数：{}", storeRequest);
        Long storeId = storeRequest.getStoreId();
        if (storeId == null || storeId <= 0) {
            log.error("门店ID不能为空");
            return false;
        }
        Long areaId = storeRequest.getAreaId();
        Store store = new Store();
        store.setStore_id(storeId);
        store.setStore_name(storeRequest.getStoreName());
        store.setArea_id(areaId);
        store.setAddress(storeRequest.getAddress());
        store.setBusiness_hours(storeRequest.getBusinessHours());
        store.setContact_phone(storeRequest.getContactPhone());
        // 只有当 areaId 不为 null 时才更新区域，并验证其存在性
        if (areaId != null && areaId > 0) {
            // 在插入门店前验证区域是否存在
            ServiceArea area = serviceAreaService.getById(areaId);
            if (area == null) {
                throw new RuntimeException("指定的服务区域不存在: " + areaId);
            }
            store.setArea_id(areaId);
        }

        // 设置状态
        String status = storeRequest.getStatus();
        if (StoreConstant.STATUS_STR_CLOSED.equals(status)) {
            store.setStatus(StoreStatusEnum.CLOSED.getCode());
        } else if (StoreConstant.STATUS_STR_NORMAL.equals(status)) {
            store.setStatus(StoreStatusEnum.NORMAL.getCode());
        }
        
        return this.updateById(store);
    }
    
    @Override
    public boolean deleteStore(Long storeId) {
        log.info("删除门店参数：storeId={}", storeId);
        if (storeId == null) {
            log.error("门店ID不能为空");
            return false;
        }
        return this.removeById(storeId);
    }
}




