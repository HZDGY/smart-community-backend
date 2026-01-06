package org.sc.smartcommunitybackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.domain.ServiceArea;
import org.sc.smartcommunitybackend.dto.request.ServiceAreaPageRequest;
import org.sc.smartcommunitybackend.dto.request.ServiceAreaRequest;
import org.sc.smartcommunitybackend.dto.response.PageResult;
import org.sc.smartcommunitybackend.service.ServiceAreaService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-服务区域管理接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/service-areas")
@Tag(name = "管理端-服务区域管理接口")
public class serviceAreasController {
    @Resource
    private ServiceAreaService serviceAreaService;
    
    @PostMapping("/list")
    @Operation(summary = "服务区域列表")
    public Result<PageResult<ServiceArea>> list(@RequestBody ServiceAreaPageRequest serviceAreaPageRequest) {
        PageResult<ServiceArea> serviceAreaPageResult = serviceAreaService.queryList(serviceAreaPageRequest);
        return Result.success(serviceAreaPageResult);
    }
    
    @PostMapping("/add")
    @Operation(summary = "新增服务区域")
    public Result<Long> add(@Valid @RequestBody ServiceAreaRequest serviceAreaRequest) {
        Long areaId = serviceAreaService.addServiceArea(serviceAreaRequest);
        return Result.success(areaId);
    }
    
    @PostMapping("/update")
    @Operation(summary = "修改服务区域")
    public Result<Boolean> update(@Valid @RequestBody ServiceAreaRequest serviceAreaRequest) {
        boolean updated = serviceAreaService.updateServiceArea(serviceAreaRequest);
        return Result.success(updated);
    }
    
    @DeleteMapping("/{areaId}")
    @Operation(summary = "删除服务区域")
    public Result<Boolean> delete(@PathVariable Long areaId) {
        boolean deleted = serviceAreaService.deleteServiceArea(areaId);
        return Result.success(deleted);
    }
}
