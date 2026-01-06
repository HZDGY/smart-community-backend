package org.sc.smartcommunitybackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.domain.ServiceArea;
import org.sc.smartcommunitybackend.dto.request.ServiceAreaPageRequest;
import org.sc.smartcommunitybackend.dto.request.ServiceAreaRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.service.ServiceAreaService;
import org.sc.smartcommunitybackend.mapper.ServiceAreaMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 吴展德
* @description 针对表【service_area(服务区域表)】的数据库操作Service实现
* @createDate 2025-12-30 10:46:05
*/
@Slf4j
@Service
public class ServiceAreaServiceImpl extends ServiceImpl<ServiceAreaMapper, ServiceArea>
    implements ServiceAreaService{

    @Override
    public PageResult<ServiceArea> queryList(ServiceAreaPageRequest serviceAreaPageRequest) {
        // 已实现的分页查询逻辑
        log.info("服务区域列表查询参数：{}", serviceAreaPageRequest);
        
        // 设置默认分页参数
        Integer pageNum = serviceAreaPageRequest.getPageNum() != null && serviceAreaPageRequest.getPageNum() > 0 
            ? serviceAreaPageRequest.getPageNum() : 1;
        Integer pageSize = serviceAreaPageRequest.getPageSize() != null && serviceAreaPageRequest.getPageSize() > 0 
            ? serviceAreaPageRequest.getPageSize() : 10;
        
        // 获取筛选参数
        String areaName = serviceAreaPageRequest.getAreaName();
        Long parentId = serviceAreaPageRequest.getParentId();
        String orderBy = serviceAreaPageRequest.getOrderBy();
        Boolean isAsc = serviceAreaPageRequest.getIsAsc();
        
        // 创建查询条件
        LambdaQueryWrapper<ServiceArea> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加模糊查询条件
        if (areaName != null && !areaName.trim().isEmpty()) {
            queryWrapper.like(ServiceArea::getArea_name, areaName);
        }
        
        // 添加父区域ID查询条件
        if (parentId != null) {
            queryWrapper.eq(ServiceArea::getParent_id, parentId);
        }
        
        // 添加排序条件
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            // 根据排序字段和排序方向添加排序条件
            boolean asc = isAsc != null && isAsc;
            switch (orderBy) {
                case "areaName":
                    if (asc) {
                        queryWrapper.orderByAsc(ServiceArea::getArea_name);
                    } else {
                        queryWrapper.orderByDesc(ServiceArea::getArea_name);
                    }
                    break;
                case "createTime":
                    if (asc) {
                        queryWrapper.orderByAsc(ServiceArea::getCreate_time);
                    } else {
                        queryWrapper.orderByDesc(ServiceArea::getCreate_time);
                    }
                    break;
                case "updateTime":
                    if (asc) {
                        queryWrapper.orderByAsc(ServiceArea::getUpdate_time);
                    } else {
                        queryWrapper.orderByDesc(ServiceArea::getUpdate_time);
                    }
                    break;
                default:
                    // 默认按创建时间降序排序
                    queryWrapper.orderByDesc(ServiceArea::getCreate_time);
            }
        } else {
            // 默认按创建时间降序排序
            queryWrapper.orderByDesc(ServiceArea::getCreate_time);
        }
        
        // 创建分页对象
        Page<ServiceArea> page = new Page<>(pageNum, pageSize);
        
        // 执行分页查询
        Page<ServiceArea> resultPage = this.page(page, queryWrapper);
        
        // 构建返回结果
        PageResult<ServiceArea> pageResult = new PageResult<>();
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPages(resultPage.getPages());
        pageResult.setList(resultPage.getRecords());
        
        log.info("服务区域分页查询结果：{}", pageResult);
        
        return pageResult;
    }

    @Override
    public Long addServiceArea(ServiceAreaRequest serviceAreaRequest) {
        log.info("新增服务区域参数：{}", serviceAreaRequest);
        
        // 创建ServiceArea实体
        ServiceArea serviceArea = new ServiceArea();
        serviceArea.setArea_name(serviceAreaRequest.getAreaName());
        
        // 设置父区域ID，默认为0（顶级区域）
        serviceArea.setParent_id(serviceAreaRequest.getParentId() != null ? serviceAreaRequest.getParentId() : 0L);
        
        // 设置创建时间和更新时间
        Date now = new Date();
        serviceArea.setCreate_time(now);
        serviceArea.setUpdate_time(now);
        
        // 保存到数据库
        boolean saved = this.save(serviceArea);
        
        if (saved) {
            log.info("新增服务区域成功，ID：{}", serviceArea.getArea_id());
            return serviceArea.getArea_id();
        } else {
            log.error("新增服务区域失败");
            throw new RuntimeException("新增服务区域失败");
        }
    }

    @Override
    public boolean updateServiceArea(ServiceAreaRequest serviceAreaRequest) {
        log.info("修改服务区域参数：{}", serviceAreaRequest);
        
        // 验证区域ID
        Long areaId = serviceAreaRequest.getAreaId();
        if (areaId == null) {
            throw new RuntimeException("区域ID不能为空");
        }
        
        // 检查区域是否存在
        ServiceArea existingArea = this.getById(areaId);
        if (existingArea == null) {
            throw new RuntimeException("服务区域不存在");
        }
        
        // 更新字段
        existingArea.setArea_name(serviceAreaRequest.getAreaName());
        existingArea.setParent_id(serviceAreaRequest.getParentId() != null && serviceAreaRequest.getParentId()!=0 ? serviceAreaRequest.getParentId() : 0L);
        existingArea.setUpdate_time(new Date());
        
        // 保存更新
        boolean updated = this.updateById(existingArea);
        
        if (updated) {
            log.info("修改服务区域成功，ID：{}", areaId);
        } else {
            log.error("修改服务区域失败，ID：{}", areaId);
        }
        
        return updated;
    }

    @Override
    public boolean deleteServiceArea(Long areaId) {
        log.info("删除服务区域，ID：{}", areaId);
        
        // 验证区域ID
        if (areaId == null) {
            throw new RuntimeException("区域ID不能为空");
        }
        
        // 检查区域是否存在
        ServiceArea existingArea = this.getById(areaId);
        if (existingArea == null) {
            throw new RuntimeException("服务区域不存在");
        }
        
        // 检查是否有子区域
        LambdaQueryWrapper<ServiceArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceArea::getParent_id, areaId);
        long count = this.count(queryWrapper);
        
        if (count > 0) {
            throw new RuntimeException("该区域下存在子区域，无法删除");
        }
        
        // 删除区域
        boolean deleted = this.removeById(areaId);
        
        if (deleted) {
            log.info("删除服务区域成功，ID：{}", areaId);
        } else {
            log.error("删除服务区域失败，ID：{}", areaId);
        }
        
        return deleted;
    }
}




